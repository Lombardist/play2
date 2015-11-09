package models.user

import cache.CacheGlobal
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import db.{Collections, DefaultDbConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.util.Try

case class User(name: String,
                lastName: String,
                email: String,
                role: String,
                login: String,
                var company_id: Int) {
  var _id: Int = DefaultDbConfig.nextId(Collections.user)
  var passwordHash: String = null
}

object User {

  val _id = "_id"
  val _name = "name"
  val _lastName = "lastName"
  val _email = "email"
  val _role = "role"
  val _login = "login"
  val _passHash = "passHash"
  val _company_id = "company_id"

  implicit val userFormat: Format[User] = (
    (__ \ _name).format[String] ~
      (__ \ _lastName).format[String] ~
      (__ \ _email).format[String] ~
      (__ \ _role).format[String] ~
      (__ \ _login).format[String] ~
      (__ \ _company_id).format[Int]
    ) (User.apply, unlift(User.unapply))

  object MongoMapper {

    def toDBObject(obj: User) = {
      MongoDBObject(
        _id -> obj._id,
        _name -> obj.name,
        _lastName -> obj.lastName,
        _email -> obj.email,
        _role -> obj.role,
        _login -> obj.login,
        _passHash -> obj.passwordHash,
        _company_id -> obj.company_id
      )
    }

    def fromDBObject(obj: DBObject) = {
      val user = User(
        name = obj.get(_name).asInstanceOf[String],
        lastName = obj.get(_lastName).asInstanceOf[String],
        email = obj.get(_email).asInstanceOf[String],
        role = obj.get(_role).asInstanceOf[String],
        login = obj.get(_login).asInstanceOf[String],
        company_id = obj.get(_company_id).asInstanceOf[Int]
      )
      user.passwordHash = obj.get(_passHash).asInstanceOf[String]
      user._id = obj.get(_id).asInstanceOf[Int]
      user
    }

  }

  object UserDAO {

    private val queryUserById = (id: Int) => MongoDBObject(_id -> id)
    private val queryUserByLogin = (login: String) => MongoDBObject(_login -> login)
    private val queryUsersByCompanyId = (id: Int) => MongoDBObject(_company_id -> id)
    private val queryUserByEmail = (email: String) => MongoDBObject(_email -> email)

    val userById: (Int => Option[User]) = (id: Int) => DefaultDbConfig.user.findOne(queryUserById(id)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }
    val userByLogin: (String => Option[User]) = (login: String) => DefaultDbConfig.user.findOne(queryUserByLogin(login)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }
    val usersByCompanyId: (Int => List[User]) = (id: Int) => DefaultDbConfig.user.find(queryUsersByCompanyId(id)).map {
      elem => MongoMapper.fromDBObject(elem)
    }.toList
    val userByEmail: (String => Option[User]) = (email: String) => DefaultDbConfig.user.findOne(queryUserByEmail(email)) match {
      case Some(value) => Some(MongoMapper.fromDBObject(value))
      case None => None
    }

    def save(user: User) = userByEmail(user.email) match {
      case None => Some(DefaultDbConfig.user.insert(MongoMapper.toDBObject(user)))
      case Some(value) => None
    }

    def delete(user: User) = DefaultDbConfig.user.remove(queryUserByEmail(user.email))

    def list = DefaultDbConfig.user.find().map {
      user => MongoMapper.fromDBObject(user)
    }.toList

  }

}
