package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart
import ru.org.codingteam.keter.game.objects.equipment.{Capability, EquipmentItem}
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}

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

  def capabilities(universe: UniverseSnapshot): Set[Capability] = {
    // TODO: check capabilities provided by other sources (objects, equipment).
    bodyparts.map(_.provides).reduce(_ | _)
  }
}

object Person {
  def ifActorIsActive(personId: ActorId, oldUniverse: UniverseSnapshot)(newUniverse: => UniverseSnapshot): UniverseSnapshot =
    oldUniverse.findActorOfType[Person](personId) match {
      case Some(p) if p.state == ActorActive => newUniverse
      case _ => oldUniverse
    }

  def ifActorIsActiveF(personId: ActorId, oldUniverse: UniverseSnapshot)(newUniverseFunc: Person => UniverseSnapshot): UniverseSnapshot =
    oldUniverse.findActorOfType[Person](personId) match {
      case Some(p) if p.state == ActorActive => newUniverseFunc(p)
      case _ => oldUniverse
    }
}