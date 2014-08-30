package ru.org.codingteam.keter

import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.scenes.menu.MainMenuScene
import ru.org.codingteam.rotjswrapper.Display

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document

object Application extends JSApp {

  val display: Display = new Display()
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