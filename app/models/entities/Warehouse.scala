package models.entities

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import db.{Collections, DefaultDbConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.collection.{mutable, immutable}

case class Warehouse(name: String,
                     companyId: Int) {
  var _id: Int = DefaultDbConfig.nextId(Collections.warehouse)
}

object Warehouse {

  val _id = "_id"
  val name = "name"
  val companyId = "companyId"

  object JsonMapper {

    implicit val warehouseFormat: Format[Warehouse] = (
        (__ \ name).format[String] ~
        (__ \ companyId).format[Int]
      ) (Warehouse.apply, unlift(Warehouse.unapply))

  }

  object MongoMapper {

    def toDBObject(obj: Warehouse) = {
      MongoDBObject(
        _id -> obj._id,
        name -> obj.name,
        companyId -> obj.companyId
      )
    }

    def fromDBObject(obj: DBObject) = {
      val warehouse = Warehouse(
        name = obj.get(name).asInstanceOf[String],
        companyId = obj.get(companyId).asInstanceOf[Int]
      )
      warehouse._id = obj.get(_id).asInstanceOf[Int]
      warehouse
    }
  }

  object WarehouseDAO {

  }

}
