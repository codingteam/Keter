package ru.org.codingteam.keter.game.objects

abstract sealed class ActorState
case object ActorActive extends ActorState
case object ActorInactive extends ActorState
