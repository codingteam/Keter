package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.{WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future
import scala.util.Random

object RandomBehavior extends IActorBehavior {

  val random = Random

  override def getNextAction(actor: Actor, state: UniverseSnapshot) = Future.successful {
    Move(random.nextInt(3) - 1, random.nextInt(3) - 1) match {
      case Move(0, 0, _) => WaitAction(actor)
      case move => WalkAction(actor, move)
    }
  }

}
