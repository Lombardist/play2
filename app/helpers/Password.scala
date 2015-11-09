package helpers

import java.security.MessageDigest

object Password {

  def md5(value: String): String = MessageDigest.getInstance("MD5").digest(value.getBytes).map { elem =>
    Integer.toString((elem & 0xff) + 0x100, 16).substring(1)
  }.mkString

}
