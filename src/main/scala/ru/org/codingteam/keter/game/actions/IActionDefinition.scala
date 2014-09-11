package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{ObjectPosition, Actor}

trait IActionDefinition {

  def process(actor: Actor, target: ObjectPosition, state: GameState): GameState
  def duration(actor: Actor, target: ObjectPosition, state: GameState): Long

}
