package ru.org.codingteam.keter.game.objects

case class Player(override val name: String) extends Actor(name, "@") {

  override def playerControllable = true

}
