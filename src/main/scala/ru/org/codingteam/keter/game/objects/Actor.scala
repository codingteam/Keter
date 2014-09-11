package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.{Faction, GameState}

case class Actor(id: ActorId,
                 faction: Faction,
                 name: String,
                 tile: String,
                 state: ActorState,
                 behavior: IActorBehavior,
                 stats: StatTable,
                 equipment: Seq[EquipmentItem],
                 position: ObjectPosition) extends GameObject {

  def getNextAction(state: GameState) = behavior.getNextAction(this, state)

}
