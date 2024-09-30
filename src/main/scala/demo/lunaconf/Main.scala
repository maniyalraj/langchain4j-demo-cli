package demo.lunaconf

import demo.lunaconf.service.OllamaGemma2Service

object Main extends App {
  private val demoExamples = new DemoExamples()

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