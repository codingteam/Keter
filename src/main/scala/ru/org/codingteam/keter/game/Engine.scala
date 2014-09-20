package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.actions.Action
import ru.org.codingteam.keter.game.objects.{Actor, ActorActive, ActorInactive}
import ru.org.codingteam.keter.map.{Universe, UniverseSnapshot}
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.EventQueue

import scala.concurrent.Future
import scala.util.Success

class Engine(val universe: Universe) extends IEngine with Logging {

  val eventQueue = new EventQueue()
  var callbacks = List[UniverseSnapshot => Unit]()

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  def start(): Unit = {
    // Initialize all actors:
    val currentState = universe.current
    val actions = Future.sequence(
      currentState.actors.map(actor => actor.getNextAction(currentState).map((actor, _))))
    actions andThen {
      case Success(as) =>
        as.foreach {
          case (actor, action) =>
            eventQueue.add(action, action.duration(currentState))
        }

        engineLoop()
    }
  }

  def registerCallback(callback: UniverseSnapshot => Unit): Unit = {
    callbacks +:= callback
  }

  private def engineLoop(): Unit = {
    val action = eventQueue.get().asInstanceOf[Action]
    log.debug(s"Processing action $action")
    var nextState = universe.current
    if (action.actor.state == ActorActive) {
      nextState = action.process(nextState, this)
    }

    nextState = performGlobalActions(nextState)
    nextState = nextState.updatedTimestamp(_ => eventQueue.getTime().toLong)
    universe.current = nextState
    callbacks.foreach(_(nextState))
    nextState.findActor(action.actor.id) match {
      case Some(actor) if actor.state == ActorActive =>
        actor.getNextAction(nextState) andThen {
          case Success(nextAction) =>
            eventQueue.add(nextAction, nextAction.duration(nextState))
            engineLoop()
        }
      case _ =>
        engineLoop()
    }
  }

  private def performGlobalActions(state: UniverseSnapshot): UniverseSnapshot = {
    state.copy(actors = checkDeaths(state.actors))
  }

  private def checkDeaths(actors: Seq[Actor]): Seq[Actor] = {
    actors.map(actor => if (actor.stats.health < 0) actor.copy(state = ActorInactive) else actor)
  }

  override def addMessage(msg: String): Unit = universe.addMessage(msg)
}
