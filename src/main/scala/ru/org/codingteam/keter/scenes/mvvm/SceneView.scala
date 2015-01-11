package ru.org.codingteam.keter.scenes.mvvm

import org.scalajs.dom.KeyboardEvent

abstract class SceneView[ViewModel] extends IView[ViewModel] {

  override def render(): Unit = ??? // TODO: Render all components
  override def onKeyDown(event: KeyboardEvent): Unit = ??? // TODO: General key handling (process focus change and pass the event to the active component)

  protected def listView(x: Int, y: Int, width: Int, height: Int, model: ItemsViewModel) = ???
  protected def textView(x: Int, y: Int, width: Int, height: Int, model: TextViewModel) = ???

}
