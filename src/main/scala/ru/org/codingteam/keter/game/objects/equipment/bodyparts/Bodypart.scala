package ru.org.codingteam.keter.game.objects.equipment.bodyparts

import ru.org.codingteam.keter.game.objects.equipment.{MeleeAttackCapability, WalkCapability, Capability}

abstract class Bodypart(name:String, health: Double, actions: Seq[Capability])

case class Leg(name:String,
               health: Double,
               actions: Seq[Capability] = Seq(WalkCapability)) extends Bodypart(name, health, actions)

case class Arm(name:String,
               health: Double,
               actions: Seq[Capability] = Seq(MeleeAttackCapability)) extends Bodypart(name, health, actions)

case class Head(name:String,
               health: Double,
               actions: Seq[Capability]) extends Bodypart(name, health, actions)

case class Torso(name:String,
               health: Double,
               actions: Seq[Capability]) extends Bodypart(name, health, actions)