package models.entities

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import db.{Collections, DefaultDbConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Company(var _id: Int = DefaultDbConfig.nextId(Collections.company),
                   name: String,
                   identificator: String,
                   email: String)

object Company {

  val _id = "_id"
  val identificator = "identificator"
  val name = "name"
  val email = "email"

  object JsonMapper extends {

    implicit val companyFormat: Format[Company] = (
      (__ \ _id).format[Int] ~
        (__ \ name).format[String] ~
        (__ \ identificator).format[String] ~
        (__ \ email).format[String]
      ) (Company.apply, unlift(Company.unapply))

  }

  object MongoMapper {

    def toDBObject(obj: Company) = {
      MongoDBObject(
        _id -> obj._id,
        name -> obj.name,
        identificator -> obj.identificator,
      email -> obj.email
      )
    }

    def fromDBObject(obj: DBObject) = {
      Company(
        _id = obj.get(_id).asInstanceOf[Int],
        name = obj.get(name).asInstanceOf[String],
        identificator = obj.get(identificator).asInstanceOf[String],
      email = obj.get(email).asInstanceOf[String]
      )
    }

  }

}

object CompanyDAO {

  def list = {
    DefaultDbConfig.company.find().map { elem =>
      Company.MongoMapper.fromDBObject(elem)
    }.toList
  }

  def getById(id: Int) = {
    val query = MongoDBObject("_id" -> id)
    DefaultDbConfig.company.findOne(query).map { elem =>
      Company.MongoMapper.fromDBObject(elem)
    }.getOrElse(null)
  }

  def save(company: Company) = {
    if (company._id == 0) company._id = DefaultDbConfig.nextId(Collections.company)
    DefaultDbConfig.company.insert(Company.MongoMapper.toDBObject(company))
    company
  }

}
