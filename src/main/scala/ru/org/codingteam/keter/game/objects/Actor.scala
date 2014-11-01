package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.objects.Actor.ActorSheduledAction
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}

import scala.concurrent.Future

case class Actor(id: ActorId,
                 faction: Faction,
                 name: String,
                 tile: String,
                 state: ActorState,
                 behavior: IActorBehavior,
                 stats: StatTable,
                 equipment: Seq[EquipmentItem],
                 position: ActorPosition,
                 bodyparts: Set[Bodypart],
                 eventQueue: EventQueue) extends ActorLike {

  def getNextAction(state: UniverseSnapshot) = behavior.getNextAction(this, state)

  override def withEventQueue(e: EventQueue): ActorLike = copy(eventQueue = e)

  override def withPosition(p: ActorPosition): ActorLike = copy(position = p)

  lazy val sheduledActionByBehaviour = new ActorSheduledAction(this)
}

object Actor {

  class ActorSheduledAction(actor: Actor) extends ScheduledAction {
    override def perform(universe: UniverseSnapshot): Future[UniverseSnapshot] =
      actor.behavior.getNextAction(actor, universe)
  }

}
