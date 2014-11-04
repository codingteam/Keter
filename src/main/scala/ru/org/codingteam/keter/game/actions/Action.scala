package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.equipment.Capability
import ru.org.codingteam.keter.game.objects.{Actor, ActorId}

trait Action {

  def actorId: ActorId

  def capabilities = Map[Capability, Int]()

  def canAct(actor: Actor): Boolean = {
    capabilities.forall { case (capability, amount) =>
      actor.bodyparts.count(_.capabilities.contains(capability)) >= amount
    }
  }

}
