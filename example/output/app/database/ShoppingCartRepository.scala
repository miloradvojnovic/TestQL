package database

import com.google.inject.Inject
import database.table.ShoppingCartTable
import model.ShoppingCart
import slick.ast.BaseTypedType
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class ShoppingCartRepository @Inject()(
    override val driver: PostgresProfile,
    override val dbConfig: DatabaseConfig[PostgresProfile]
)(override implicit val ec: ExecutionContext)
    extends BaseRepository[ShoppingCart, Long](driver, dbConfig) {

  import driver.api._

  type TableType = ShoppingCartTable

  override def pkType = implicitly[BaseTypedType[Long]]
  override def tableQuery = ShoppingCartTable.shoppingCarts
}
