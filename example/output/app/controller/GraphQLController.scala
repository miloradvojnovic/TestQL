package controller

import com.google.common.net.HttpHeaders
import com.google.inject.Inject
import graphql.{Ctx, GraphQLDefinition}
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import sangria.ast.Document
import sangria.execution._
import sangria.marshalling.playJson._
import sangria.parser.{QueryParser, SyntaxError}
import service._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class GraphQLController @Inject()(
    cc: ControllerComponents,
    userService: UserService,
    categoryService: CategoryService
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  def graphql: Action[JsValue] = Action.async(parse.json) { request =>
    val auth = request.headers.get(HttpHeaders.AUTHORIZATION)
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) => Some(parseVariables(vars))
      case obj: JsObject  => Some(obj)
      case _              => None
    }

    QueryParser.parse(query) match {
      case Success(queryAst) =>
        executeGraphQLQuery(auth, queryAst, operation, variables)
      case Failure(error: SyntaxError) =>
        Future.successful(BadRequest(Json.obj("error" -> error.getMessage)))
    }
  }

  private def parseVariables(variables: String) =
    if (variables.trim.isEmpty || variables.trim == "null") {
      Json.obj()
    } else {
      Json.parse(variables).as[JsObject]
    }

  private def executeGraphQLQuery(
      auth: Option[String],
      query: Document,
      op: Option[String],
      variables: Option[JsObject]
  ) = {
    val ctx = Ctx(userService, categoryService)
    Executor
      .execute(
        new GraphQLDefinition().schema,
        query,
        ctx,
        operationName = op,
        variables = variables.getOrElse(Json.obj()),
        exceptionHandler = new GraphQLDefinition().exceptionHandler
      )
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
      }
  }
}
