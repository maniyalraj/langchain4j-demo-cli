package demo.lunaconf

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
import dev.langchain4j.service.AiServices

class DemoExamples() {

  private val model = new OllamaGemma2Service().buildModel()

  def demo1(): Unit =
    /** Hello World * */
    prettyPrint(model.generate("Hi, my name is Raj"))

  def demo2(): Unit = {

    /** Interaction with a Friendly persona
      */
    val friend = AiServices.create(classOf[Friendly], model)

    val response = friend.chat("Hello")

    prettyPrint(response)

  }

  def demo3(): Unit = {

    /** Interaction with a Sarcastic persona
      */
    val sarcasticAI = AiServices.create(classOf[Sarcastic], model)

    val response = sarcasticAI.chat("Hello")

    prettyPrint(response)

  }

  def demo4(): Unit = {

    /** Example of chat with a memory
      */
    val chatMemory = MessageWindowChatMemory.withMaxMessages(10)

    val sarcasticAI = AiServices
      .builder(classOf[Sarcastic])
      .chatLanguageModel(model)
      .chatMemory(chatMemory)
      .build()

// Add different personas to use the same chat memory
    val friendlyAI = AiServices
      .builder(classOf[Friendly])
      .chatLanguageModel(model)
      .chatMemory(chatMemory)
      .build()

    val response1 = sarcasticAI.chat("Hello, I am Raj")
    val response2 = friendlyAI.chat("What is my name?")

    prettyPrint(response1, response2)
  }

  def demo5(): Unit = {

    /** Custom POJO extractor
      */

    val lunatechEmployeeExtractor = AiServices.create(classOf[LunatechEmployeeExtractor], model)

    val textForExtraction =
      "Raj joined Lunatech in the summer of 2021, his family name is Maniyal and he was born on the first thursday of 1994 "
//    val textForExtraction = "Raj joined Lunatech in the summer of 2021, and he was born on the first thursday of 1994 "

    val result = lunatechEmployeeExtractor.extractEmployeeDetailFrom(textForExtraction)

    prettyPrint(result.toString)
  }

  def demo6(): Unit = {

    /** Calling custom tools
      */

    val lamma3_1model = new OllamaLamma3_1Service().buildModel()

    val serviceWithTool = AiServices
      .builder(classOf[ChatBotAssistant])
      .chatLanguageModel(lamma3_1model)
      .tools(new CustomTool())
      .build()

    val result = serviceWithTool.chat("What is the price of Golden memberships?")

    prettyPrint(result)

  }

  def demo7(): Unit = {

    /** Prompt templates
      */
    val planner = AiServices.create(classOf[ProfessionalPlanner], model)

    val itineraryPlanner = ItineraryPlanner(destination = "Rotterdam", days = 2)

    val response = planner.chat(promptTemplate(itineraryPlanner))

    prettyPrint(response)

  }

  def demo8(): Unit = {

    /** RAG with private information
      */

    val contentRetriever = new DocumentRetriever().getContentRetriever(
      "/Users/rajendra.maniyal/Desktop/Personal/Code/lanchain4j-demo-cli/src/main/resources/veloria.txt"
    )
    val planner = AiServices
      .builder(classOf[ProfessionalPlanner])
      .chatLanguageModel(model)
      .contentRetriever(contentRetriever)
      .build()

    val itineraryPlanner = ItineraryPlanner(destination = "Veloria", days = 2)
    val response = planner.chat(promptTemplate(itineraryPlanner))

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
