package db

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Play.current

object Collections {
  val user = "user"
  val session = "session"
  val sequence = "sequence"
  val account = "account"
  val company = "company"
  val warehouse = "warehouse"
  val addressReference = "addressReference"
}

trait DbConfig {
  def host: String
  def port: Int
  def dbName: String

  private val db = MongoClient(host, port)(dbName)
  private def collection(collection: String) = db(collection)

  val user = collection(Collections.user)
  val session = collection(Collections.session)
  val sequence = collection(Collections.sequence)
  val company = collection(Collections.company)
  val warehouse = collection(Collections.warehouse)
  val addressReference = collection(Collections.addressReference)
  val account = collection(Collections.account)

  def nextId(collection: String) = sequence
    .findAndModify(
      MongoDBObject("_id" -> collection), null, null, false,
      MongoDBObject("$inc" -> MongoDBObject("seq" -> 1.toInt)), true, true)
    .get.get("seq").asInstanceOf[Int]
}

/**
  * This Default config -> for Dev ops
  */
object DefaultDbConfig extends DbConfig {

  //val config = current.configuration.getConfig("mongo").getOrElse(null)

  def host = "127.0.0.1"
  def port = 27017
  def dbName = "lombard"
}
