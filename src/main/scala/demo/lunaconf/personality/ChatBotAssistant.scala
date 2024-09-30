package demo.lunaconf.personality

import dev.langchain4j.service.SystemMessage

trait ChatBotAssistant {

  @SystemMessage(
    Array(
      "You are a friendly assistant of a vacation planner of LunaVacation and responds with 'Insufficient Information' if you cannot call tools, we provide 3 memberships Gold:129Euros, Silver:50Euros, Bronze:10Euros"
    )
  )
  def chat(userMessage: String): String

}
