package demo.lunaconf.prompts

import dev.langchain4j.model.input.structured.StructuredPrompt

case class ItineraryPlanner(destination: String, days: Int)

case object ItineraryPlanner {
  def promptTemplate(planner: ItineraryPlanner): String = s"""
                          |Create a travel itinerary for a ${planner.destination} trip that lasts ${planner.days} days.
                          |Structure your answer in the following way:
                          |
                          |Destination: ${planner.destination}
                          |Number of days: ${planner.days}
                          |Itinerary:
                          |Day 1:
                          |- ...
                          |Day 2:
                          |- ...
                          |Day ${planner.days}:
                          |- ...
                          |""".stripMargin
}
