package ru.org.codingteam.keter

import ru.org.codingteam.rotjswrapper.Display

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document

object Application extends JSApp {
  val display: Display = new Display()

  def main(): Unit = {
    document.body.appendChild(display.getContainer())
  }
}