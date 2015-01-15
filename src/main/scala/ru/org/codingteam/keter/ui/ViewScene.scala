package ru.org.codingteam.keter.ui

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.ui.viewmodels.{StaticTextViewModel, MenuViewModel, TextViewModel, ItemsViewModel}
import ru.org.codingteam.keter.ui.views.{TextView, MenuView}
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.Display
import ru.org.codingteam.rotjs.wrappers._

abstract class ViewScene[ViewModel](display: Display) extends Scene(display) with Logging {

  def components: Vector[IView]
  private var activeComponent: Option[IView] = None

  override def render(): Unit = {
    display.clear()
    components.foreach(_.render(display))
  }

  override def onKeyDown(event: KeyboardEvent): Unit = {
    activeComponent = activeComponent.orElse(components.headOption)
    log.info(s"activeComponent = $activeComponent")

    activeComponent map { c =>
      log.info(s"c = $c")
      c.onKeyDown(event)
    }

    if (Application.currentScene == Some(this)) {
      render()
    }
  }

  protected def listView(x: Int, y: Int, width: Int, height: Int, model: ItemsViewModel) = ???
  protected def textView(x: Int, y: Int, width: Int, height: Int, model: TextViewModel): TextView = {
    val view = new TextView(width, height, model)
    view.x = x
    view.y = y
    view
  }
  protected def textView(model: TextViewModel): TextView = textView(0, 0, display.width, display.height, model)
  protected def textView(text: String): TextView = textView(new StaticTextViewModel(text))
  protected def menu(model: MenuViewModel) = new MenuView(model)

}
