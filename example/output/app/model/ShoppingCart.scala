package model

import com.byteslounge.slickrepo.meta.Entity

case class ShoppingCart(
    override val id: Option[Long],
    userId: Long
) extends Entity[ShoppingCart, Long] {
  def withId(id: Long): ShoppingCart = this.copy(id = Some(id))
}
