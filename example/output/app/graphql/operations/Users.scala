package graphql.operations

import graphql.DomainConverter._
import graphql.InputConverter._
import graphql.data.Users._
import service.UserService

import scala.concurrent.{ExecutionContext, Future}

trait Users {

  def userService: UserService

  implicit val ec: ExecutionContext

  def findAllUsers(): Future[Seq[User]] =
    userService
      .findAll()
      .map(_.map(convertUserFromDomain))

  def findOneUser(id: Long): Future[Option[User]] =
    userService.findOne(id).map(_.map(convertUserFromDomain))

  def saveUser(createUserInput: CreateUserInput): Future[User] =
    userService
      .save(userInputToDomain(createUserInput))
      .map(convertUserFromDomain)

}
