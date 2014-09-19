package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

case class MeleeAttackAction(actor: Actor,
                             target: ObjectPosition,
                             damage: Int = 10) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    if (state.actors.exists(_.position.objectPosition == target)) {
      state.copy(actors = state.actors.map { a =>
        if (a.id != actor.id && a.position.objectPosition == target)
          a.copy(stats = a.stats.copy(health = a.stats.health - damage))
        else
          a
      }).updatedTimestamp(_ + duration(state))
    } else {
      engine.addMessage(s"$actor tries to attack the $target but there is nothing to attack")
      state
    }
  }

  override def duration(state: UniverseSnapshot): Long = 80L

}
