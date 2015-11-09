package controllers.company

import javax.inject.Inject

import models.entities.Company.CompanyDAO
import models.entities.{Company}
import play.api.data.{Forms, Form}
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Flash, Action, Controller}
import Company.JsonMapper._

import scala.concurrent.duration.DurationInt

class Companies @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val companyListKey = "companyList"

  def list = Action {
    val companies = CompanyDAO.list.sortWith((one, two) => one.name.toLowerCase.compareTo(two.name.toLowerCase) < 0)
    Ok(views.html.company.companies(companies))
  }

  val companyForm: Form[Company] = Form(
    mapping(
      Company._name -> nonEmptyText,
      Company._identificator -> nonEmptyText,
      Company._email -> email
    )(Company.apply)(Company.unapply)
  )

  def companyJson(id: Int) = Action {
    CompanyDAO.companyById(id) match {
      case Some(value) => Ok(Json.toJson(value))
      case None => NotFound
    }
  }

  def companiesJson = Action {
    var companies = CompanyDAO.list
    Ok(Json.toJson(companies))
  }

}
