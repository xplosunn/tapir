package tapir

sealed trait Schema {
  def show: String
}

object Schema {
  case object SString extends Schema {
    def show: String = "string"
  }
  case object SInteger extends Schema {
    def show: String = "integer"
  }
  case object SNumber extends Schema {
    def show: String = "number"
  }
  case object SBoolean extends Schema {
    def show: String = "boolean"
  }
  case class SObject(info: SObjectInfo, fields: Iterable[(String, Schema)], required: Iterable[String]) extends Schema {
    def show: String = s"object(${fields.map(f => s"${f._1}->${f._2.show}").mkString(",")};required:${required.mkString(",")})"
  }
  case class SArray(element: Schema) extends Schema {
    def show: String = s"array(${element.show})"
  }
  case object SBinary extends Schema {
    def show: String = "binary"
  }
  case object SDate extends Schema {
    def show: String = "date"
  }
  case object SDateTime extends Schema {
    def show: String = "date-time"
  }

  case class SRef(info: SObjectInfo) extends Schema {
    def show: String = s"ref($info)"
  }

  case class SCoproduct(schemas: Set[Schema], discriminator: Option[Discriminator]) extends Schema {
    override def show: String = "oneOf:" + schemas.mkString(",")
  }

  case class SObjectInfo(fullName: String, typeParameterShortNames: List[String] = Nil)

  case class Discriminator(propertyName: String, mappingOverride: Map[String, SRef])
}
