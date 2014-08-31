package ru.org.codingteam.keter.game.objects.ai

trait OmniscientStupidPursuer extends StupidMover {
  override val playerControllable = false
  override val to = 1
}
