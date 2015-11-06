package controllers

import javax.inject.Inject

import _root_.db.DefaultDbConfig
import com.mongodb.casbah.commons.MongoDBObject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc
import play.api.mvc._
import play.api.mvc.Results._
import play.api.mvc.Controller
import play.mvc.Http.Context

class Authenticator extends Controller {

  def loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(Login.apply)(Login.unapply)
  )

  def login = Action {
    Ok(views.html.login(loginForm))
  }

  def auth = Action { implicit request =>
    val login = loginForm.bindFromRequest.get
    if (login.validate != null) {
      BadRequest(views.html.login(loginForm))
    }
    else {
      Redirect("/")
    }
  }

}

case class Login(email: String, password: String) {
  def validate = {
    null
  }
}