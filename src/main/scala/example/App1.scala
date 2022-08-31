package example

import play.api.libs.json._

object App1 extends App {
  sealed trait Parent

  final case class ChildA(name: String, age: Int) extends Parent
  object ChildA {
    implicit val format: Format[ChildA] = Json.format[ChildA]
  }

  final case class ChildB(amount: Double) extends Parent
  object ChildB {
    implicit val format: Format[ChildB] = Json.format[ChildB]
  }

  object Parent {
    val typeHintField = "_type"

    implicit val format: Format[Parent] = new Format[Parent] {
      override def writes(o: Parent): JsValue = {
        val (typeHint, jsValue) = o match {
          case c @ ChildA(name, age) => "childA" -> Json.toJson(c)
          case c @ ChildB(amount)    => "childB" -> Json.toJson(c)
        }
        jsValue.transform(JsPath.json.update((__ \ typeHintField).json.put(JsString(typeHint)))).get
      }

      override def reads(json: JsValue): JsResult[Parent] = {
        (json \ typeHintField).validate[String].flatMap {
          case "childA" => json.validate[ChildA]
          case "childB" => json.validate[ChildB]
        }
      }
    }
  }

  val p1: Parent = ChildA("tom", 23)
  println(Json.toJson(p1)) // {"name":"tom","_type":"childA","age":23}
  val p2: Parent = ChildB(0.11)
  println(Json.toJson(p2)) // {"amount":0.11,"_type":"childB"}
}
