package service

import com.google.inject.Inject
import database.ProductRepository
import model.Product

import scala.concurrent.Future

class ProductService @Inject()(productRepository: ProductRepository) {
  def findAll(): Future[Seq[Product]] = productRepository.findAll()
  def findOne(id: Long): Future[Option[Product]] = productRepository.findOne(id)
  def save(entity: Product): Future[Product] = productRepository.save(entity)
  def update(entity: Product): Future[Product] =
    productRepository.update(entity)
  def delete(entity: Product): Future[Product] =
    productRepository.delete(entity)
}
