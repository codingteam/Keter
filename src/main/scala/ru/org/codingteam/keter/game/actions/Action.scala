package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.Actor

abstract class Action {

  def process(actor: Actor, state: GameState): GameState

}
