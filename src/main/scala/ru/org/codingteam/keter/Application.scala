package ru.org.codingteam.keter

import org.scalajs.dom.document
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.scenes.menu.MainMenuScene
import ru.org.codingteam.rotjs.interface.{DisplayOptions, Display}

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Application extends JSApp {

  val display: Display = new Display(js.Dynamic.literal(fontSize = 20).asInstanceOf[DisplayOptions])
  var currentScene: Option[Scene] = None

  def main(): Unit = {
    document.body.appendChild(display.getContainer())
    setScene(new MainMenuScene(display))
  }

  def setScene(scene: Scene): Unit = {
    currentScene match {
      case Some(s) => s.disable()
      case None =>
    }

    scene.enable()
    currentScene = Some(scene)
  }

}