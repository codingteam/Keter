package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.actions.Action
import ru.org.codingteam.keter.game.objects.{Actor, ActorActive, ActorId, ActorInactive}
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.EventQueue

import scala.concurrent.Future
import scala.util.Success

class Engine(var gameState: GameState) extends Logging {

  val eventQueue = new EventQueue()
  var callbacks = List[GameState => Unit]()

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  def start(): Unit = {
    // Initialize all actors:
    val actions = Future.sequence(gameState.map.actors.values.map(_.getNextAction(gameState)))
    actions andThen {
      case Success(as) =>
        as.foreach(action => {
          eventQueue.add(action, action.duration)
        })
        engineLoop()
    }
  }

  def registerCallback(callback: GameState => Unit): Unit = {
    callbacks = callback +: callbacks
  }

  private def engineLoop(): Unit = {
    val action = eventQueue.get().asInstanceOf[Action]
    log.debug(s"Processing action $action")

    if (action.actor.state == ActorActive) {
      gameState = action.process(gameState.copy(time = eventQueue.getTime().toLong))
    }

    gameState = performGlobalActions(gameState)
    callbacks.foreach(_(gameState))

    val actor = gameState.map.actors(action.actor.id)
    if (actor.state == ActorActive) {
      actor.getNextAction(gameState) andThen {
        case Success(a) =>
          eventQueue.add(a, a.duration)
          engineLoop()
      }
    } else {
      engineLoop()
    }
  }

  private def performGlobalActions(state: GameState): GameState = {
    val map = state.map
    state.copy(map = map.copy(actors = checkDeaths(map.actors)))
  }

  private def checkDeaths(actors: Map[ActorId, Actor]): Map[ActorId, Actor] = {
    actors.map({ case (id, actor) =>
      val newActor = if (actor.stats.health < 0) actor.copy(state = ActorInactive) else actor
      (id, newActor)
    })
  }

}
