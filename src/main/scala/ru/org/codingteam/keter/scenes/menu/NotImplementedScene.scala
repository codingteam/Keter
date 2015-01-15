package ru.org.codingteam.keter.scenes.menu

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.ui.{IView, ViewScene}
import ru.org.codingteam.rotjs.interface.{Display, ROT}

class NotImplementedScene(display: Display, parentScene: Scene) extends ViewScene(display) {

  override def components: Vector[IView] =
    Vector(textView("This function is not implemented. Press ENTER to continue."))

  override def onKeyDown(event: KeyboardEvent): Unit = {
    event.keyCode match {
      case c if c == ROT.VK_RETURN => Application.setScene(parentScene)
      case _ =>
    }
  }

}
