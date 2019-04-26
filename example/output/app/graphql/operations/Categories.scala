package graphql.operations

import graphql.DomainConverter._
import graphql.InputConverter._
import graphql.data.Categories._
import service.CategoryService

import scala.concurrent.{ExecutionContext, Future}

trait Categories {

  def categoryService: CategoryService

  implicit val ec: ExecutionContext

  def findAllCategories(): Future[Seq[Category]] =
    categoryService
      .findAll()
      .map(_.map(convertCategoryFromDomain))

  def findOneCategory(id: Long): Future[Option[Category]] =
    categoryService.findOne(id).map(_.map(convertCategoryFromDomain))

  def saveCategory(createCategoryInput: CreateCategoryInput): Future[Category] =
    categoryService
      .save(categoryInputToDomain(createCategoryInput))
      .map(convertCategoryFromDomain)

  def searchCategories(name: String): Future[Seq[Category]] =
    categoryService.search(name).map(_.map(convertCategoryFromDomain))

}
