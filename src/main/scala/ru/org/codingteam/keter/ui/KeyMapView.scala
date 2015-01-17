package ru.org.codingteam.keter.ui

import org.scalajs.dom.KeyboardEvent

trait KeyMapView extends IView {

  def keyMap: Map[Int, () => Unit]

  override def onKeyDown(event: KeyboardEvent): Unit = {
    val key = event.keyCode
    val action = keyMap.get(key)
    action map(_())
  }

}
