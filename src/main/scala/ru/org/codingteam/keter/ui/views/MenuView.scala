package ru.org.codingteam.keter.ui.views

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.ui.IView
import ru.org.codingteam.keter.ui.viewmodels.MenuViewModel
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrappers._


class MenuView(viewModel: MenuViewModel) extends IView with Logging {

  private val keyMap = Map[Int, () => Unit](
    ROT.VK_UP -> viewModel.up _,
    ROT.VK_DOWN -> viewModel.down _,
    ROT.VK_RETURN -> viewModel.execute _
  )

  override def onKeyDown(event: KeyboardEvent): Unit = {
    val action = keyMap.get(event.keyCode)
    log.info(s"action = $action")
    action map(_())
  }

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
