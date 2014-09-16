package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.actions.Action
import ru.org.codingteam.keter.map.UniverseSnapshot

import scala.concurrent.Future

trait IActorBehavior {

  def getNextAction(actor: Actor, state: UniverseSnapshot): Future[Action]

}
