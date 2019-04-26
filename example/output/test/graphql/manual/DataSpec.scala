package graphql.manual

import java.io.PrintWriter

import graphql.{Ctx, GraphQLDefinition}
import model.{Category, User}
import org.mockito.Mockito.when
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import sangria.introspection._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json.{JsValue, Json}
import sangria.ast.Document
import sangria.execution.{ExceptionHandler, Executor}
import sangria.marshalling.playJson._
import sangria.schema.Schema
import service.{CategoryService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait DataSpec extends MockitoSugar with ScalaFutures {

  val schema: Schema[Ctx, Unit] = new GraphQLDefinition().schema
  val exceptionHandler: ExceptionHandler =
    new GraphQLDefinition().exceptionHandler

  val userId: Long = 1
  val badUserId: Long = 2
  val user: User = User(Some(userId),
                        "miloradvojnovic",
                        "pass123",
                        "Milorad",
                        "Vojnovic",
                        "Puskinova 27",
                        "06978954558",
                        "milorad.vojnovic@gmail.com",
                        100000)
  val anotherUser: User = User(Some(badUserId),
                               "zikazikic",
                               "pass123",
                               "Zika",
                               "Zikic",
                               "Puskinova 27",
                               "06978954558",
                               "zika.zikic@gmail.com",
                               100000)
  val badUser = User(Some(badUserId),
                     "miloradvojnovic",
                     "pass123",
                     "Pera",
                     "Peric",
                     "Puskinova 28",
                     "06978954559",
                     "pera.peric@gmail.com",
                     100000)
  val userService: UserService = mock[UserService]
  when(userService.findAll())
    .thenReturn(Future.successful(Seq(user, anotherUser)))
  when(userService.findOne(userId)).thenReturn(Future.successful(Some(user)))
  when(userService.findOne(badUserId)).thenReturn(Future.successful(None))
  when(userService.save(user.copy(id = None)))
    .thenReturn(Future.successful(user))
  when(userService.save(badUser.copy(id = None)))
    .thenReturn(Future.failed(
      new IllegalArgumentException("User with same username already exists.")))

  val categoryId: Long = 1
  val badCategoryId: Long = 2
  val category: Category = Category(Some(categoryId), "RAM memory")
  val anotherCategory: Category = Category(Some(badCategoryId), "SSD disk")
  val badCategory = Category(Some(badCategoryId), "Processors")
  val categoryService: CategoryService = mock[CategoryService]
  when(categoryService.findAll())
    .thenReturn(Future.successful(Seq(category, anotherCategory)))
  when(categoryService.findOne(categoryId))
    .thenReturn(Future.successful(Some(category)))
  when(categoryService.findOne(badCategoryId))
    .thenReturn(Future.successful(None))
  when(categoryService.save(category.copy(id = None)))
    .thenReturn(Future.successful(category))
  when(categoryService.save(badCategory.copy(id = None)))
    .thenReturn(Future.failed(
      new IllegalArgumentException("Category with same name already exists.")))
  when(categoryService.search("intel")).thenReturn(
    Future.successful(
      Seq(Category(Some(1), "Intel i5-7540X"),
          Category(Some(2), "Intel i7-7740X"),
          Category(Some(3), "Intel i9-7940X"))))
  when(categoryService.search("kingston")).thenReturn(
    Future.successful(
      Seq(Category(Some(1), "Kingston SSD 120GB"),
          Category(Some(2), "Kingston SSD 240GB"))))
  val ctx = Ctx(userService, categoryService)

  def executeQuery(query: Document): JsValue = {
    val executor = Executor(
      schema = schema,
      exceptionHandler = exceptionHandler
    )

    executor
      .execute(query, ctx, ())
      .futureValue(Timeout(10.seconds))

  }

}
