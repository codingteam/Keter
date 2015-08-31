package ru.org.codingteam.keter.scenes.inventory

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.game.objects.Inventory
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.ui.ViewScene
import ru.org.codingteam.keter.ui.shape.Rectangle
import ru.org.codingteam.keter.ui.views.ListView
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.{Display, ROT}

import scala.concurrent.Future

class InventoryScene(parentScene: Scene, inventory: Inventory)
  extends ViewScene(parentScene.display) with Logging {

  val viewModel = new InventoryViewModel(inventory)
  val result: Future[Option[Inventory]] = viewModel.result

  override val components = Vector(
    listView(Rectangle(0, 0, 40, 40), backpackCategories),
    folderView(Rectangle(40, 0, 40, 40)),
    textView(Rectangle(0, 40, 80, 40), currentItemInfo))

  override def onKeyDown(event: KeyboardEvent): Unit = {
    super.onKeyDown(event)

    if (event.keyCode == ROT.VK_A) {
      viewModel.applyChanges()
    } else if (event.keyCode == ROT.VK_ESCAPE) {
      viewModel.rejectChanges()
    }
  }

  private def backpackCategories = viewModel.backpackCategories
  private def currentFolderItems = viewModel.currentFolderItems
  private def currentItemInfo = viewModel.currentItemInfo

  private def folderView(shape: Rectangle) = {
    val model = currentFolderItems
    new ListView(shape, model, ListView.arrowKeyMap(model)) {

      override protected def renderItem(display: Display, item: Item, x: Int, y: Int, width: Int): Unit = {
        val (value, name) = item
        val newName = if (model.equipped(value)) {
          // Render equipped items in gray color.
          s"%c{gray}$name%c{}"
        } else {
          name
        }

        super.renderItem(display, (value, newName), x, y, width)
      }
    }
  }
}

object InventoryScene {

  def edit(parentScene: Scene, inventory: Inventory): Future[Option[Inventory]] = {
    val inventoryScene = new InventoryScene(parentScene, inventory)
    Application.setScene(inventoryScene)
    inventoryScene.viewModel.result
  }
}
