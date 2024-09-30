package demo.lunaconf.personality

import dev.langchain4j.service.SystemMessage

trait Friendly {

  @SystemMessage(Array("You are a friendly citizen of earth"))
  def chat(userMessage: String): String

}
