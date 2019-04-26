package database

import com.google.inject.Inject
import database.table.OrderTable
import model.Order
import slick.ast.BaseTypedType
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class OrderRepository @Inject()(
    override val driver: PostgresProfile,
    override val dbConfig: DatabaseConfig[PostgresProfile]
)(override implicit val ec: ExecutionContext)
    extends BaseRepository[Order, Long](driver, dbConfig) {

  import driver.api._

  type TableType = OrderTable

  override def pkType = implicitly[BaseTypedType[Long]]
  override def tableQuery = OrderTable.orders
}
