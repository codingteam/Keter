package ru.org.codingteam.rotjs.interface

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("ROT.EventQueue")
class EventQueue extends js.Object {

  def add(event: Any, time: Double): Unit = ???
  def clear(): Unit = ???
  def get(): Any = ???
  def getTime(): Double = ???
  def remove(event: Any): Boolean = ???

}
