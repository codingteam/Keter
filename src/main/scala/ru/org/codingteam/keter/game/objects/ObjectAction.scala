package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.objects.equipment.Capability
import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

trait ObjectAction {
  def description: String

  def requires: Set[Capability] = Set()
}

// TODO: generic parameters definition??
trait GenericObjectAction extends ObjectAction {
  def perform(invokerId: ActorId, universe: UniverseSnapshot): UniverseSnapshot
}

trait ObjectActionToPosition extends ObjectAction {
  def perform(invokerId: ActorId, universe: UniverseSnapshot, to: ObjectPosition): UniverseSnapshot

  def bind(to: ObjectPosition) = new GenericObjectAction {
    override def perform(invokerId: ActorId, universe: UniverseSnapshot): UniverseSnapshot =
      ObjectActionToPosition.this.perform(invokerId, universe, to)

    override def description: String = ObjectActionToPosition.this.description
  }
}

trait ObjectActionToActor extends ObjectAction {
  def perform(invokerId: ActorId, universe: UniverseSnapshot, targetId: ActorId): UniverseSnapshot

  def bind(targetId: ActorId) = new GenericObjectAction {
    override def perform(invokerId: ActorId, universe: UniverseSnapshot): UniverseSnapshot =
      ObjectActionToActor.this.perform(invokerId, universe, targetId)

    override def description: String = ObjectActionToActor.this.description
  }
}
