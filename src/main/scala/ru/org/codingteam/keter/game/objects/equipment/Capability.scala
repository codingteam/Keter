package ru.org.codingteam.keter.game.objects.equipment

abstract class Capability(name: String)

case object WalkCapability extends Capability("Walk")

case object ManipulatorCapability extends Capability("MeleeAttack")
