package ru.org.codingteam.keter.scenes.inventory

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.scenes.mvvm.SceneView
import ru.org.codingteam.rotjs.interface.ROT

class InventoryView() extends SceneView[InventoryViewModel] {

  val backpack = listView(0, 0, 40, 40, backpackItems)
  val currentFolder = listView(40, 0, 40, 40, currentFolderItems)
  val itemInfo = textView(0, 40, 80, 40, currentItemInfo)

  var viewModel: Option[InventoryViewModel] = None
  override def bind(viewModel: InventoryViewModel): Unit = {
    this.viewModel = Some(viewModel)
  }

  override def onKeyDown(event: KeyboardEvent): Unit = {
    super.onKeyDown(event)
    val Some(model) = viewModel

    if (event.keyCode == ROT.VK_A) {
      model.applyChanges()
    } else if (event.keyCode == ROT.VK_ESCAPE) {
      model.rejectChanges()
    }
  }

  private def backpackItems = viewModel.get.backpackItems
  private def currentFolderItems = viewModel.get.currentFolderItems
  private def currentItemInfo = viewModel.get.currentItemInfo

}
