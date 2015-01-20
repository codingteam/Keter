package ru.org.codingteam.keter.ui

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.rotjs.interface.Display

trait IView {

  def render(display: Display): Unit
  def onKeyDown(event: KeyboardEvent): Unit

}
