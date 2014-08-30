package ru.org.codingteam.rotjswrapper

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("ROT.Display")
class Display extends js.Object {

  def clear(): Unit = ???
  def getContainer(): dom.Node = ???
  def drawText(x: Int, y: Int, text: String, maxWidth: Int = 0): Unit = ???

}
