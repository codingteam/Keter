package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.actions.{WaitAction, MeleeAttackAction, WalkAction, Action}
import ru.org.codingteam.keter.game.objects.equipment.{MeleeAttackCapability, WalkCapability, EquipmentItem}
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}

case class Actor(id: ActorId,
                 faction: Faction,
                 name: String,
                 tile: String,
                 state: ActorState,
                 behavior: IActorBehavior,
                 stats: StatTable,
                 equipment: Seq[EquipmentItem],
                 position: ActorPosition,
                 bodyparts: Set[Bodypart]) {

  def getNextAction(state: UniverseSnapshot) = behavior.getNextAction(this, state)

  def can(act: Action) = {
    act match {
      case act:WalkAction => {
        !bodyparts.forall(bodypart => !bodypart.actions.contains(WalkCapability))
      }
      case act:MeleeAttackAction => {
        !bodyparts.forall(bodypart => !bodypart.actions.contains(MeleeAttackCapability))
      }
      case act:WaitAction => {
        true // no requirements for waiting
      }
      case _ => false // don't change to true -- it will force programmers to declare future actions here
    }
  }

}
