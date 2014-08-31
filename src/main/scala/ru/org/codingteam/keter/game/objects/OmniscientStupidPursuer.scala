package ru.org.codingteam.keter.game.objects

case class OmniscientStupidPursuer(override val name: String, override val tile: String) extends StupidMover(name, tile) {
  override val playerControllable = false
  override val to = 1
}
