package ru.org.codingteam.keter.game.objects.equipment.items

import ru.org.codingteam.keter.game.objects.equipment.{EquipmentItem, MeleeAttackCapability, Capability}

abstract class Weapon extends EquipmentItem {
  def damage: Double
}

case class Knife(name:String,
                 actions: Set[Capability] = Set(MeleeAttackCapability),
                 damage: Double = 100.0) extends Weapon