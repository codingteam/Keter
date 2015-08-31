package ru.org.codingteam.keter.ui.viewmodels

import ru.org.codingteam.keter.util.VectorMap

class ItemsViewModel[T](initialItems: VectorMap[T, String]) {

  private var _items: VectorMap[T, String] = initialItems
  private var _selectedIndex: Option[Int] = initialIndex()

  def items = _items
  def items_=(newItems: VectorMap[T, String]): Unit = {
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

    onSelectedItemChanged(selectedItem)
  }

  def selectedItem = _selectedIndex map { index => _items(index)._1 }

  def up(): Unit = {
    _selectedIndex foreach { value => selectedIndex = Some(value - 1) }
  }

  def down(): Unit = {
    _selectedIndex foreach { value => selectedIndex = Some(value + 1) }
  }

  protected def onSelectedItemChanged(item: Option[T]) = ()

  private def initialIndex(): Option[Int] = _items.vector.headOption.map(_ => 0)
}
