package ru.org.codingteam.keter.scenes.menu

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrappers._

class NotImplementedScene(display: Display, parentScene: Scene) extends Scene(display) {

  override def onKeyDown(event: KeyboardEvent): Unit = {
    event.keyCode match {
      case c if c == ROT.VK_RETURN => Application.setScene(parentScene)
      case _ =>
    }
  }

  override def render(): Unit = {
    display.clear()
    display.drawTextCentered("This function is not implemented. Press ENTER to continue.")
  }

}
