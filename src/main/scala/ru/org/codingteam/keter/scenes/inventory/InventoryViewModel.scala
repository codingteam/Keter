package ru.org.codingteam.keter.scenes.inventory

import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.ui.viewmodels.{ItemsViewModel, TextViewModel}

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

  def backpackItems = new ItemsViewModel() // TODO: Implement source
  def currentFolderItems = new ItemsViewModel() // TODO: Implement change logic
  def currentItemInfo = new TextViewModel() // TODO: Implement logic

}
