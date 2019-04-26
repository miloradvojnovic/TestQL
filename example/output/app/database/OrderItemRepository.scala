package database

import com.google.inject.Inject
import database.table.OrderItemTable
import model.OrderItem
import slick.ast.BaseTypedType
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class OrderItemRepository @Inject()(
    override val driver: PostgresProfile,
    override val dbConfig: DatabaseConfig[PostgresProfile]
)(override implicit val ec: ExecutionContext)
    extends BaseRepository[OrderItem, Long](driver, dbConfig) {

  import driver.api._

  type TableType = OrderItemTable

  override def pkType = implicitly[BaseTypedType[Long]]
  override def tableQuery = OrderItemTable.orderItems
}
