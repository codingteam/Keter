package ru.org.codingteam.keter.game.objects.equipment.bodyparts

import ru.org.codingteam.keter.game.objects.equipment.{MeleeAttackCapability, WalkCapability, Capability}

abstract class Bodypart() {
  def name: String
  def health: Double
  def capabilities: Set[Capability]
}


case class Leg(name:String,
               health: Double,
               capabilities: Set[Capability] = Set(WalkCapability)) extends Bodypart

case class Arm(name:String,
               health: Double,
               capabilities: Set[Capability] = Set(MeleeAttackCapability)) extends Bodypart

case class Head(name:String,
               health: Double,
               capabilities: Set[Capability] = Set()) extends Bodypart

case class Torso(name:String,
               health: Double,
               capabilities: Set[Capability] = Set()) extends Bodypart