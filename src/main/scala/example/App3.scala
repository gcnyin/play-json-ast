package example

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

object App3 extends App {
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

  object Parent {
    val aReads: Reads[ChildA] = ChildA.format
    val bReads: Reads[ChildB] = ChildB.format

    implicit val reads: Reads[Parent] = aReads.widen[Parent] or bReads.widen[Parent]
  }

  val j1: JsValue = Json.parse(
    """{
      |   "name": "tom",
      |   "age": 23
      |}""".stripMargin)
  val j2: JsValue = Json.parse(
    """{
      |   "amount": 123.32
      |}""".stripMargin)

  println(Json.fromJson[Parent](j1).get) // ChildA(tom,23)
  println(Json.fromJson[Parent](j2).get) // ChildB(123.32)
}
