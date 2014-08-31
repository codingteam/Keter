package ru.org.codingteam.keter.game.objects

import util.Random;

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.MoveAction
import ru.org.codingteam.keter.game.actions.WaitAction
import scala.concurrent.Future

case class RandomBot(override val name: String, override val tile: String) extends Actor(name, tile) {
  val random = Random;
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
