package ru.org.codingteam.keter.game.objects.ai

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.{MoveAction, WaitAction}
import ru.org.codingteam.keter.game.objects.Actor

import scala.concurrent.Future
import scala.util.Random

trait RandomBot extends Actor {
  val random = Random
  override val playerControllable = false
  override def getNextAction(state: GameState) = Future.successful({
    val (x, y) = (random.nextInt(3)-1, random.nextInt(3)-1)
    if (x == 0 && y == 0) {
      WaitAction(this)
    } else {
      MoveAction(this, x, y)
    }
  })
}
