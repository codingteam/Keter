package ru.org.codingteam.keter.scenes.inventory

import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.game.objects.equipment.EquipmentCategory._
import ru.org.codingteam.keter.game.objects.equipment.{EquipmentCategory, EquipmentItem}
import ru.org.codingteam.keter.ui.viewmodels.{ItemsViewModel, StaticTextViewModel}
import ru.org.codingteam.keter.util.VectorMap

import scala.concurrent.Promise

class InventoryViewModel(var inventory: Inventory) {

  private val promise = Promise[Option[Inventory]]()
  val result = promise.future

  def applyChanges(): Unit = {
    promise.success(Some(inventory))
  }

  def rejectChanges(): Unit = {
    promise.success(None)
  }

  val backpackCategories = new ItemsViewModel(
    VectorMap(All -> "ALL", Hat -> "Hats", Weapon -> "Weapons")
  ) {

    override protected def onSelectedItemChanged(item: Option[EquipmentCategory.Value]): Unit = {
      val folderItems = item.toSeq flatMap (category => inventory.allEquipment.filter(e => category.contains(e.category)))
      currentFolderItems.items = toVectorMap(folderItems)
    }
  }

  val currentFolderItems = new ItemsViewModel[EquipmentItem](toVectorMap(inventory.allEquipment.toSeq)) {

    def equipped(item: EquipmentItem): Boolean = {
      inventory.equipment.contains(item)
    }

    def toggleSelected() = {
      selectedItem foreach { item =>
        var backpack = inventory.backpack
        var equipment = inventory.equipment
        if (equipped(item)) {
          equipment -= item
          backpack += item
        } else {
          backpack -= item
          equipment += item
        }

        inventory = inventory.copy(backpack = backpack, equipment = equipment)
      }
    }
  }

  val currentItemInfo = new StaticTextViewModel("") // TODO: Implement logic

  private def toVectorMap(items: Seq[EquipmentItem]): VectorMap[EquipmentItem, String] = {
    val itemsWithNames = items.map(equipment => (equipment, equipment.name))
    VectorMap(itemsWithNames.toSeq: _*)
  }
}
