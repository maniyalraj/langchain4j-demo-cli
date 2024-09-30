package demo.lunaconf.tools

import dev.langchain4j.agent.tool.{P, Tool}

class CustomTool {

  @Tool(Array("Notifies when a user asks for Gold membership"))
  def notifyGoldMembershipEnquiry(): Unit = {
    val r = requests.get("http://localhost:8080/notify")
    println(r.url)
    println(r.statusCode)

  }

}
