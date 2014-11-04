package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.ActorId

case class WaitAction(actorId: ActorId, duration: Double = 50.0) extends Action
