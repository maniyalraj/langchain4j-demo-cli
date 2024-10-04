package demo.lunaconf

import demo.lunaconf.model.LunatechEmployeeAIResponseDTO
import demo.lunaconf.model.LunatechEmployeeExtractor
import demo.lunaconf.personality.ChatBotAssistant
import demo.lunaconf.personality.Friendly
import demo.lunaconf.personality.ProfessionalPlanner
import demo.lunaconf.personality.Sarcastic
import demo.lunaconf.prompts.ItineraryPlanner
import demo.lunaconf.prompts.ItineraryPlanner.promptTemplate
import demo.lunaconf.rag.DocumentRetriever
import demo.lunaconf.service.OllamaGemma2Service
import demo.lunaconf.service.OllamaLamma3_1Service
import demo.lunaconf.tools.CustomTool
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.service.AiServices

class DemoExamples() {

  private val model = new OllamaGemma2Service().buildModel()

  /* Hello World */
  def demo1(): Unit =
    prettyPrint(model.generate("Hi, my name is Raj"))

  /* Interaction with a Friendly persona */
  def demo2(): Unit = {

    val friend = AiServices.create(classOf[Friendly], model)

    val response = friend.chat("Hello")

    prettyPrint(response)
  }

  /* Interaction with a Sarcastic persona */
  def demo3(): Unit = {

    val sarcasticAI = AiServices.create(classOf[Sarcastic], model)

    val response = sarcasticAI.chat("Hello")

    prettyPrint(response)
  }

  /* Example of chat with a memory      */
  def demo4(): Unit = {

    val chatMemory: MessageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10)

    val sarcasticAI: Sarcastic = AiServices
      .builder(classOf[Sarcastic])
      .chatLanguageModel(model)
      .chatMemory(chatMemory)
      .build()

    val friendlyAI: Friendly = AiServices
      .builder(classOf[Friendly])
      .chatLanguageModel(model)
      .chatMemory(chatMemory)
      .build()

    val response1: String = sarcasticAI.chat("Hello, I am Raj")
    val response2: String = friendlyAI.chat("What is my name?")

    prettyPrint(response1, response2)
  }

  /* Custom POJO extractor */
  def demo5(): Unit = {

    val lunatechEmployeeExtractor: LunatechEmployeeExtractor =
      AiServices.create(classOf[LunatechEmployeeExtractor], model)

    val textForExtraction = "Raj joined Lunatech in the summer of 2021, " +
      "his family name is Maniyal and he was born on the first thursday of 1994 "

//    val textForExtraction = "Raj joined Lunatech in the summer of 2021, " +
//      "and he was born on the first thursday of 1994 "

    val result: LunatechEmployeeAIResponseDTO =
      lunatechEmployeeExtractor.extractEmployeeDetailFrom(textForExtraction)

    prettyPrint(result.toString)
  }

  /* Calling custom tools */
  def demo6(): Unit = {

    val chatMemory: MessageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10)

    val lamma3_1model: ChatLanguageModel = new OllamaLamma3_1Service().buildModel()

    val serviceWithTool: ChatBotAssistant = AiServices
      .builder(classOf[ChatBotAssistant])
      .chatLanguageModel(lamma3_1model)
      .chatMemory(chatMemory)
      .tools(new CustomTool())
      .build()

    serviceWithTool.chat("Hello I am raj")
    val result: String = serviceWithTool.chat("What is the price of Golden memberships?")

    prettyPrint(result)
  }

  /* Prompt templates  */
  def demo7(): Unit = {

    val planner: ProfessionalPlanner = AiServices.create(classOf[ProfessionalPlanner], model)

    val itineraryPlanner: ItineraryPlanner = ItineraryPlanner(destination = "Rotterdam", days = 2)

    val response: String = planner.chat(promptTemplate(itineraryPlanner))

    prettyPrint(response)
  }

  /* RAG with private information  */
  def demo8(): Unit = {

    val lamma3_1model: ChatLanguageModel = new OllamaLamma3_1Service().buildModel()

    val contentRetriever = new DocumentRetriever()
      .getContentRetriever(
        "http://localhost:8081/Solar%20Power%20Optimization%20System%20(SPOS).pdf"
      )

    val friendly = AiServices
      .builder(classOf[Friendly])
      .chatLanguageModel(lamma3_1model)
      .contentRetriever(contentRetriever)
      .build()

    val response = friendly.chat("Explain some key concepts in SPOS?")

    prettyPrint(response)
  }

  private def prettyPrint(message: String*): Unit = {
    printf("\n================ Model response ================\n")
    message.foreach(println)
    printf("\n================================================\n")
  }

  println(
    """
      |██╗     ██╗   ██╗███╗   ██╗ █████╗  ██████╗ ██████╗ ███╗   ██╗███████╗    ██████╗  ██████╗ ██████╗ ██╗  ██╗
      |██║     ██║   ██║████╗  ██║██╔══██╗██╔════╝██╔═══██╗████╗  ██║██╔════╝    ╚════██╗██╔═████╗╚════██╗██║  ██║
      |██║     ██║   ██║██╔██╗ ██║███████║██║     ██║   ██║██╔██╗ ██║█████╗       █████╔╝██║██╔██║ █████╔╝███████║
      |██║     ██║   ██║██║╚██╗██║██╔══██║██║     ██║   ██║██║╚██╗██║██╔══╝      ██╔═══╝ ████╔╝██║██╔═══╝ ╚════██║
      |███████╗╚██████╔╝██║ ╚████║██║  ██║╚██████╗╚██████╔╝██║ ╚████║██║         ███████╗╚██████╔╝███████╗     ██║
      |╚══════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝         ╚══════╝ ╚═════╝ ╚══════╝     ╚═╝
      |""".stripMargin
  )
}
