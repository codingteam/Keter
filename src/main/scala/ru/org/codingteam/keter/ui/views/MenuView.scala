package ru.org.codingteam.keter.ui.views

import ru.org.codingteam.keter.ui.KeyMapView
import ru.org.codingteam.keter.ui.viewmodels.MenuViewModel
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrappers._

class MenuView(viewModel: MenuViewModel) extends KeyMapView with Logging {

  val keyMap = Map(
    ROT.VK_UP -> viewModel.up _,
    ROT.VK_DOWN -> viewModel.down _,
    ROT.VK_RETURN -> viewModel.execute _
  )

  override def render(display: Display): Unit = {
    display.drawTextCentered("Keter", Some(1))
    display.drawTextCentered("=====", Some(2))

    val base = 4
    viewModel.items.zipWithIndex.foreach { case ((name, action), index) =>
      log.info(s"Drawing with selectedItem = ${viewModel.selectedItem}")
      val (indent, text) = if (index == viewModel.selectedItem) {
        (2, s"> %b{#fff}%c{#000}$name%c{}%b{}")
      } else {
        (4, name)
      }

      display.drawText(indent, base + index, text)
    }
  }

}
