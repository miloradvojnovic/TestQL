package model

import com.byteslounge.slickrepo.meta.Entity

case class User(
    override val id: Option[Long],
    username: String,
    password: String,
    firstName: String,
    lastName: String,
    address: String,
    phone: String,
    email: String,
    money: Double
) extends Entity[User, Long] {
  def withId(id: Long): User = this.copy(id = Some(id))
}
