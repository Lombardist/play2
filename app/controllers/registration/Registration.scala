package controllers.registration

import java.util.UUID
import javax.inject.Inject

import akka.actor.Status.Success
import cache.CacheGlobal
import db.DefaultDbConfig
import helpers.Password
import models.entities.Company.CompanyDAO
import models.entities.{Company}
import models.user.User.UserDAO
import models.user.{Administrator, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{Messages, I18nSupport, MessagesApi}
import play.api.libs.mailer.{AttachmentFile, Email, MailerClient}
import play.api.mvc.{Request, RequestHeader, Action, Controller}

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global


class Registration @Inject()(val messagesApi: MessagesApi, val mailer: MailerClient) extends Controller with I18nSupport {

  val registrationForm: Form[RegistrationForm] = Form(
    mapping(
      "company" -> nonEmptyText,
      "companyId" -> nonEmptyText,
      "email" -> email,
      "email2" -> email,
      "password" -> nonEmptyText,
      "password2" -> nonEmptyText,
      "name" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "accountId" -> nonEmptyText
    )(RegistrationForm.apply)(RegistrationForm.unapply)
  )

  def registrationView = Action {
    Ok(views.html.registration.registration(registrationForm))
  }

  def registrationContinue(hash: String) = Action {
    CompanyDAO.companyByHash(hash) match {
      case Some(value) => {
        value.hash = "activated"
        Ok(views.html.internal.main(value))
      }
      case None => {
        NotFound
      }
    }
  }

  def registrationSubmit = Action { implicit request =>
    val Form = registrationForm.bindFromRequest
    //TODO: Need tobe re-implemented in future, cause we have to responde with errors
    Form.fold(
      hasErrors = { regForm =>
        Redirect(routes.Registration.registrationView())
      },
      success = { regForm =>
        val company = Company(regForm.company, regForm.companyId, regForm.email)
        company.hash = UUID.randomUUID().toString
        val user = User(regForm.name, regForm.lastName, regForm.email, Administrator.name, regForm.email, company._id)
        user.passwordHash = Password.md5(regForm.password)
        if (registerNewCompany(company, user)) {
          Redirect("/")
        }
        else
          Redirect(routes.Registration.registrationView())
      }
    )
  }

  def registerNewCompany(company: Company, user: User)(implicit request: RequestHeader) = CompanyDAO.save(company) match {
    case Some(value) => UserDAO.save(user) match {
      case Some(value) => {
        mailer.send(
          Email(
            "Registration",
            "registration@cloud-lombard.com",
            Seq(company.email),
            bodyText = Some("Registration"),
            bodyHtml = Some(views.html.registration.email(company.hash, Messages("registration.email.body")).body)
          )
        )
        true
      }
      case None => {
        //TODO: Have to think, maybe we need to delete this user
        false
      }
    }
    case None => {
      //TODO: Have to think, maybe we need to delete this company
      false
    }
  }

}

case class RegistrationForm(company: String,
                            companyId: String,
                            email: String,
                            email2: String,
                            password: String,
                            password2: String,
                            name: String,
                            lastName: String,
                            accountId: String)
