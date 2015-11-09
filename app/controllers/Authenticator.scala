package controllers

import javax.inject.Inject

import _root_.db.DefaultDbConfig
import com.mongodb.casbah.commons.MongoDBObject
import helpers.Password
import models.user.User.UserDAO
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc
import play.api.mvc._
import play.api.mvc.Results._
import play.api.mvc.Controller
import play.mvc.Http.Context

class Authenticator @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(Login.apply)(Login.unapply)
  )

  def login = Action {
    Ok(views.html.main.login(loginForm))
  }

  def auth = Action { implicit request =>
    val login = loginForm.bindFromRequest.get
    if (!login.validate) {
      BadRequest(views.html.main.login(loginForm))
    }
    else {
      Redirect("/")
    }
  }

}

case class Login(email: String, password: String) {

  def validate = UserDAO.userByEmail(email) match {
    case Some(user) => user.passwordHash == Password.md5(password)
    case None => false
  }

}
