package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.equipment.MeleeAttackCapability
import ru.org.codingteam.keter.game.objects.{Actor, ActorId}
import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

case class MeleeAttackAction(actorId: ActorId,
                             target: ObjectPosition,
                             damage: Int = 10) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    val targets = state.findActorsOfType[Actor](target)
    targets.headOption match {
      case Some(t) => state.updatedActorOfType[Actor](t.id)(a => a.copy(stats = a.stats.copy(a.stats.health - damage)))
      case None =>
        state.findActor(actorId) foreach {
          actor => engine.addMessage(s"$actor tries to attack the $target but there is nothing to attack")
        }
        state
    }
  }

  override def capabilities = Map(
    MeleeAttackCapability -> 1
  )
}
