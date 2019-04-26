package database.table

import com.byteslounge.slickrepo.meta.Keyed
import model.Order
import slick.jdbc.PostgresProfile.api._

class OrderTable(tag: Tag)
    extends Table[Order](tag, "orders")
    with Keyed[Long] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def totalPrice = column[Double]("total_price")
  def userId = column[Long]("user_id")

  def user =
    foreignKey("shopping_carts_user_id_fk", userId, UserTable.users)(_.id)

  def * = (id.?, totalPrice, userId) <> (Order.tupled, Order.unapply)
}

object OrderTable {

  lazy val orders = TableQuery[OrderTable]

}
