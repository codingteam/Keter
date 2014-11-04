package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.{ObjectPosition, UniverseSnapshot}

import scala.concurrent.Future

trait GameObject extends ActorLike {
  def owner: ActorId

  def actionsList: Seq[ObjectAction]
}

trait ObjectAction {
  def description: String
}

trait GenericObjectAction extends ObjectAction {
  def perform(universe: UniverseSnapshot): Future[UniverseSnapshot]
}

trait ObjectActionToPosition extends ObjectAction {
  def perform(universe: UniverseSnapshot, to: ObjectPosition): Future[UniverseSnapshot]
}

trait ObjectActionToActor extends ObjectAction {
  def perform(universe: UniverseSnapshot, id: ActorId): Future[UniverseSnapshot]
}

