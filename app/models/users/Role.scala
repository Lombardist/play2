package models.users

import models.exceptions.AppException
import play.api.libs.json.Json

trait Role {
  def name: String
}

case object Regular {
  val name = "Regular"
}

case object RegularLeader {
  val name = "RegularLeader"
}

case object Administrator {
  val name = "Administrator"
}

case class UndefinedRoleException(val message: String, val code: Int) extends AppException(message, code)

object Role {

  def fromString(value: String) = value match {
    case Regular.name => Regular
    case RegularLeader.name => RegularLeader
    case Administrator.name => Administrator
    case undefined => throw UndefinedRoleException(s"error=; name=${undefined}", 1)
  }
}