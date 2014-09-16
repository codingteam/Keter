package ru.org.codingteam.keter.game.actions

abstract class Capability(name:String)

case object WalkCapability extends Capability("Walk")
case object MeleeAttackCapability extends Capability("MeleeAttack")
