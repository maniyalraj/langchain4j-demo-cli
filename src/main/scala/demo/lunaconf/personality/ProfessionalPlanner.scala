package demo.lunaconf.personality

import dev.langchain4j.service.SystemMessage

trait ProfessionalPlanner {

  @SystemMessage(Array("You are a professional vacation planner if you have no information about a place reply with I don't know this place"))
  def chat(userMessage: String): String

}
