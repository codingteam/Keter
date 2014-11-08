package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.{WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.{ActorId, IActorBehavior, Person}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

import scala.concurrent.Future

trait StupidMoverBehavior extends IActorBehavior {

  val to: Int

  def getNextAction(actorId: ActorId, state: UniverseSnapshot) = Future.successful {
    state.findActorOfType[Person](actorId) match {
      case Some(person) =>
        val move = state.actors.find {
          case (_id, p: Person) => p.faction != person.faction &&
            p.position.submap == person.position.submap &&
            p.position.objectPosition != person.position.objectPosition
          case _ => false
        } map { case (_id, a) =>
          val selfPos = person.position.objectPosition.coords
          val actorPos = a.position.objectPosition.coords
          val m = Move(math.signum(actorPos.x - selfPos.x) * to, math.signum(actorPos.y - selfPos.y) * to)
          person.position.subspaceMatrix.rotateMoveBack(m)
        }
        val action = move match {
          case Some(m) => WalkAction(actorId, m)
          case None => WaitAction(actorId)
        }
        Person.processAction(action, state)
      case None => state
    }
  }
}
