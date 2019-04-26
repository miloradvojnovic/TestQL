package database

import com.google.inject.Inject
import database.table.CategoryTable
import model.Category
import slick.ast.BaseTypedType
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import scala.concurrent.{ExecutionContext, Future}

class CategoryRepository @Inject()(
    override val driver: PostgresProfile,
    override val dbConfig: DatabaseConfig[PostgresProfile]
)(override implicit val ec: ExecutionContext)
    extends BaseRepository[Category, Long](driver, dbConfig) {

  import driver.api._

  type TableType = CategoryTable

  override def pkType = implicitly[BaseTypedType[Long]]
  override def tableQuery = CategoryTable.categories

  def search(name: String): Future[Seq[Category]] =
    db.run((for {
      category <- tableQuery if category.name like "%" + name + "%"
    } yield category).result)
}
