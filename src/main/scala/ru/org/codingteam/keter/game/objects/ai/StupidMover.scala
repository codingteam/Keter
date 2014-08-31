package ru.org.codingteam.keter.game.objects.ai

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.{MoveAction, WaitAction}
import ru.org.codingteam.keter.game.objects.{Actor, Player}

import scala.concurrent.Future

trait StupidMover extends Actor {
  val to:Int

  def getNextAction(state: GameState) = Future.successful({
    var (x, y) = (0,0)
    for (obj <- state.map.objects) {
      obj._1 match {
        case player: Player => {
          x = math.signum(obj._2._1 - state.map.objects(this)._1) * to
          y = math.signum(obj._2._2 - state.map.objects(this)._2) * to
        }
        case _ => {}
      }
    }
    if (x == 0 && y == 0) {
      WaitAction(this)
    } else {
      MoveAction(this, x, y)
    }
  })
}
