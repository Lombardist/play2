package controllers.company

import javax.inject.Inject

import models.entities.{CompanyDAO, Company}
import play.api.cache.CacheApi
import play.api.data.{Forms, Form}
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Flash, Action, Controller}
import Company.JsonMapper._

import scala.concurrent.duration.DurationInt

class Companies @Inject() (val messagesApi: MessagesApi, val cacheApi: CacheApi) extends Controller with I18nSupport {

  val companyListKey = "companyList"

  def list = Action {
    var companies = cacheApi.get(companyListKey).getOrElse(List.empty[Company])
    if (companies.isEmpty) {
      companies = CompanyDAO.list.sortWith((one, two) => one.name.toLowerCase.compareTo(two.name.toLowerCase) < 0)
      cacheApi.set(companyListKey, companies, 300 seconds)
    }
    Ok(views.html.company.companies(companies))
  }

  val companyForm: Form[Company] = Form(
    mapping(
      Company._id -> number,
      Company.name -> nonEmptyText,
    Company.identificator -> nonEmptyText,
    Company.email -> email
    )(Company.apply)(Company.unapply)
  )

  def newCompany = Action {
    Ok(views.html.company.addCompany(companyForm))
  }

  def add = Action { implicit request =>
    var company = companyForm.bindFromRequest.get
    company = CompanyDAO.save(company)
    var companies = cacheApi.get(companyListKey).getOrElse(List.empty[Company])
    companies = (company :: companies).sortWith((one, two) => one.name.toLowerCase.compareTo(two.name.toLowerCase) < 0)
    cacheApi.remove(companyListKey)
    cacheApi.set(companyListKey, companies, 300 second)
    Redirect(routes.Companies.list())
  }

  def companyJson(id: Int) = Action {
    val companies = cacheApi.get(companyListKey).getOrElse(List.empty[Company])
    val company = companies.find( company => company._id == id).getOrElse( CompanyDAO.getById(id) )
    Ok(Json.toJson(company))
  }

  def companiesJson = Action {
    var companies = cacheApi.get(companyListKey).getOrElse(List.empty[Company])
    if (companies.isEmpty) {
      companies = CompanyDAO.list
      cacheApi.set(companyListKey, companies, 300 seconds)
    }
    Ok(Json.toJson(companies))
  }

}
