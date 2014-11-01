package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.game.objects.equipment.MeleeAttackCapability
import ru.org.codingteam.keter.game.objects.equipment.items.Weapon
import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

case class MeleeAttackAction(actor: Actor,
                             target: ObjectPosition,
                             damage: Double = 10) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    if (state.actors.exists(_.position.objectPosition == target)) {
      if (actor.equipment.exists(_.actions.exists(_ == MeleeAttackCapability))) {
        val newDamage = actor.equipment.find(_.actions.exists(_ == MeleeAttackCapability) == true).get.asInstanceOf[Weapon].damage // assume this item is weapon
        state.copy(actors = state.actors.map { a =>
          if (a.id != actor.id && a.position.objectPosition == target)
            a.copy(stats = a.stats.copy(health = math.round(a.stats.health - newDamage).asInstanceOf[Int]))
          else
            a
        })
      } else {
        engine.addMessage(s"$actor tries to attack the $target but $actor doesn't have weapons")
        state
      }
    } else {
      engine.addMessage(s"$actor tries to attack the $target but there is nothing to attack")
      state
    }
  }

  override def duration(state: UniverseSnapshot): Long = 80L

  override def capabilities = Map(
    MeleeAttackCapability -> 1
  )

}