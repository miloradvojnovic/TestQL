package database.table

import com.byteslounge.slickrepo.meta.Keyed
import model.Category
import slick.jdbc.PostgresProfile.api._

class CategoryTable(tag: Tag)
    extends Table[Category](tag, "categories")
    with Keyed[Long] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.Unique)
  def superCategoryId = column[Long]("super_category_id")

  def superCategory =
    foreignKey("categories_super_category_id_fk",
               superCategoryId,
               CategoryTable.categories)(_.id)

  def * = (id.?, name, superCategoryId.?) <> (Category.tupled, Category.unapply)
}

object CategoryTable {

  lazy val categories = TableQuery[CategoryTable]

}
