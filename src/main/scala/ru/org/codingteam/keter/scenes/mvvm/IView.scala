package ru.org.codingteam.keter.scenes.mvvm

import org.scalajs.dom.KeyboardEvent

trait IView[ViewModel] {

  def bind(viewModel: ViewModel): Unit
  def render(): Unit
  def onKeyDown(event: KeyboardEvent): Unit

}
