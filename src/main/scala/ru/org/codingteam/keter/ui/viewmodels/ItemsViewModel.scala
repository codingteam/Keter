package ru.org.codingteam.keter.ui.viewmodels

import scala.collection.immutable.ListMap

class ItemsViewModel[T](initialItems: ListMap[T, String]) {

  private var _items: ListMap[T, String] = initialItems
  private var _selectedIndex: Option[Int] = initialIndex()

  def items = _items
  def items_=(newItems: ListMap[T, String]): Unit = {
    _items = newItems
    _selectedIndex = initialIndex()
  }

  def selectedIndex = _selectedIndex
  def selectedIndex_=(newIndex: Option[Int]): Unit = {
    newIndex match {
      case None =>
        _selectedIndex = newIndex
      case Some(index) if _items.isEmpty => // Nothing
      case Some(index) if index < 0 =>
        _selectedIndex = Some(_items.size - 1)
      case Some(index) if index >= _items.size =>
        _selectedIndex = Some(0)
      case Some(index) =>
        _selectedIndex = newIndex
    }
  }

  def up(): Unit = {
    _selectedIndex foreach { value => selectedIndex = Some(value - 1) }
  }

  def down(): Unit = {
    _selectedIndex foreach { value => selectedIndex = Some(value + 1) }
  }

  private def initialIndex(): Option[Int] = _items.headOption.map(_ => 0)
}
