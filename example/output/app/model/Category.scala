package model

import com.byteslounge.slickrepo.meta.Entity

case class Category(
    override val id: Option[Long],
    name: String,
    superCategoryId: Option[Long] = None
) extends Entity[Category, Long] {
  def withId(id: Long): Category = this.copy(id = Some(id))
}
