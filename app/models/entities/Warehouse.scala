package models.entities

import javax.inject.Inject

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import db.{Collections, DefaultDbConfig}
import net.sf.ehcache.Cache
import play.api.cache.{EhCacheApi, CacheApi}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.collection.{mutable, immutable}

case class Warehouse(_id: Int = DefaultDbConfig.nextId(Collections.warehouse), name: String, companyId: Int)

object Warehouse {

  val _id = "_id"
  val name = "name"
  val companyId = "companyId"

  object JsonMapper {

    implicit val warehouseFormat: Format[Warehouse] = (
      (__ \ _id).format[Int] ~
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
      Warehouse(
        _id = obj.get(_id).asInstanceOf[Int],
        name = obj.get(name).asInstanceOf[String],
        companyId = obj.get(companyId).asInstanceOf[Int]
      )
    }
  }

}

object WarehouseDAO {

  def list = {
    DefaultDbConfig.warehouse.find.map { elem =>
      Warehouse.MongoMapper.fromDBObject(elem)
    }.toList
  }

  def getById(id: Int) = {
    val query = MongoDBObject("_id" -> id)
    DefaultDbConfig.warehouse.findOne(query) match {
      case Some(value) => Warehouse.MongoMapper.fromDBObject(value)
      case None => null
    }
  }

  def save(obj: Warehouse) = {
    val query = MongoDBObject("name" -> obj.name)
    DefaultDbConfig.warehouse.findOne(query) match {
      case Some(value) => throw new Exception
      case None =>
        DefaultDbConfig.warehouse.insert(Warehouse.MongoMapper.toDBObject(obj))
    }
  }

}