package database.table

import com.byteslounge.slickrepo.meta.Keyed
import model.OrderItem
import slick.jdbc.PostgresProfile.api._

class OrderItemTable(tag: Tag)
  extends Table[OrderItem](tag, "order_items")
    with Keyed[Long] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def quantity = column[Long]("quantity")
  def price = column[Double]("price")
  def productId = column[Long]("product_id")
  def shoppingCartId = column[Long]("shopping_cart_id")
  def orderId = column[Long]("order_id")


  def product =
    foreignKey("order_items_product_id_fk",
      productId,
      ProductTable.products)(_.id)

  def shoppingCart =
    foreignKey("order_items_shopping_cart_id_fk",
      shoppingCartId,
      ShoppingCartTable.shoppingCarts)(_.id)

  def order =
    foreignKey("order_items_order_id_fk",
      orderId,
      OrderTable.orders)(_.id)

  def * = (id.?, quantity, price, productId, shoppingCartId.?, orderId.?) <> (OrderItem.tupled, OrderItem.unapply)
}

object OrderItemTable {

  lazy val orderItems = TableQuery[OrderItemTable]

}
