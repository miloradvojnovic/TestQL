package {{package}}

import graphql.manual.DataSpec
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsObject, JsValue}
import sangria.ast.Document
import sangria.execution.Executor
import sangria.marshalling.playJson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait GraphQLSpec extends PlaySpec with DataSpec with ScalaFutures {

  def executeQuery(
      query: Document,
      variables: JsObject = JsObject.empty
  ): JsValue = {
    val executor = Executor(
      schema = schema,
      exceptionHandler = exceptionHandler
    )

    executor
      .execute(query, ctx, (), variables = variables)
      .futureValue(Timeout(10.seconds))

  }
}

