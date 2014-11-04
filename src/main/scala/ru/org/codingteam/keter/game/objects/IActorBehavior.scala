package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.UniverseSnapshot

import scala.concurrent.Future

trait IActorBehavior {

  def getNextAction(actorId: ActorId, state: UniverseSnapshot): Future[UniverseSnapshot]
}
