package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.game.objects.equipment.Capability
import ru.org.codingteam.keter.map.UniverseSnapshot

trait Action {

  def actor: Actor

  def duration(state: UniverseSnapshot): Double

  def process(state: UniverseSnapshot, engine: IEngine): UniverseSnapshot

  def capabilities = Map[Capability, Int]()

  def canAct(actor: Actor): Boolean = {
    capabilities.forall { case (capability, amount) =>
      actor.bodyparts.count(_.capabilities.contains(capability)) >= amount
    }
  }

}
