package demo.lunaconf

import demo.lunaconf.model.LunatechEmployeeExtractor
import demo.lunaconf.personality.{Friendly, ProfessionalPlanner, Sarcastic}
import demo.lunaconf.prompts.ItineraryPlanner
import demo.lunaconf.prompts.ItineraryPlanner.promptTemplate
import demo.lunaconf.rag.DocumentRetriever
import demo.lunaconf.service.{DemoAiServices, OllamaGemma2Service, OllamaLamma3_1Service}
import demo.lunaconf.tools.CustomTool
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.service.AiServices

import java.io.File
import java.nio.file.{Files, Path}
import java.util.ResourceBundle

object Main extends App {

  private val model = new OllamaGemma2Service().buildModel()

  private val demoExamples = new DemoExamples(model)

  val demo = args(0).toInt

  demo match {
    case 1 => demoExamples.demo1()
    case 2 => demoExamples.demo2()
    case 3 => demoExamples.demo3()
    case 4 => demoExamples.demo4()
    case 5 => demoExamples.demo5()
    case 6 => demoExamples.demo6()
    case 7 => demoExamples.demo7()
    case 8 => demoExamples.demo8()
    case _ => println("Enter between 1 to 8")
  }

}

class DemoExamples(model: ChatLanguageModel) {

  private def prettyPrint(message: Object) = {
    println(s"""================
               |${message.toString}
               |================""".stripMargin)
  }

  def demo1(): Unit = {

    /** Add comments for the demo**/
    prettyPrint(model.generate("Hi, I am Raj"))
  }

  def demo2():Unit = {

    val friend = AiServices.create(classOf[Friendly], model)

    val response = friend.chat("Hello")

    prettyPrint(response)

  }


  def demo3():Unit = {

    val sarcasticAI = AiServices.create(classOf[Sarcastic], model)

    val response = sarcasticAI.chat("Hello")

    prettyPrint(response)

  }

  def demo4(): Unit = {
    val chatMemory = MessageWindowChatMemory.withMaxMessages(10)

    val sarcasticAI = AiServices.builder(classOf[Sarcastic])
      .chatLanguageModel(model)
      .chatMemory(chatMemory)
      .build()

// Add different personas to use the same chat memory
    val friendlyAI = AiServices.builder(classOf[Friendly])
      .chatLanguageModel(model)
      .chatMemory(chatMemory)
      .build()

    val response1 = sarcasticAI.chat("Hello, I am Raj")
    val response2 = friendlyAI.chat("What is my name?")

    prettyPrint(response1)
    prettyPrint(response2)
  }

  def demo5(): Unit = {

    val lunatechEmployeeExtractor = AiServices.create(classOf[LunatechEmployeeExtractor], model)

    val textForExtraction = "Raj joined Lunatech in the summer of 2021, his family name is Maniyal and he was born on the first thursday of 1994 "
//    val textForExtraction = "Raj joined Lunatech in the summer of 2021, and he was born on the first thursday of 1994 "

    val result = lunatechEmployeeExtractor.extractEmployeeDetailFrom(textForExtraction)

    prettyPrint(result)

  }

  def demo6():Unit = {

// Check with a business specific tool and also check if we can call an external api

    val lamma3_1model = new OllamaLamma3_1Service().buildModel()

    val serviceWithTool = AiServices.builder(classOf[Friendly])
      .chatLanguageModel(lamma3_1model)
      .tools(new CustomTool())
      .build()

    val result = serviceWithTool.chat("Add 1 and 2 return double")

    prettyPrint(result)

  }

  def demo7(): Unit = {
    val planner = AiServices.create(classOf[ProfessionalPlanner], model)

    val itineraryPlanner = ItineraryPlanner(destination = "Rotterdam", days = 2)

    val response = planner.chat(promptTemplate(itineraryPlanner))

    prettyPrint(response)

  }

  def demo8():Unit = {

    val contentRetriever = new DocumentRetriever().getContentRetriever("/Users/rajendra.maniyal/Desktop/Personal/Code/lanchain4j-demo-cli/src/main/resources/veloria.txt")
    val planner = AiServices.builder(classOf[ProfessionalPlanner]).chatLanguageModel(model).contentRetriever(contentRetriever).build()

    val itineraryPlanner = ItineraryPlanner(destination = "Veloria", days = 2)
    val response =  planner.chat(promptTemplate(itineraryPlanner))

    prettyPrint(response)
  }

}
