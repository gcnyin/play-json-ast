package example

import play.api.libs.json._
import play.api.libs.functional.syntax._

object App2 extends App {
  sealed trait Parent

  final case class ChildA(name: String, age: Int) extends Parent
  object ChildA {
    implicit val writes: Writes[ChildA] =
      ((JsPath \ "name").write[String] and (JsPath \ "age").write[Int])(unlift(ChildA.unapply))
    implicit val reads: Reads[ChildA] =
      ((JsPath \ "name").read[String] and (JsPath \ "age").read[Int])(ChildA.apply _)
  }

  final case class ChildB(amount: Double) extends Parent
  object ChildB {
    implicit val writes: Writes[ChildB] = (JsPath \ "amount").write[Double].contramap(c => c.amount)
    implicit val reads: Reads[ChildB] = (JsPath \ "amount").read[Double].map(ChildB(_))
  }

  println(Json.toJson(ChildA("tom", 23))) // {"name":"tom","age":23}
  println(Json.toJson(ChildB(1.01))) // {"amount":1.01}
}
