package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.equipment.MeleeAttackCapability
import ru.org.codingteam.keter.game.objects.equipment.items.Weapon
import ru.org.codingteam.keter.game.objects.{Actor, ActorId}
import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

case class MeleeAttackAction(actorId: ActorId,
                             target: ObjectPosition,
                             damage: Int = 10) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    state.findActorOfType[Actor](actorId) match {
      case Some(actor) =>
        val targets = state.findActorsOfType[Actor](target)
        targets.headOption match {
          case Some(targetActor) =>
            actor.equipment.find(_.actions.contains(MeleeAttackCapability)) match {
              case Some(weapon: Weapon) =>
                engine.addMessage(s"$actor attacks the $target with $weapon (damage=${weapon.damage})")
                state.updatedActorOfType[Actor](targetActor.id)(_.decreaseHealth(weapon.damage))
              case _ =>
                engine.addMessage(s"$actor tries to attack the $target but $actor doesn't have weapons")
                state
            }
          case None =>
            state.findActor(actorId) foreach {
              actor => engine.addMessage(s"$actor tries to attack the $target but there is nothing to attack")
            }
            state
        }
      case None => state
    }
  }

  override def capabilities = Map(
    MeleeAttackCapability -> 1
  )
}
