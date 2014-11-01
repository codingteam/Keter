package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future

trait StupidMoverBehavior extends IActorBehavior {

  val to: Int

  def getNextAction(oldActor: Actor, state: UniverseSnapshot) = Future.successful {
    state.updatedActor(oldActor.id) { case actor: Actor =>
      val move = state.actors.find {
        case (_id, a: Actor) => a.faction != actor.faction &&
          a.position.submap == actor.position.submap &&
          a.position.objectPosition != actor.position.objectPosition
        case _ => false
      } map { case (_id, a) =>
        val selfPos = actor.position.objectPosition.coords
        val actorPos = a.position.objectPosition.coords
        val m = Move(math.signum(actorPos.x - selfPos.x) * to, math.signum(actorPos.y - selfPos.y) * to)
        actor.position.subspaceMatrix.rotateMoveBack(m)
      }
      val WaitDuration = 250
      val m2 = move match {
        case Some(m) => m match {
          case m@Move(0, 0, _) => m.copy(dt = WaitDuration)
          case m@Move(dx, dy, _) => m.copy(dt = (math.hypot(dx, dy) * 250).toInt)
        }
        case None => Move(0, 0, WaitDuration)
      }
      val newPosition = actor.position.moveWithJumps(m2)
      val duration = if (newPosition.surfaceAt.exists(_.passable)) m2.dt else WaitDuration
      actor.withPosition(newPosition).addEventAfter(duration, actor.sheduledActionByBehaviour)
    }

  }
}
