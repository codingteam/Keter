package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future
import scala.util.Random

object RandomBehavior extends IActorBehavior {

  val random = Random

  override def getNextAction(actor: Actor, state: UniverseSnapshot) = Future.successful {
    state.updatedActor(actor.id) { case a: Actor =>
      val WaitDuration = 150
      val move = Move(random.nextInt(3) - 1, random.nextInt(3) - 1) match {
        case m@Move(0, 0, _) => m.copy(dt = WaitDuration)
        case m@Move(dx, dy, _) => m.copy(dt = (math.hypot(dx, dy) * 250).toInt)
      }
      val newPosition = a.position.moveWithJumps(move)
      val duration = if (newPosition.surfaceAt.exists(_.passable)) move.dt else WaitDuration
      a.withPosition(newPosition).addEventAfter(duration, a.sheduledActionByBehaviour)
    }
  }
}
