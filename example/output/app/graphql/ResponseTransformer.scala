package graphql

import graphql.Exceptions.ServiceErrorTransformer

import scala.concurrent.{ExecutionContext, Future}

case class InvalidValidation(errors: Seq[Throwable]) extends Throwable

object ResponseTransformer {
  implicit class ResponseConverter[A](val future: Future[A]) extends AnyVal {

    def withConversion()(implicit ec: ExecutionContext,
                         m: Manifest[A]): Future[A] =
      future.recoverWith {
        case throwable: Throwable => Future.failed(throwable.handleException)
      }
  }
}
