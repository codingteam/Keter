package ru.org.codingteam.rotjs.wrapper

import ru.org.codingteam.rotjs.interface.{Text, Display}

class RichDisplay(val self: Display) extends AnyVal {

  def height: Int = self.getOptions().height
  def width: Int = self.getOptions().width

  def drawTextCentered(text: String, y: Option[Int] = None): Unit = {
    val displayWidth = width
    val measurement = Text.measure(text, displayWidth)

    val realX = (width - measurement.width) / 2
    val realY = y.getOrElse((height - measurement.height) / 2)

    self.drawText(realX, realY, text)
  }

}
