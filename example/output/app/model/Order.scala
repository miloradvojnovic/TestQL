package model

import com.byteslounge.slickrepo.meta.Entity

case class Order(
    override val id: Option[Long],
    totalPrice: Double,
    userId: Long
) extends Entity[Order, Long] {
  def withId(id: Long): Order = this.copy(id = Some(id))
}
