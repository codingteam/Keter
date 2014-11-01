package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.{ActorPosition, ObjectPosition, UniverseSnapshot}

import scala.collection.SortedMap
import scala.concurrent.Future


trait ActorLike {
  def id: ActorId

  def position: ActorPosition

  def eventQueue: EventQueue

  def withPosition(p: ActorPosition): ActorLike

  def withEventQueue(e: EventQueue): ActorLike

  def nextEventDelay: Option[Double] =
    eventQueue.nextEventDelay map (_ / position.subspaceMatrix.timeCompressionQuotient)

  def addTime(dt: Double): ActorLike = withEventQueue(
    eventQueue.addTime(dt * position.subspaceMatrix.timeCompressionQuotient))

  def dropNextEvent(): ActorLike = withEventQueue(eventQueue.dropNextEvent())
}

trait NewGameObject extends ActorLike {
  def owner: ActorId

  def actionsList: Seq[ObjectAction]
}

trait ObjectAction {
  def description: String
}

trait GenericAction extends ObjectAction {
  def perform(universe: UniverseSnapshot): Future[UniverseSnapshot]
}

trait ActionToPosition extends ObjectAction {
  def perform(universe: UniverseSnapshot, to: ObjectPosition): Future[UniverseSnapshot]
}

trait ActionToActor extends ObjectAction {
  def perform(universe: UniverseSnapshot, id: ActorId): Future[UniverseSnapshot]
}


trait ScheduledAction {
  def perform(universe: UniverseSnapshot): Future[UniverseSnapshot]
}

sealed class EventQueue(val timestamp: Double, events: SortedMap[Double, Seq[ScheduledAction]]) {

  def addEvent(at: Double, event: ScheduledAction): EventQueue = {
    new EventQueue(timestamp, events + (at -> (events.getOrElse(at, IndexedSeq()) :+ event)))
  }

  def nextEventTime: Option[Double] = events.headOption map (_._1)

  def nextEvent: Option[(Double, ScheduledAction)] = events.headOption map {
    case (at, actions) => (at, actions.head)
  }

  def dropNextEvent(): EventQueue = {
    require(events.nonEmpty)
    events.head match {
      case (at, Seq(action)) => new EventQueue(timestamp, events.tail)
      case (at, actions) => new EventQueue(timestamp, events.tail + (at -> actions.tail))
    }
  }

  def addTime(dt: Double): EventQueue = new EventQueue(timestamp + dt, events)

  def nextEventDelay: Option[Double] = nextEventTime map (_ - timestamp)
}

object EventQueue {
  val empty = new EventQueue(0, SortedMap())
}