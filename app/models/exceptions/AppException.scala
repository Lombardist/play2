package models.exceptions

/**
  * Created by root on 11/4/15.
  */
abstract class AppException(message: String, code: Int) extends Exception(message)
