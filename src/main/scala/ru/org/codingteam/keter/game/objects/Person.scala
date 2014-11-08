package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.actions.{Action, MeleeAttackAction, WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart
import ru.org.codingteam.keter.game.objects.equipment.items.Weapon
import ru.org.codingteam.keter.game.objects.equipment.{EquipmentItem, MeleeAttackCapability}
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}
import ru.org.codingteam.keter.util.Logging

import scala.concurrent.Future

case class Person(id: ActorId,
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

  override type SelfType = Person

  def decreaseHealth(amount: Int): Person = if (stats.health <= amount)
    copy(stats = stats.copy(health = 0), state = ActorInactive)
  else
    copy(stats = stats.copy(health = stats.health - amount))

  override def withEventQueue(e: EventQueue): Person = copy(eventQueue = e)

  override def withPosition(p: ActorPosition): Person = copy(position = p)

  def withSheduledNextEventAfter(dt: Double): Person = addEventAfter(dt, new ScheduledEvent {
    override def perform(universe: UniverseSnapshot): Future[UniverseSnapshot] =
      behavior.getNextAction(id, universe)
  })
}

object Person extends Logging {

  def processAction(action: Action, universe: UniverseSnapshot): UniverseSnapshot = {
    // TODO: messages to engine.
    @inline def processDefaultWait = processAction(WaitAction(action.actorId, 150), universe)
    universe.findActorOfType[Person](action.actorId) match {
      case Some(person) if person.state == ActorActive =>
        action match {
          case WaitAction(actorId, duration) =>
            universe.updatedActorOfType[Person](actorId)(_.withSheduledNextEventAfter(duration))
          case WalkAction(actorId, move) =>
            if (move.length == 0)
              processDefaultWait
            else {
              val duration = move.length * 250
              val newPosition = person.position.moveWithJumps(move)
              newPosition.surfaceAt match {
                case Some(s) if s.passable =>
                  universe.updatedActorOfType[Person](actorId)(_.withPosition(newPosition).withSheduledNextEventAfter(duration))
                case _ =>
                  processDefaultWait
              }
            }
          case MeleeAttackAction(actorId, target, damage) =>
            person.equipment.find(_.actions.contains(MeleeAttackCapability)) match {
              case Some(weapon: Weapon) =>
                val personsAt = universe.findActorsOfType[Person](target)
                //      log.debug(s"MeleeAttack: personsAt=$personsAt")
                if (personsAt.isEmpty)
                  processDefaultWait
                else {
                  personsAt.foldLeft(universe) {
                    (u, p) => u.updatedActorOfType[Person](p.id)(_.decreaseHealth(weapon.damage))
                  }.updatedActorOfType[Person](actorId)(_.withSheduledNextEventAfter(80))
                }
              case _ =>
                processDefaultWait
            }
        }
      case _ => universe
    }
  }
}
