package service

import com.google.inject.Inject
import database.ShoppingCartRepository
import model.ShoppingCart

import scala.concurrent.Future

class ShoppingCartService @Inject()(
    shoppingCartRepository: ShoppingCartRepository) {
  def findAll(): Future[Seq[ShoppingCart]] = shoppingCartRepository.findAll()
  def findOne(id: Long): Future[Option[ShoppingCart]] =
    shoppingCartRepository.findOne(id)
  def save(entity: ShoppingCart): Future[ShoppingCart] =
    shoppingCartRepository.save(entity)
  def update(entity: ShoppingCart): Future[ShoppingCart] =
    shoppingCartRepository.update(entity)
  def delete(entity: ShoppingCart): Future[ShoppingCart] =
    shoppingCartRepository.delete(entity)
}
