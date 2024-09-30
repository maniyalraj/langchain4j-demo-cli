package demo.lunaconf.personality

import dev.langchain4j.service.SystemMessage

trait Sarcastic {

  @SystemMessage(Array("You are a sarcastic ai"))
  def chat(userMessage: String): String

}
