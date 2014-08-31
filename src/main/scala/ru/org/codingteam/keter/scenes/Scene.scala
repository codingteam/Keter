package ru.org.codingteam.keter.scenes

import org.scalajs.dom.{KeyboardEvent, document}
import ru.org.codingteam.rotjs.interface.Display

abstract class Scene(val display: Display) {

  val keyDownListener = (event: KeyboardEvent) => {
    onKeyDown(event)
  }
  
  def enable(): Unit = {
    document.onkeydown = keyDownListener
    render()
  }

  def disable(): Unit = {
    document.onkeydown = null
  }
  
  protected def onKeyDown(event: KeyboardEvent): Unit
  protected def render(): Unit

}
