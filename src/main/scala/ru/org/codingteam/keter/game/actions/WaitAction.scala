package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{ObjectPosition, Actor}

case object WaitAction extends IActionDefinition {

  override def process(actor: Actor, target: ObjectPosition, state: GameState): GameState = {
    state.copy(state.messages :+ s"Time passed for ${actor.name} but nothing has changed")
  }

  override def duration(actor: Actor, target: ObjectPosition, state: GameState) = 50L

}
