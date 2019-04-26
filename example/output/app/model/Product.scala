package model

import com.byteslounge.slickrepo.meta.Entity

case class Product(
    override val id: Option[Long],
    name: String,
    manufacturer: String,
    price: Double,
    description: String,
    productUrl: String,
    quantity: Int,
    categoryId: Long
) extends Entity[Product, Long] {
  def withId(id: Long): Product = this.copy(id = Some(id))
}
