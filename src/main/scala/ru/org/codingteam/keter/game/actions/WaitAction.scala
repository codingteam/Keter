package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.{ActorId, GenericObjectAction, Person}
import ru.org.codingteam.keter.map.UniverseSnapshot

case class WaitAction(duration: Double = 50.0) extends GenericObjectAction {
  override def description: String = "wait"

  override def perform(invokerId: ActorId,
                       universe: UniverseSnapshot): UniverseSnapshot = Person.ifActorIsActive(invokerId, universe) {
    universe.updatedActorOfType[Person](invokerId)(_.withSheduledNextEventAfter(duration))
  }
}
