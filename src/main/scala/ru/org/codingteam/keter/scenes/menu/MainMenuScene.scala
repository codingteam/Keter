package ru.org.codingteam.keter.scenes.menu

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.rotjswrapper.{Display, ROT}

class MainMenuScene(display: Display) extends Scene(display) {

  val menuItems = Vector(
    "New Game" -> newGame _,
    "Load Game" -> notImplemented _
  )

  var selectedItem = 0

  override def onKeyDown(event: KeyboardEvent): Unit = {
    event.keyCode match {
      case c if c == ROT.VK_UP =>
        selectedItem -= 1
      case c if c == ROT.VK_DOWN =>
        selectedItem += 1
      case c if c == ROT.VK_RETURN =>
        val (_, action) = menuItems(selectedItem)
        action()
      case _ =>
    }

    selectedItem = selectedItem match {
      case x if x < 0 => menuItems.length - 1
      case x if x >= menuItems.length => 0
      case x => x
    }
  }


  override def render(): Unit = {
    display.clear()
    menuItems.zipWithIndex.foreach { case ((name, action), index) =>
      val text = if (index == selectedItem) {
        s"%b{#fff}%c{#000}$name%c{}%b{}"
      } else {
        name
      }

      display.drawText(0, index, text)
    }
  }

  private def newGame(): Unit = {
    notImplemented()
  }

  private def notImplemented(): Unit = {
    Application.setScene(new NotImplementedScene(display, this))
  }

}
