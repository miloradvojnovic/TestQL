package model

import com.byteslounge.slickrepo.meta.Entity

case class OrderItem(
    override val id: Option[Long],
    quantity: Long,
    price: Double,
    productId: Long,
    shoppingCartId: Option[Long],
    orderId: Option[Long]
) extends Entity[OrderItem, Long] {
  def withId(id: Long): OrderItem = this.copy(id = Some(id))
}
