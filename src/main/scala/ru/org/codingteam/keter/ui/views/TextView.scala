package ru.org.codingteam.keter.ui.views

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.ui.IView
import ru.org.codingteam.keter.ui.viewmodels.TextViewModel
import ru.org.codingteam.rotjs.interface.Display
import ru.org.codingteam.rotjs.wrappers._

class TextView(var width: Int, var height: Int, model: TextViewModel) extends IView {

  var x: Int = 0
  var y: Int = 0

  override def render(display: Display): Unit = {
    display.drawTextCentered(x, y, width, height, model.text, None)
  }

  override def onKeyDown(event: KeyboardEvent): Unit = ()

}
