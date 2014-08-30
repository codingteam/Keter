package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.Actor

case class WaitAction() extends Action {

  override def process(actor: Actor, state: GameState): GameState = {
    state.copy(state.messages :+ "Time passed but nothing has changed")
  }

  override def duration = 50L

}
