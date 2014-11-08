package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.equipment.Capability
import ru.org.codingteam.keter.game.objects.{ActorId, Person}

trait Action {

  def actorId: ActorId

  def capabilities = Map[Capability, Int]()

  def canAct(person: Person): Boolean = {
    capabilities.forall { case (capability, amount) =>
      person.bodyparts.count(_.capabilities.contains(capability)) >= amount
    }
  }

}
