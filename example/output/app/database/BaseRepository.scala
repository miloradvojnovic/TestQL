package database

import com.byteslounge.slickrepo.meta.Entity
import com.byteslounge.slickrepo.repository.Repository
import com.byteslounge.slickrepo.scalaversion.JdbcProfile
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import database.PSQLExceptionTransformer._

import scala.concurrent.{ExecutionContext, Future}

abstract class BaseRepository[A <: Entity[A, ID], ID](
    override val driver: JdbcProfile,
    val dbConfig: DatabaseConfig[PostgresProfile]
)(implicit m: Manifest[A], implicit val ec: ExecutionContext)
    extends Repository[A, ID](driver) {

  val db: PostgresProfile#Backend#Database = dbConfig.db

  def findAll(): Future[Seq[A]] = db.run(super.findAll())

  def findOne(id: ID): Future[Option[A]] = db.run(super.findOne(id))

  def save(entity: A): Future[A] =
    db.run(super.save(entity)).withPSQLException()

  def update(entity: A): Future[A] =
    db.run(super.update(entity)).withPSQLException()

  def delete(entity: A): Future[A] =
    db.run(super.delete(entity)).withPSQLException()

}
