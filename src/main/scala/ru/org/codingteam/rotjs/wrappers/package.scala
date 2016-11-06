package ru.org.codingteam.rotjs

import ru.org.codingteam.keter.scenes.IDisplay
import ru.org.codingteam.rotjs.interface.ROT.Display

package object wrappers {

  implicit class RichDisplay(val self: Display) extends IDisplay {

    override def draw(x: Int, y: Int, ch: String, fg: String, bg: String): Unit = self.draw(x, y, ch, fg, bg)

    override def draw(x: Int, y: Int, ch: Array[String], fg: String, bg: String): Unit = self.draw(x, y, ch, fg, bg)

    override def height: Int = self.getOptions().height

    override def width: Int = self.getOptions().width

    override def drawText(x: Int, y: Int, text: String, maxWidth: Int): Int = self.drawText(x, y, text, maxWidth)

  }

}
