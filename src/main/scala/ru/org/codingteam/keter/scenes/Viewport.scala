package ru.org.codingteam.keter.scenes

case class Viewport(display: IDisplay,
                    left: Int,
                    top: Int,
                    override val width: Int,
                    override val height: Int,
                    shiftX: Int = 0,
                    shiftY: Int = 0) extends IDisplay {
  private def computeXY(x: Int, y: Int) = {
    val (dispX, dispY) = (x + shiftX, y + shiftY)
    if (dispX >= 0 && dispY >= 0 && dispX < width && dispY < height)
      Some(left + dispX, top + dispY)
    else
      None
  }

  override def draw(x: Int, y: Int, ch: String, fg: String, bg: String): Unit =
    computeXY(x, y) foreach {
      case (newX, newY) => display.draw(newX, newY, ch, fg, bg)
    }

  override def draw(x: Int, y: Int, ch: Array[String], fg: String, bg: String): Unit =
    computeXY(x, y) foreach {
      case (newX, newY) => display.draw(newX, newY, ch, fg, bg)
    }

  override def drawText(x: Int, y: Int, text: String, maxWidth: Int): Int =
    computeXY(x, y) map {
      case (newX, newY) => display.drawText(newX, newY, text, maxWidth)
    } getOrElse 0
}
