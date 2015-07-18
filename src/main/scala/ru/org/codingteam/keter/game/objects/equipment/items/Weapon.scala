package ru.org.codingteam.keter.game.objects.equipment.items

import ru.org.codingteam.keter.game.actions.WaitAction
import ru.org.codingteam.keter.game.objects.equipment.{EquipmentCategory, ManipulatorCapability, Capability, EquipmentItem}
import ru.org.codingteam.keter.game.objects.{ActorId, ObjectAction, ObjectActionToActor, Person}
import ru.org.codingteam.keter.map.{TraverseUtils, UniverseSnapshot}

abstract class Weapon extends EquipmentItem {
  override val category = EquipmentCategory.Weapon
  override val provides: Set[Capability] = Set()
  def damage: Int
}

case class Knife(name: String,
                 damage: Int = 100) extends Weapon {

  val cooldown = 200
  val missCooldown = 100

  override val actions: Set[ObjectAction] = Set(new ObjectActionToActor {

    override def description: String = "attack"

    override def perform(invokerId: ActorId,
                         universe: UniverseSnapshot,
                         targetId: ActorId): UniverseSnapshot = Person.ifActorIsActiveF(invokerId, universe) { attacker =>
      val near = TraverseUtils.DirectionLookTraverseMethod.traverse(universe, attacker.position, -1, 1, -1, 1)
      near.find(_.actors.map(_.id).contains(targetId)) match {
        case Some(bc) =>
          universe
            .updatedActorOfType[Person](targetId)(_.decreaseHealth(damage))
            .updatedActorOfType[Person](invokerId)(_.withSheduledNextEventAfter(cooldown))
        case None => WaitAction(missCooldown).perform(invokerId, universe)
      }
    }
  })

  override val requires: Set[Capability] = Set(ManipulatorCapability)

}
