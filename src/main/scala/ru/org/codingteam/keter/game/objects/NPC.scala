package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.WaitAction

import scala.concurrent.Future

case class NPC(override val name: String, override val tile: String) extends Actor(name, tile) {

  override val playerControllable = false
  override def getNextAction(state: GameState) = Future.successful(WaitAction(this))

}
