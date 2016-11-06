package ru.org.codingteam.keter.ui.views

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.ui.IView
import ru.org.codingteam.keter.ui.shape.Rectangle
import ru.org.codingteam.keter.ui.viewmodels.TextViewModel
import ru.org.codingteam.rotjs.interface.ROT.Display
import ru.org.codingteam.rotjs.wrappers._

class TextView(shape: Rectangle, model: TextViewModel) extends IView {

  override def render(display: Display): Unit = {
    display.drawTextCentered(shape.x, shape.y, shape.width, shape.height, model.text, None)
  }

  override def onKeyDown(event: KeyboardEvent): Unit = ()
}
