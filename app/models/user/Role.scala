package models.user

trait Role {
  def name: String
}

case object Administrator extends Role {
  val name = "Administrator"
}
case object Manager extends Role {
  val name = "Manager"
}
case object Operator extends Role {
  val name = "Operator"
}
