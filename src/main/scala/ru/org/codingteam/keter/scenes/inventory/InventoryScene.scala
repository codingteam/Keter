package ru.org.codingteam.keter.scenes.inventory

import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.scenes.mvvm.IMvvmScene
import ru.org.codingteam.keter.util.Logging

import scala.concurrent.{Future, Promise}

class InventoryScene(parentScene: Scene, inventory: Inventory) extends Scene(parentScene.display)
  with Logging with IMvvmScene[InventoryView, InventoryViewModel] {

  private val resultPromise = Promise[Option[Inventory]]()

  override val view = new InventoryView()
  override val viewModel = new InventoryViewModel(inventory)

  val result: Future[Option[Inventory]] = viewModel.result

}

object InventoryScene {

  def edit(parentScene: Scene, inventory: Inventory): Future[Option[Inventory]] = {
    val inventoryScene = new InventoryScene(parentScene, inventory)
    Application.setScene(inventoryScene)
    inventoryScene.result
  }

}