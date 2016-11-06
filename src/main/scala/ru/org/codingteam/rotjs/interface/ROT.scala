package ru.org.codingteam.rotjs.interface

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.annotation.JSImport.Namespace

@JSImport("rot-js", Namespace)
object ROT extends js.Object {

  def VK_A: Int = ???
  def VK_CLOSE_BRACKET: Int = ???
  def VK_DOWN: Int = ???
  def VK_ENTER: Int = ???
  def VK_ESCAPE: Int = ???
  def VK_I: Int = ???
  def VK_LEFT: Int = ???
  def VK_NUMPAD0: Int = ???
  def VK_NUMPAD1: Int = ???
  def VK_NUMPAD2: Int = ???
  def VK_NUMPAD3: Int = ???
  def VK_NUMPAD4: Int = ???
  def VK_NUMPAD5: Int = ???
  def VK_NUMPAD6: Int = ???
  def VK_NUMPAD7: Int = ???
  def VK_NUMPAD8: Int = ???
  def VK_NUMPAD9: Int = ???
  def VK_OPEN_BRACKET: Int = ???
  def VK_RETURN: Int = ???
  def VK_RIGHT: Int = ???
  def VK_UP: Int = ???

  class Display(options: DisplayOptions = ???) extends js.Object {

    def clear(): Unit = ???
    def getContainer(): dom.Node = ???
    def getOptions(): DisplayOptions = ???
    def draw(x: Int, y: Int, ch: String, fg: String = null, bg: String = null): Unit = ???
    def draw(x: Int, y: Int, ch: Array[String], fg: String, bg: String): Unit = ???
    def drawText(x: Int, y: Int, text: String, maxWidth: Int = 0): Int = ???
  }

  object Text extends js.Object {

    def measure(str: String, maxWidth: Int = 0): TextBlockSize = ???
  }
}
