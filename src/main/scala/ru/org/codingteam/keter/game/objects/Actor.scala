package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.actions.{Action, MeleeAttackAction, WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.Actor.ActorSheduledAction
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}

import scala.concurrent.Future

case class Actor(id: ActorId,
                 faction: Faction,
                 name: String,
                 tile: Option[String],
                 state: ActorState,
                 behavior: IActorBehavior,
                 stats: StatTable,
                 equipment: Seq[EquipmentItem],
                 position: ActorPosition,
                 bodyparts: Set[Bodypart],
                 eventQueue: EventQueue = EventQueue.empty) extends ActorLike {
  override type SelfType = Actor
  def decreaseHealth(amount: Int): Actor = if (stats.health <= amount)
    copy(stats = stats.copy(health = 0), state = ActorInactive)
  else
    copy(stats = stats.copy(health = stats.health - amount))

  def getNextAction(state: UniverseSnapshot) = behavior.getNextAction(this, state)

  override def withEventQueue(e: EventQueue): Actor = copy(eventQueue = e)

  override def withPosition(p: ActorPosition): Actor = copy(position = p)

  lazy val sheduledActionByBehaviour = new ActorSheduledAction(this)

  def withSheduledNextEventAfter(dt: Double): Actor = addEventAfter(dt, sheduledActionByBehaviour)

}

object Actor {

  class ActorSheduledAction(actor: Actor) extends ScheduledAction {
    override def perform(universe: UniverseSnapshot): Future[UniverseSnapshot] =
      actor.behavior.getNextAction(actor, universe)
  }

  def processAction(action: Action, universe: UniverseSnapshot): UniverseSnapshot = action match {
    case WaitAction(actor, duration) =>
      universe.updatedActor(actor.id)(_.asInstanceOf[Actor].withSheduledNextEventAfter(duration))
    case WalkAction(actor, move) =>
      if (move.length == 0)
        processAction(WaitAction(actor, 150), universe)
      else {
        val duration = move.length * 250
        val newPosition = actor.position.moveWithJumps(move)
        newPosition.surfaceAt match {
          case Some(s) if s.passable =>
            universe.updatedActor(actor.id)(_.withPosition(newPosition).asInstanceOf[Actor].withSheduledNextEventAfter(duration))
          case _ =>
            processAction(WaitAction(actor, 150), universe)
        }
      }
    case MeleeAttackAction(actor, target, damage) =>
      val actorsAt = universe.findActors(target)
      if (actorsAt.isEmpty)
        processAction(WaitAction(actor, 150), universe)
      else {
        actorsAt.foldLeft(universe)((u, ai) => ai match {
          case (actorId, _actor: Actor) => u.updatedActor(actorId) {
            case actor: Actor => actor.decreaseHealth(damage).withSheduledNextEventAfter(80)
          }
          case _ => u
        })
      }
  }
}
