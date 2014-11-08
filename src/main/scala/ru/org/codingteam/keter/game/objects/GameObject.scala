package ru.org.codingteam.keter.game.objects

trait GameObject extends ActorLike {
  def owner: ActorId

  def actionsList: Seq[ObjectAction]
}
