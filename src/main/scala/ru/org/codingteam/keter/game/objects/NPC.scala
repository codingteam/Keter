package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.WaitAction

import scala.concurrent.Future

case class NPC(override val gameState: GameState,
               override val name: String,
               override val tile: String) extends Actor(gameState, name, tile) {

  override val playerControllable = false
  override def getNextAction() = Future.successful(WaitAction(this))

}
