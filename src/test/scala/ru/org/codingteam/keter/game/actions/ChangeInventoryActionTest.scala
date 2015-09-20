package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.Location
import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Head
import ru.org.codingteam.keter.game.objects.equipment.items.Knife
import utest._

object ChangeInventoryActionTest extends TestSuite {
  val universe = Location.createLocation()
  val action = InventoryChangeAction(Inventory(Set(Head("h", 10)), Set(Knife("k")), Set()))

  val tests = TestSuite {
    'shouldChangeInventory {
      val newState = action.perform(universe.playerId.get, universe)
      val player = newState.player.get

      assert(player.inventory == action.inventory)
    }
  }
}
