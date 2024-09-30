package demo.lunaconf.service

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.ollama.OllamaChatModel

trait DemoAiServices {

  def buildModel(): ChatLanguageModel
}

class OllamaGemma2Service extends DemoAiServices {

  override def buildModel(): ChatLanguageModel =
    OllamaChatModel
      .builder()
      .baseUrl("http://localhost:11434")
      .modelName("gemma2:9b")
      .logRequests(true)
      .logResponses(true)
      .build()
}

class OllamaLamma3_1Service extends DemoAiServices {

  override def buildModel(): ChatLanguageModel = OllamaChatModel
    .builder()
    .baseUrl("http://localhost:11434")
    .modelName("llama3.1")
    .logRequests(true)
    .logResponses(true)
    .build()
}
