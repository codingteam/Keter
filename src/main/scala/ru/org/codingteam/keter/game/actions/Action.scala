package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{Actor, ObjectPosition}

case class Action(actor: Actor, definition: IActionDefinition, target: ObjectPosition) {

  def duration(state: GameState) = definition.duration(actor, target, state)
  def process(state: GameState) = definition.process(actor, target, state)

}
