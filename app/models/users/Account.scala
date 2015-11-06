package models.users

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import db.DefaultDbConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by root on 11/2/15.
 */
case class Account(login: String, passwordHash: String) {
}

object Account {

  def findById(_id: String) = Future {
    val dbUser = DefaultDbConfig.account.findOne(MongoDBObject("login" -> _id)).getOrElse(null)
    val user = if (dbUser != null) Account.fromDBObject(dbUser) else null
    user match {
      case Account(login, hash) => Some(Account(login, hash))
      case _ => None
    }
  }

  def authenticate(account: Account) = {
    findById(account.login).flatMap {
      case Some(user) => Future(user.passwordHash == account.passwordHash)
      case None => Future.successful(false)
    }
  }

  def authorize(account: Account) = {
    findById(account.login).flatMap {
      case Some(user) => Future(user.passwordHash == account.passwordHash)
      case None => Future.successful(false)
    }
  }

  implicit def toDBObject(obj: Account) = {
    MongoDBObject(
      "login" -> obj.login,
      "hash" -> obj.passwordHash
    )
  }

  def fromDBObject(obj: DBObject) = {
    Account(
      login = obj.get("login").asInstanceOf[String],
      passwordHash = obj.get("hash").asInstanceOf[String]
    )
  }
}