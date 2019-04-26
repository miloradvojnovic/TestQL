package service

import com.google.inject.Inject
import database.OrderItemRepository
import model.OrderItem

import scala.concurrent.Future

class OrderItemService @Inject()(orderItemRepository: OrderItemRepository) {
  def findAll(): Future[Seq[OrderItem]] = orderItemRepository.findAll()
  def findOne(id: Long): Future[Option[OrderItem]] = orderItemRepository.findOne(id)
  def save(entity: OrderItem): Future[OrderItem] = orderItemRepository.save(entity)
  def update(entity: OrderItem): Future[OrderItem] = orderItemRepository.update(entity)
  def delete(entity: OrderItem): Future[OrderItem] = orderItemRepository.delete(entity)
}
