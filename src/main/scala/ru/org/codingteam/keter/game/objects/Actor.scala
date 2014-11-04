package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.actions.{Action, MeleeAttackAction, WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.Actor.ActorSheduledAction
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart
import ru.org.codingteam.keter.game.objects.equipment.items.Weapon
import ru.org.codingteam.keter.game.objects.equipment.{EquipmentItem, MeleeAttackCapability}
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}
import ru.org.codingteam.keter.util.Logging

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

  override def withEventQueue(e: EventQueue): Actor = copy(eventQueue = e)

  override def withPosition(p: ActorPosition): Actor = copy(position = p)

  lazy val sheduledActionByBehaviour = new ActorSheduledAction(this)

  def withSheduledNextEventAfter(dt: Double): Actor = addEventAfter(dt, sheduledActionByBehaviour)
}

object Actor extends Logging {

  class ActorSheduledAction(actor: Actor) extends ScheduledAction {
    override def perform(universe: UniverseSnapshot): Future[UniverseSnapshot] =
      actor.behavior.getNextAction(actor.id, universe)
  }

  def processAction(action: Action, universe: UniverseSnapshot): UniverseSnapshot = {
    // TODO: messages to engine.
    @inline def processDefaultWait = processAction(WaitAction(action.actorId, 150), universe)
    universe.findActorOfType[Actor](action.actorId) match {
      case Some(actor) if actor.state == ActorActive =>
        action match {
          case WaitAction(actorId, duration) =>
            universe.updatedActorOfType[Actor](actorId)(_.withSheduledNextEventAfter(duration))
          case WalkAction(actorId, move) =>
            if (move.length == 0)
              processDefaultWait
            else {
              val duration = move.length * 250
              val newPosition = actor.position.moveWithJumps(move)
              newPosition.surfaceAt match {
                case Some(s) if s.passable =>
                  universe.updatedActorOfType[Actor](actorId)(_.withPosition(newPosition).withSheduledNextEventAfter(duration))
                case _ =>
                  processDefaultWait
              }
            }
          case MeleeAttackAction(actorId, target, damage) =>
            actor.equipment.find(_.actions.contains(MeleeAttackCapability)) match {
              case Some(weapon: Weapon) =>
                val actorsAt = universe.findActorsOfType[Actor](target)
                //      log.debug(s"MeleeAttack: actorsAt=$actorsAt")
                if (actorsAt.isEmpty)
                  processDefaultWait
                else {
                  actorsAt.foldLeft(universe) {
                    (u, a) => u.updatedActorOfType[Actor](a.id)(_.decreaseHealth(weapon.damage))
                  }.updatedActorOfType[Actor](actorId)(_.withSheduledNextEventAfter(80))
                }
              case _ =>
                processDefaultWait
            }
        }
      case _ => universe
    }
  }
}
