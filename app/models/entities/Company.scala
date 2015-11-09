package models.entities

import cache.CacheGlobal
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import db.{Collections, DefaultDbConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.util.Success

case class Company(name: String,
                   identificator: String,
                   email: String) {
  var _id: Int = DefaultDbConfig.nextId(Collections.company)
  var hash: String = ""
}

object Company {

  val _id = "_id"
  val _identificator = "identificator"
  val _name = "name"
  val _email = "email"
  val _hash = "hash"

  object JsonMapper extends {

    implicit val companyFormat: Format[Company] = (
      (__ \ _name).format[String] ~
        (__ \ _identificator).format[String] ~
        (__ \ _email).format[String]
      ) (Company.apply, unlift(Company.unapply))

  }

  object MongoMapper {

    def toDBObject(obj: Company) = {
      MongoDBObject(
        _id -> obj._id,
        _name -> obj.name,
        _identificator -> obj.identificator,
        _email -> obj.email,
        _hash -> obj.hash
      )
    }

    def fromDBObject(obj: DBObject) = {
      val company = Company(
        name = obj.get(_name).asInstanceOf[String],
        identificator = obj.get(_identificator).asInstanceOf[String],
        email = obj.get(_email).asInstanceOf[String]
      )
      company._id = obj.get(_id).asInstanceOf[Int]
      company.hash = obj.get(_hash).asInstanceOf[String]
      company
    }

  }

  object CompanyDAO {

    private val queryCompanyById = (id: Int) => MongoDBObject(_id -> id)
    private val queryCompanyByIdentificator = (id: String) => MongoDBObject(_identificator -> id)
    private val queryCompanyByEmail = (email: String) => MongoDBObject(_email -> email)
    private val queryCompanyByHash = (hash: String) => MongoDBObject(_hash -> hash)

    val companyById: (Int => Option[Company]) = (id: Int) => DefaultDbConfig.company.findOne(queryCompanyById(id)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }
    val companyByIdentificator: (String => Option[Company]) = (id: String) => DefaultDbConfig.company.findOne(queryCompanyByIdentificator(id)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }
    val companyByEmail: (String => Option[Company]) = (email: String) => DefaultDbConfig.company.findOne(queryCompanyByEmail(email)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }
    val companyByHash: (String => Option[Company]) = (hash: String) => DefaultDbConfig.company.findOne(queryCompanyByHash(hash)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }

    def save(company: Company) = companyByEmail(company.email) match {
      case None => Some(DefaultDbConfig.company.insert(MongoMapper.toDBObject(company)))
      case Some(value) => None
    }

    def delete(company: Company) = DefaultDbConfig.company.remove(queryCompanyByEmail(company.email))

    def list = DefaultDbConfig.company.find().map {
      elem => MongoMapper.fromDBObject(elem)
    }.toList

  }

}
