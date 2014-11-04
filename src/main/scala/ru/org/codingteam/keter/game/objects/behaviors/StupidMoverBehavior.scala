package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.{WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.{Actor, ActorId, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future

trait StupidMoverBehavior extends IActorBehavior {

  val to: Int

  def getNextAction(actorId: ActorId, state: UniverseSnapshot) = Future.successful {
    state.findActorOfType[Actor](actorId) match {
      case Some(actor) =>
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
        val action = move match {
          case Some(m) => WalkAction(actorId, m)
          case None => WaitAction(actorId)
        }
        Actor.processAction(action, state)
      case None => state
    }
  }
}
