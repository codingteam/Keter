package ru.org.codingteam.keter.scenes

import org.scalajs.dom.{KeyboardEvent, document}
import ru.org.codingteam.rotjs.interface.Display

abstract class Scene(val display: Display) {

  val keyDownListener = (event: KeyboardEvent) => {
    onKeyDown(event)

    if (enabled) {
      render()
    }
  }

  var enabled = false
  
  def enable(): Unit = {
    document.onkeydown = keyDownListener
    enabled = true
    render()
  }

  def disable(): Unit = {
    document.onkeydown = null
    enabled = false
  }
  
  protected def onKeyDown(event: KeyboardEvent): Unit
  protected def render(): Unit

}
