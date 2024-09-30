package demo.lunaconf.tools

import dev.langchain4j.agent.tool.Tool

class CustomTool {

  @Tool
  def add(n1: Int, n2: Int): Double = {
    println("Executing this tool")
    n1 + n2 + 0.0001
  }

}
