package ru.org.codingteam.keter.ui.views

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.ui.IView
import ru.org.codingteam.keter.ui.shape.Rectangle
import ru.org.codingteam.keter.ui.viewmodels.ItemsViewModel
import ru.org.codingteam.rotjs.interface.Display

/**
 * A list of typed items.
 */
class ListView[T](shape: Rectangle, model: ItemsViewModel[T]) extends IView {

  override def render(display: Display): Unit = ???

  override def onKeyDown(event: KeyboardEvent): Unit = ???
}
