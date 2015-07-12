package ru.org.codingteam.keter.ui

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.ui.shape.Rectangle
import ru.org.codingteam.keter.ui.viewmodels.{StaticTextViewModel, MenuViewModel, TextViewModel, ItemsViewModel}
import ru.org.codingteam.keter.ui.views.{ListView, TextView, MenuView}
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.Display
import ru.org.codingteam.rotjs.wrappers._

abstract class ViewScene(display: Display) extends Scene(display) with Logging {

  def components: Vector[IView]
  private var activeComponent: Option[IView] = None

  protected def renderOnKeyDown = Application.currentScene.contains(this)

  override def render(): Unit = {
    log.debug("render")
    display.clear()
    components.foreach(_.render(display))
  }

  override def onKeyDown(event: KeyboardEvent): Unit = {
    activeComponent = activeComponent.orElse(components.headOption)
    log.debug(s"activeComponent = $activeComponent")

    activeComponent map { c =>
      log.debug(s"c = $c")
      c.onKeyDown(event)
    }

    if (renderOnKeyDown) {
      render()
    }
  }

  protected def listView[T](shape: Rectangle, model: ItemsViewModel[T]) = new ListView(shape, model)
  protected def textView(shape: Rectangle, model: TextViewModel) = new TextView(shape, model)
  protected def textView(model: TextViewModel): TextView = textView(Rectangle(0, 0, display.width, display.height), model)
  protected def textView(text: String): TextView = textView(new StaticTextViewModel(text))
  protected def menu(model: MenuViewModel) = new MenuView(model)
}
