package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.map.Universe
import ru.org.codingteam.keter.util.Logging

class Engine(val universe: Universe) extends IEngine with Logging {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  def start(): Unit = {
    engineLoop()
  }

  private def engineLoop(): Unit = {
    val state = universe.current
    state.nextEvent match {
      case Some((at, action, nextState)) =>
        log.debug(s"Processing action $action, timestamp=$at")
        action.perform(nextState) map { st =>
          // a simple implementation of universe update by an actor.
          log.debug(s"Action $action processed")
          universe.current = st
          engineLoop()
        }
      case None =>
        log.error(s"Universe death (no events), last timestamp=${state.globalEvents.timestamp}")
    }
  }

  override def addMessage(msg: String): Unit = universe.addMessage(msg)
}
