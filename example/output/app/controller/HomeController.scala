package controller

import play.api.mvc._
import com.google.inject.Inject

class HomeController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  def index = Action {
    Ok("Hello world!")
  }

}