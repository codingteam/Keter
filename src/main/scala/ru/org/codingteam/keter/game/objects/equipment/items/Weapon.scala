package ru.org.codingteam.keter.game.objects.equipment.items

import ru.org.codingteam.keter.game.actions.WaitAction
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.{ActorId, ObjectAction, ObjectActionToActor, Person}
import ru.org.codingteam.keter.map.TraverseUtils.BoardCoords
import ru.org.codingteam.keter.map.{TraverseUtils, UniverseSnapshot}

abstract class Weapon extends EquipmentItem {
  def damage: Int
}

case class Knife(name: String,
                 damage: Int = 100) extends Weapon {

  override val actions: Set[ObjectAction] = Set(new ObjectActionToActor {

    override def description: String = "attack"

    override def perform(invokerId: ActorId,
                         universe: UniverseSnapshot,
                         targetId: ActorId): UniverseSnapshot = Person.ifActorIsActiveF(invokerId, universe) { attacker =>
      val near = TraverseUtils.DirectionLookTraverseMethod.traverse(universe, attacker.position, -1, 1, -1, 1)
      near.find(_.actors.map(_.id).contains(targetId)) match {
        case Some(bc) =>
          val dir = BoardCoords(0, 0) to bc.coords
          universe
            .updatedActorOfType[Person](targetId)(_.decreaseHealth((damage / dir.length).toInt))
            .updatedActorOfType[Person](invokerId)(_.withSheduledNextEventAfter(dir.length * 200))
        case None => WaitAction(100).perform(invokerId, universe)
      }
    }
  })
}
