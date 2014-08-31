package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.actions.Action
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.rotjs.interface.EventQueue

import scala.concurrent.Future
import scala.util.Success

class Engine(var gameState: GameState) {

  val eventQueue = new EventQueue()
  var callbacks = List[GameState => Unit]()

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  def start(): Unit = {
    // Initialize all actors:
    val actors = gameState.map.objects.keys.filter({
      case a: Actor => true
      case _ => false
    }).map(_.asInstanceOf[Actor])
    val actions = Future.sequence(actors.map(_.getNextAction()))
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
    println(s"Processing action $action")

    gameState = action.process(gameState.copy(time = eventQueue.getTime().toLong))
    callbacks.foreach(_(gameState))

    val actor = action.actor
    if (actor.enabled) {
      actor.getNextAction() andThen {
        case Success(a) =>
          eventQueue.add(a, a.duration)
          engineLoop()
      }
    } else {
      engineLoop()
    }
  }

}
