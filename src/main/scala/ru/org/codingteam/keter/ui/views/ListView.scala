package ru.org.codingteam.keter.ui.views

import ru.org.codingteam.keter.ui.SimpleKeyMapView
import ru.org.codingteam.keter.ui.shape.Rectangle
import ru.org.codingteam.keter.ui.viewmodels.ItemsViewModel
import ru.org.codingteam.rotjs.interface.{ROT, Display}

/**
 * A list of typed items.
 */
class ListView[T](shape: Rectangle, model: ItemsViewModel[T]) extends SimpleKeyMapView {

  override def render(display: Display): Unit = {
    val margin = 1
    model.items.vector.zipWithIndex foreach { case ((value, name), index) =>
      val y = shape.y + index
      if (index > shape.height) {
        return
      }

      if (model.selectedIndex.contains(index)) {
        display.draw(shape.x, y, ">")
      }

      display.drawText(shape.x + margin, y, name, shape.width - 2 * margin)
    }
  }

  override val keyMap = Map(
    ROT.VK_OPEN_BRACKET -> model.up _,
    ROT.VK_CLOSE_BRACKET -> model.down _
  )
}
