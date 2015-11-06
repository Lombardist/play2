package controllers.registration

import javax.inject.Inject

import models.entities.{Company, CompanyDAO}
import play.api.cache.CacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}


class Registration @Inject()(val messagesApi: MessagesApi, val cacheApi: CacheApi) extends Controller with I18nSupport {

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

  def registrationSubmit = Action { implicit request =>
    val regForm = registrationForm.bindFromRequest.get

    if (regForm.email == regForm.email2 && regForm.email.length > 0 &&
      regForm.password == regForm.password2 && regForm.password.length > 0 &&
      regForm.company.length > 0 &&
      regForm.companyId.length > 0 &&
      regForm.name.length > 0 &&
      regForm.lastName.length > 0
    ) {
      val company = Company(0, regForm.company, regForm.companyId, regForm.email)
      CompanyDAO.save(company)
    }

    Redirect("/companies")
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
