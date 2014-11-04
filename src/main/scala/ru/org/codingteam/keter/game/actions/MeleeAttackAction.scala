package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.ActorId
import ru.org.codingteam.keter.game.objects.equipment.MeleeAttackCapability
import ru.org.codingteam.keter.map.ObjectPosition

case class MeleeAttackAction(actorId: ActorId,
                             target: ObjectPosition,
                             damage: Int = 10) extends Action {

  override def capabilities = Map(
    MeleeAttackCapability -> 1
  )
}
