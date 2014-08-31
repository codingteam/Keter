package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.Actor

case class WaitAction(override val actor: Actor) extends Action(actor) {

  override def process(state: GameState): Unit = {
    state.messages = state.messages :+ s"Time passed for ${actor.name} but nothing has changed"
  }

  override def duration = 50L

}
