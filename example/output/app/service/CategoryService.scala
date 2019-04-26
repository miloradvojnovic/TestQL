package service

import com.google.inject.Inject
import database.CategoryRepository
import model.Category

import scala.concurrent.Future

class CategoryService @Inject()(categoryRepository: CategoryRepository) {
  def findAll(): Future[Seq[Category]] = categoryRepository.findAll()
  def findOne(id: Long): Future[Option[Category]] =
    categoryRepository.findOne(id)
  def save(entity: Category): Future[Category] = categoryRepository.save(entity)
  def update(entity: Category): Future[Category] =
    categoryRepository.update(entity)
  def delete(entity: Category): Future[Category] =
    categoryRepository.delete(entity)
  def search(name: String): Future[Seq[Category]] =
    categoryRepository.search(name)
}
