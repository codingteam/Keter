package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.WalkAction
import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future
import scala.util.Random

object RandomBehavior extends IActorBehavior {

  val random = Random

  override def getNextAction(actor: Actor, state: UniverseSnapshot) = Future.successful {
    val move = Move(random.nextInt(3) - 1, random.nextInt(3) - 1)
    val action = WalkAction(actor, move)
    Actor.processAction(action, state)
  }
}
