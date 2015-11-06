package controllers.warehouse

import javax.inject.Inject

import models.entities.WarehouseDAO
import play.api.cache.CacheApi
import play.api.data.{Forms, Form}
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Flash, Action, Controller}
import models.entities.Warehouse.JsonMapper._

import scala.concurrent.duration.DurationInt

class Warehouses @Inject() (val messagesApi: MessagesApi, val cacheApi: CacheApi) extends Controller with I18nSupport {

  //List of Warehouses (JSON)
  def warehousesListJson = Action {
    val list = WarehouseDAO.list
    Ok(Json.toJson(list))
  }

  //Warehouse by ID (JSON)
  def warehouseJson(id: Int) = Action {
    val warehouse = WarehouseDAO.getById(id)
    Ok(Json.toJson(warehouse))
  }

  def warehouses = Action {
    val list = WarehouseDAO.list
    Ok(views.html.warehouse.warehouses(list))
  }

}
