package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.util.Logging

import scala.concurrent.{Promise, Future}

class InventoryScene(parentScene: Scene) extends Scene(parentScene.display) with Logging {

  private val resultPromise = Promise[Option[Inventory]]()

  val result: Future[Option[Inventory]] = resultPromise.future

  override protected def onKeyDown(event: KeyboardEvent): Unit = {
    event.keyCode match {
      case _ => Application.setScene(parentScene)
    }
  }

  override protected def render(): Unit = {
    // TODO: Render inventory.
  }

}

object InventoryScene {

  def edit(parentScene: Scene, inventory: Inventory): Future[Option[Inventory]] = {
    val inventoryScene = new InventoryScene(parentScene)
    Application.setScene(inventoryScene)
    inventoryScene.result
  }

}