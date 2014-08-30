package ru.org.codingteam.rotjs.interface

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("ROT.Text")
object Text extends js.Object {

  def measure(str: String, maxWidth: Int = 0): TextBlockSize = ???

}
