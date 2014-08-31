package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.objects.ai.RandomBot

case class NPC(override val name: String, override val tile: String) extends Actor(name, tile) with RandomBot {

  override val playerControllable = false

}
