package ru.org.codingteam.keter.game.objects

import scala.collection.SortedMap


sealed class EventQueue(val timestamp: Double, events: SortedMap[Double, Seq[ScheduledAction]]) {

  def addEvent(at: Double, event: ScheduledAction): EventQueue = {
    new EventQueue(timestamp, events + (at -> (events.getOrElse(at, IndexedSeq()) :+ event)))
  }

  def nextEventTime: Option[Double] = events.headOption map (_._1)

  def nextEvent: Option[(Double, ScheduledAction)] = events.headOption map {
    case (at, actions) => (at, actions.head)
  }

  def dropNextEvent(): EventQueue = {
    require(events.nonEmpty, "Event queue is empty!")
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
