package ru.org.codingteam.keter.ui

import org.scalajs.dom.KeyboardEvent

trait SimpleKeyMapView extends KeyMapView[Int] {

  def mapEvent(event: KeyboardEvent) = event.keyCode

}
