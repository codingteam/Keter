package ru.org.codingteam.keter.scenes

import ru.org.codingteam.rotjs.interface.ROT.Text

trait IDisplay {

  def draw(x: Int, y: Int, ch: String, fg: String = null, bg: String = null): Unit

  def draw(x: Int, y: Int, ch: Array[String], fg: String, bg: String): Unit

  def drawText(x: Int, y: Int, text: String, maxWidth: Int = 0): Int

  def width: Int

  def height: Int

  def drawTextCentered(x: Int, y: Int, width: Int, height: Int, text: String, topPadding: Option[Int]): Unit = {
    val measurement = Text.measure(text, width)

    val realX = x + (width - measurement.width) / 2
    val realY = y + topPadding.getOrElse((height - measurement.height) / 2)

    drawText(realX, realY, text)
  }

  def drawTextCentered(text: String, y: Option[Int] = None): Unit = drawTextCentered(0, 0, width, height, text, y)

  def viewport(left: Int, top: Int, width: Int, height: Int, shiftX: Int = 0, shiftY: Int = 0): IDisplay =
    Viewport(this, left, top, width, height, shiftX, shiftY)

  def viewportCentered(width: Int, height: Int, y: Option[Int] = None, shiftX: Int = 0, shiftY: Int = 0): IDisplay = {
    val left = (this.width - width) / 2
    val top = y.getOrElse((this.height - height) / 2)
    viewport(left, top, width, height, shiftX, shiftY)
  }
}
