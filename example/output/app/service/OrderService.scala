package service

import com.google.inject.Inject
import database.OrderRepository
import model.Order

import scala.concurrent.Future

class OrderService @Inject()(orderRepository: OrderRepository) {
  def findAll(): Future[Seq[Order]] = orderRepository.findAll()
  def findOne(id: Long): Future[Option[Order]] = orderRepository.findOne(id)
  def save(entity: Order): Future[Order] = orderRepository.save(entity)
  def update(entity: Order): Future[Order] = orderRepository.update(entity)
  def delete(entity: Order): Future[Order] = orderRepository.delete(entity)
}
