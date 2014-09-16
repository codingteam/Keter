package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.Faction
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.map.{ActorPosition, UniverseSnapshot}

case class Actor(id: ActorId,
                 faction: Faction,
                 name: String,
                 tile: String,
                 state: ActorState,
                 behavior: IActorBehavior,
                 stats: StatTable,
                 equipment: Seq[EquipmentItem],
                 position: ActorPosition) {

  def getNextAction(state: UniverseSnapshot) = behavior.getNextAction(this, state)

}
