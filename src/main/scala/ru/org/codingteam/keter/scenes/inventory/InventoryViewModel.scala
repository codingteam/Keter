package ru.org.codingteam.keter.scenes.inventory

import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.game.objects.equipment.{EquipmentCategory, EquipmentItem}
import ru.org.codingteam.keter.ui.viewmodels.{ItemsViewModel, StaticTextViewModel}

import scala.collection.immutable.ListMap
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

  def backpackCategories = new ItemsViewModel(
    ListMap(EquipmentCategory.All -> "ALL", EquipmentCategory.Weapon -> "Weapons")
  )
  def currentFolderItems = new ItemsViewModel[EquipmentItem](ListMap()) // TODO: Implement change logic
  def currentItemInfo = new StaticTextViewModel("") // TODO: Implement logic
}
