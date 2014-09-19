package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.{WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future

trait StupidMoverBehavior extends IActorBehavior {

  val to: Int

  def getNextAction(actor: Actor, state: UniverseSnapshot) = Future.successful {
    val move = state.actors.find(a => a.faction != actor.faction &&
      a.position.submap == actor.position.submap &&
      a.position.objectPosition != actor.position.objectPosition) map {
      a =>
        val selfPos = actor.position.objectPosition.coords
        val actorPos = a.position.objectPosition.coords
        val m = Move(math.signum(actorPos.x - selfPos.x) * to, math.signum(actorPos.y - selfPos.y) * to)
        actor.position.subspaceMatrix.rotateMoveBack(m)
    }
    move match {
      case Some(m) => WalkAction(actor, m)
      case None => WaitAction(actor)
    }
  }
}
