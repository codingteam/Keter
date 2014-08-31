package ru.org.codingteam.keter.game.objects

abstract class Actor(override val name: String, override val tile: String) extends GameObject {

  def playerControllable: Boolean
  val enabled = true

}
