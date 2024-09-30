package demo.lunaconf.model

import dev.langchain4j.model.output.structured.Description
import dev.langchain4j.service.UserMessage

import java.time.LocalDate

@Description(Array("An employee of Lunatech"))
case class LunatechEmployeeAIResponseDTO(
  firstName: String,
  lastName: String,
  dob: LocalDate,
  doj: LocalDate
) {
  override def toString: String =
    s"name: $firstName, $lastName, Dob: $dob, DOJ: $doj"
}

case class LunatechEmployee(
  firstName: Option[String],
  lastName: Option[String],
  dob: Option[LocalDate],
  doj: Option[LocalDate]
)

object LunatechEmployeeAIResponseDTO {
  def scalafy(lunatechEmployee: LunatechEmployeeAIResponseDTO): LunatechEmployee = LunatechEmployee(
    firstName = Option(lunatechEmployee.firstName),
    lastName = Option(lunatechEmployee.lastName),
    dob = Option(lunatechEmployee.dob),
    doj = Option(lunatechEmployee.doj)
  )
}

trait LunatechEmployeeExtractor {

  @UserMessage(Array("Extract information about a Lunatech employee from {{it}}"))
  def extractEmployeeDetailFrom(text: String): LunatechEmployeeAIResponseDTO

}
