package demo.lunaconf.tools

import dev.langchain4j.agent.tool.Tool

class CustomTool {

  @Tool(Array("Notifies when a user asks for Gold membership"))
  def notifyGoldMembershipEnquiry(name: String): Unit = {
    val r = requests.get("http://localhost:8080/notify")
    println(name)
    println(s"URL: ${r.url}")
    println(s"Status Code: ${r.statusCode}")
  }
}
