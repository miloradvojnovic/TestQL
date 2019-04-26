package database

import database.PSQLExceptionTransformer.DBErrorType.DBErrorType
import org.apache.commons.lang3.StringUtils
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

object PSQLExceptionTransformer {

  object DBErrorType extends Enumeration {
    type DBErrorType = Value
    val UNIQUE_VIOLATION, FOREIGN_KEY_VIOLATION = Value
  }

  final case class DBError(`type`: DBErrorType,
                           detailedMessage: String,
                           message: String,
                           column: String,
                           table: String)
      extends Exception(message)

  private val logger = LoggerFactory.getLogger(getClass)

  implicit class DBResult[A](val future: Future[A]) extends AnyVal {

    def withPSQLException(
        )(implicit ec: ExecutionContext, m: Manifest[A]): Future[A] =
      future.recoverWith {
        case psqlEx: PSQLException =>
          logger.error(psqlEx.getMessage)
          Future.failed(transform(psqlEx))
        case ex =>
          logger.error(ex.getMessage)
          Future.failed(ex)
      }
  }

  def transform(ex: PSQLException): Exception =
    ex.getSQLState match {
      case UniquenessError => transformUniqueViolation(ex)
      case ForeignKeyError => transformFKViolation(ex)
      case _               => ex
    }

  def transformUniqueViolation(ex: PSQLException): DBError = {
    val column = ex.getServerErrorMessage.getDetail match {
      case UniqueExPattern(cmn) => cmn
      case _                    => StringUtils.EMPTY
    }

    val table = ex.getServerErrorMessage.getTable
    val message =
      s"${tableEntityMapper.getOrElse(table, StringUtils.EMPTY)} with same $column already exists."

    DBError(
      `type` = DBErrorType.UNIQUE_VIOLATION,
      detailedMessage = ex.getLocalizedMessage,
      message = message,
      column = column,
      table = table
    )
  }

  def transformFKViolation(ex: PSQLException): DBError = {
    val (column, identifier) = ex.getServerErrorMessage.getDetail match {
      case FKExPattern(cmn, id) => (cmn, id)
      case _                    => (StringUtils.EMPTY, StringUtils.EMPTY)
    }

    val message =
      s"${columnEntityMapper.getOrElse(column, StringUtils.EMPTY)} with id $identifier does not exist."

    DBError(
      `type` = DBErrorType.FOREIGN_KEY_VIOLATION,
      detailedMessage = ex.getLocalizedMessage,
      message = message,
      column = column,
      table = ex.getServerErrorMessage.getTable
    )
  }

  val UniquenessError = "23505"
  val UniqueExPattern: Regex = """Key \((.*)\)=\(.*\) already exists.""".r
  val ForeignKeyError = "23503"
  val FKExPattern: Regex =
    """Key \((.*)\)=\((.*)\) is not present in table .*""".r

  val columnEntityMapper = Map(
    "user_id" -> "User"
  )

  val tableEntityMapper = Map(
    "users" -> "User"
  )
}
