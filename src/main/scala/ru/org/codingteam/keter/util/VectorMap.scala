package ru.org.codingteam.keter.util

import scala.collection.immutable.ListMap

class VectorMap[Key, Value] private (items: (Key, Value)*) {

  val map = ListMap(items: _*)
  val vector = Vector(items: _*)

  def apply(index: Int) = vector(index)
  def apply(key: Key) = map(key)
  def isEmpty = map.isEmpty
  def size = map.size
}

object VectorMap {

  def apply[Key, Value](items: (Key, Value)*) = new VectorMap(items: _*)
}
