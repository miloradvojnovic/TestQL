package database.table

import com.byteslounge.slickrepo.meta.Keyed
import model.Product
import slick.jdbc.PostgresProfile.api._

class ProductTable(tag: Tag) extends Table[Product](tag, "products") with Keyed[Long] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.Unique)
  def manufacturer = column[String]("manufacturer")
  def price = column[Double]("price")
  def description = column[String]("description")
  def productUrl = column[String]("product_url")
  def quantity = column[Int]("quantity")
  def categoryId = column[Long]("category_id")

  def category =
    foreignKey("products_category_id_fk", categoryId, CategoryTable.categories)(_.id)

  def * =
    (id.?,name, manufacturer, price, description, productUrl, quantity, categoryId) <> (Product.tupled, Product.unapply)
}

object ProductTable {

  lazy val products = TableQuery[ProductTable]

}
