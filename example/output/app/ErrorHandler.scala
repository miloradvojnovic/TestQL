import play.api.Logger
import play.api.http.HttpErrorHandler
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

import scala.concurrent.Future

class ErrorHandler extends HttpErrorHandler {

  private val logger = Logger(getClass)

  def onClientError(
                     request: RequestHeader,
                     statusCode: Int,
                     message: String
                   ) =
    Future.successful(Status(statusCode)(s"A client error occurred: $message"))

  def onServerError(request: RequestHeader, exception: Throwable) = {
    logger.error("Error occurred", exception)
    Future.successful(InternalServerError)
  }
}
