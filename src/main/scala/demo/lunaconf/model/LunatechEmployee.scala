package demo.lunaconf.model

import dev.langchain4j.model.output.structured.Description
import dev.langchain4j.service.UserMessage

import java.time.LocalDate

@Description(Array("An employee of Lunatech"))
class LunatechEmployee {
//  Try case class, maybe map null with Option

  val firstName: String = ""
  val lastName: String = ""
  val dateOfBirth: LocalDate = LocalDate.now()
  val dateOfJoining: LocalDate = LocalDate.now()

  override def toString: String = {
    s"name: $firstName, $lastName, Dob: $dateOfBirth, DOJ: $dateOfJoining"
  }

}

trait LunatechEmployeeExtractor {

  @UserMessage(Array("Extract information about a Lunatech employee from {{it}}"))
  def extractEmployeeDetailFrom(text: String): LunatechEmployee

}