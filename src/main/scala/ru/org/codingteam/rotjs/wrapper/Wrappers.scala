package ru.org.codingteam.rotjs.wrapper

import ru.org.codingteam.rotjs.interface.Display

object Wrappers {

  implicit def richDisplay(display: Display) = new RichDisplay(display)

}
