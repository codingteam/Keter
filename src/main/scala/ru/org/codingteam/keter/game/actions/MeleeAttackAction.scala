package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{ObjectPosition, Actor}

case object MeleeAttackAction extends IActionDefinition {

  val damage = 10

  override def process(actor: Actor, target: ObjectPosition, state: GameState): GameState = {
    val map = state.map
    state.map.actorAt(target) match {
      case Some(targetActor) =>
        val stats = targetActor.stats
        val newTarget = targetActor.copy(stats = stats.copy(health = stats.health - damage))
        state.copy(map = map.copy(actors = map.actors + (newTarget.id -> newTarget)))
      case None =>
        state.copy(state.messages :+ s"$actor tries to attack the $target but there is nothing to attack")
    }
  }

  override def duration(actor: Actor, target: ObjectPosition, state: GameState): Long = 80L

}
