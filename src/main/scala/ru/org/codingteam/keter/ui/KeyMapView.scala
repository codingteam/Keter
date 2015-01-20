package ru.org.codingteam.keter.ui

import org.scalajs.dom.KeyboardEvent

trait KeyMapView[Event] extends IView {

  def keyMap: Map[Event, () => Unit]
  def mapEvent(event: KeyboardEvent): Event

  override def onKeyDown(event: KeyboardEvent): Unit = {
    val key = mapEvent(event)
    val action = keyMap.get(key)
    action map(_())
  }

}
