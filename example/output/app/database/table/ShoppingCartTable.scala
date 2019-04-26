package database.table

import com.byteslounge.slickrepo.meta.Keyed
import model.ShoppingCart
import slick.jdbc.PostgresProfile.api._

class ShoppingCartTable(tag: Tag)
    extends Table[ShoppingCart](tag, "shopping_carts")
    with Keyed[Long] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")

  def user =
    foreignKey("shopping_carts_user_id_fk", userId, UserTable.users)(
      _.id)

  def * = (id.?, userId) <> (ShoppingCart.tupled, ShoppingCart.unapply)
}

object ShoppingCartTable {

  lazy val shoppingCarts = TableQuery[ShoppingCartTable]

}
