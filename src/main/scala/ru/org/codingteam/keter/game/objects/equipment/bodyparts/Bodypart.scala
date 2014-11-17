package ru.org.codingteam.keter.game.objects.equipment.bodyparts

import ru.org.codingteam.keter.game.objects.equipment.{Capability, ManipulatorCapability, WalkCapability}

abstract class Bodypart() {
  def name: String

  def health: Double

  def provides: Set[Capability]
}

case class Leg(name: String,
               health: Double,
               provides: Set[Capability] = Set(WalkCapability)) extends Bodypart

case class Arm(name: String,
               health: Double,
               provides: Set[Capability] = Set(ManipulatorCapability)) extends Bodypart

case class Head(name: String,
                health: Double,
                provides: Set[Capability] = Set()) extends Bodypart

case class Torso(name: String,
                 health: Double,
                 provides: Set[Capability] = Set()) extends Bodypart
