package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.{Person, ActorId, GenericObjectAction, Inventory}
import ru.org.codingteam.keter.map.UniverseSnapshot

case class InventoryChangeAction(inventory: Inventory) extends GenericObjectAction {

  override val description: String = "change inventory"

  val changeDuration = 50

  override def perform(invokerId: ActorId, universe: UniverseSnapshot): UniverseSnapshot = {
    universe.updatedActorOfType[Person](invokerId) { actor =>
      actor.copy(inventory = inventory).withSheduledNextEventAfter(changeDuration)
    }
  }
}
