package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.Actor

class MeeleeAttackAction(actor: Actor, target: Actor) extends Action(actor) {

  val damage = 10

  override def process(state: GameState): GameState = {
    val map = state.map
    if (target.position - actor.position >= 2.0) {
      state.copy(state.messages :+ s"$actor misses the $target")
    } else {
      val stats = target.stats
      val newTarget = target.copy(stats = stats.copy(health = stats.health - damage))
      state.copy(map = map.copy(actors = map.actors + (newTarget.id -> newTarget)))
    }
  }

  override def duration: Long = 500

}
