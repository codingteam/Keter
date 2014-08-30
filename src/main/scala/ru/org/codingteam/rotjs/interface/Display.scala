package ru.org.codingteam.rotjs.interface

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("ROT.Display")
class Display extends js.Object {

  def clear(): Unit = ???
  def getContainer(): dom.Node = ???
  def getOptions(): DisplayOptions = ???
  def draw(x: Int, y: Int, ch: String, fg: String = null, bg: String = null): Unit = ???
  def draw(x: Int, y: Int, ch: Array[String], fg: String, bg: String): Unit = ???
  def drawText(x: Int, y: Int, text: String, maxWidth: Int = 0): Int = ???

}
