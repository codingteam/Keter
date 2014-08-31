package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.Actor

abstract class Action(val actor: Actor) {

  def process(state: GameState): Unit
  def duration: Long

}
