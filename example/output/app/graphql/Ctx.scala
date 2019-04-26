package graphql

import service._
import graphql.operations._

import scala.concurrent.ExecutionContext

case class Ctx(
                userService: UserService,
                categoryService: CategoryService
              )(implicit val executionContext: ExecutionContext)
  extends Users with Categories {

  override implicit val ec: ExecutionContext = executionContext
}

