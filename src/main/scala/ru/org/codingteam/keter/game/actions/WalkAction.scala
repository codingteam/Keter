package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.ActorId
import ru.org.codingteam.keter.game.objects.equipment.WalkCapability
import ru.org.codingteam.keter.map.Move

case class WalkAction(actorId: ActorId,
                      move: Move) extends Action {

  override def capabilities = Map(
    WalkCapability -> 1
  )
}
