package ru.org.codingteam.keter.ui.viewmodels

import ru.org.codingteam.keter.util.Logging

abstract class MenuViewModel extends Logging {

  val items: Seq[(String, () => Unit)]
  var selectedItem: Int = 0

  def up(): Unit = change(-1)
  def down(): Unit = change(1)
  def execute(): Unit = items(selectedItem)._2()

  private def change(delta: Int): Unit = {
    log.debug(s"delta = $delta")
    log.debug(s"selectedItem = $selectedItem")
    log.debug(s"items = $items")

    selectedItem = selectedItem + delta match {
      case x if x < 0 => items.length - 1
      case x if x >= items.length => 0
      case other => other
    }

    log.debug(s"selectedItem became $selectedItem")
  }

}
