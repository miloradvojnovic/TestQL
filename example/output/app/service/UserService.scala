package service

import com.google.inject.Inject
import database.UserRepository
import model.User

import scala.concurrent.Future

class UserService @Inject()(userRepository: UserRepository) {
  def findAll(): Future[Seq[User]] = userRepository.findAll()
  def findOne(id: Long): Future[Option[User]] = userRepository.findOne(id)
  def save(entity: User): Future[User] = userRepository.save(entity)
  def update(entity: User): Future[User] = userRepository.update(entity)
  def delete(entity: User): Future[User] = userRepository.delete(entity)
}
