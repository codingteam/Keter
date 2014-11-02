package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.ActorPosition

trait ActorLike {

  type SelfType <: ActorLike

  def id: ActorId

  def position: ActorPosition

  def eventQueue: EventQueue

  def tile: Option[String]

  def withPosition(p: ActorPosition): SelfType

  def withEventQueue(e: EventQueue): SelfType

  def nextEventDelay: Option[Double] =
    eventQueue.nextEventDelay map (_ / position.subspaceMatrix.timeCompressionQuotient)

  def addTime(dt: Double): SelfType = withEventQueue(
    eventQueue.addTime(dt * position.subspaceMatrix.timeCompressionQuotient))

  def dropNextEvent(): SelfType = withEventQueue(eventQueue.dropNextEvent())

  def addEventAfter(dt: Double, action: ScheduledAction): SelfType = addEventAt(eventQueue.timestamp + dt, action)

  def addEventAt(at: Double, action: ScheduledAction): SelfType = withEventQueue(eventQueue.addEvent(at, action))
}
