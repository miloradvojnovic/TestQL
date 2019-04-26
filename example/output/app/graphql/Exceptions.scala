package graphql

import database.PSQLExceptionTransformer.DBError
import sangria.execution.UserFacingError

object Exceptions {
  final case class Unauthorized(message: String) extends Exception(message) with UserFacingError
  final case class BadRequest(message: String) extends Exception(message) with UserFacingError

  implicit class ServiceErrorTransformer(val throwable: Throwable) extends AnyVal {

    def handleException: Throwable =
      throwable match {
        case ile: IllegalArgumentException => BadRequest(ile.getMessage)
        case dbError: DBError              => BadRequest(dbError.message)
        case th: Throwable => th
      }
  }
}

