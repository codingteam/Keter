package ru.org.codingteam.keter.scenes.mvvm

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.scenes.Scene

trait IMvvmScene[View <: IView[ViewModel], ViewModel] extends Scene {

  protected def view: View
  protected def viewModel: ViewModel

  view.bind(viewModel)

  override final protected def render(): Unit = {
    view.render()
  }

  override final protected def onKeyDown(event: KeyboardEvent): Unit = {
    view.onKeyDown(event)
  }

}
