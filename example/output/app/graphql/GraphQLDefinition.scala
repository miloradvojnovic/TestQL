package graphql

import com.google.inject.Inject
import graphql.data.{Categories, Users}
import sangria.execution.{ExceptionHandler, HandledException}
import sangria.schema._

import scala.concurrent.ExecutionContext

class GraphQLDefinition @Inject()(implicit val ec: ExecutionContext) {

  def schema: Schema[Ctx, Unit] = {
    val Query = ObjectType("Query",
                           fields[Ctx, Unit](Users.users,
                                             Users.user,
                                             Categories.categories,
                                             Categories.category,
                                             Categories.searchCategories))
    val Mutation = ObjectType(
      "Mutation",
      fields[Ctx, Unit](Users.createUser, Categories.createCategory))
    Schema(Query, Some(Mutation))
  }

  def exceptionHandler: ExceptionHandler =
    ExceptionHandler(onUserFacingError = {
      case (m, e: Throwable) =>
        val fields = Map(
          "type" -> m.scalarNode(e.getClass.getSimpleName, "String", Set.empty))
        HandledException(e.getMessage,
                         fields,
                         addFieldsInExtensions = false,
                         addFieldsInError = true)
    })

}
