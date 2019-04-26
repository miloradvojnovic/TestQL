package database

import com.google.inject.Inject
import database.table.ProductTable
import model.Product
import slick.ast.BaseTypedType
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class ProductRepository @Inject()(
    override val driver: PostgresProfile,
    override val dbConfig: DatabaseConfig[PostgresProfile]
)(override implicit val ec: ExecutionContext)
    extends BaseRepository[Product, Long](driver, dbConfig) {

  import driver.api._

  type TableType = ProductTable

  override def pkType = implicitly[BaseTypedType[Long]]
  override def tableQuery = ProductTable.products
}
