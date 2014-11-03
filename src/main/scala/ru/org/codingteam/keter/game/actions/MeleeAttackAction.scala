package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.game.objects.equipment.MeleeAttackCapability
import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

case class MeleeAttackAction(actor: Actor,
                             target: ObjectPosition,
                             damage: Int = 10) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    val targets = state.findActors(target).values.flatMap {
      case a: Actor => Some(a)
      case _ => None
    }
    targets.headOption match {
      case Some(t) => state.updatedActor(t.id) {
        case a: Actor => a.copy(stats = a.stats.copy(a.stats.health - damage))
        case a => a
      }
      case None =>
        engine.addMessage(s"$actor tries to attack the $target but there is nothing to attack")
        state
    }
  }

  override def duration(state: UniverseSnapshot) = 80

  override def capabilities = Map(
    MeleeAttackCapability -> 1
  )

}
