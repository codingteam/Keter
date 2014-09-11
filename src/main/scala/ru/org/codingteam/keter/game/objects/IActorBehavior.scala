package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.Action

import scala.concurrent.Future

trait IActorBehavior {

  def getNextAction(actor: Actor, gameState: GameState): Future[Action]

}
