package example

import play.api.libs.json._
import play.api.libs.functional.syntax._

object App2 extends App {
  sealed trait Parent

  final case class ChildA(name: String, age: Int) extends Parent
  object ChildA {
    implicit val format: Format[ChildA] =
      ((__ \ "name").format[String] and (__ \ "age").format[Int])(ChildA.apply, unlift(ChildA.unapply))
  }

  final case class ChildB(amount: Double) extends Parent
  object ChildB {
    implicit val format: Format[ChildB] =
      (__ \ "amount").format[Double].bimap(ChildB.apply, unlift(ChildB.unapply))
  }

  println(Json.toJson(ChildA("tom", 23))) // {"name":"tom","age":23}
  println(Json.toJson(ChildB(1.01))) // {"amount":1.01}
}
