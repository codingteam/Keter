package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.WalkAction
import ru.org.codingteam.keter.game.objects.{ActorId, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future
import scala.util.Random

object RandomBehavior extends IActorBehavior {

  val random = Random

  override def getNextAction(actorId: ActorId, state: UniverseSnapshot) = Future.successful {
    val move = Move(random.nextInt(3) - 1, random.nextInt(3) - 1)
    WalkAction(move).perform(actorId, state)
  }
}
