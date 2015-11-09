package controllers.warehouse

import javax.inject.Inject

import play.api.data.{Forms, Form}
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Flash, Action, Controller}
import models.entities.Warehouse.JsonMapper._

import scala.concurrent.duration.DurationInt

class Warehouses @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

}
