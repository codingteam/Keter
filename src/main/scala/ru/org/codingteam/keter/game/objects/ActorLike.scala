package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.ActorPosition

trait ActorLike {
  self =>

  def id: ActorId

  def position: ActorPosition

  def eventQueue: EventQueue

  def tile: Option[String]

  def withPosition(p: ActorPosition): self.type

  def withEventQueue(e: EventQueue): self.type

  def nextEventDelay: Option[Double] =
    eventQueue.nextEventDelay map (_ / position.subspaceMatrix.timeCompressionQuotient)

  def addTime(dt: Double): self.type = withEventQueue(
    eventQueue.addTime(dt * position.subspaceMatrix.timeCompressionQuotient))

  def dropNextEvent(): self.type = withEventQueue(eventQueue.dropNextEvent())

  def addEventAfter(dt: Double, action: ScheduledAction): self.type = addEventAt(eventQueue.timestamp + dt, action)

  def addEventAt(at: Double, action: ScheduledAction): self.type = withEventQueue(eventQueue.addEvent(at, action))
}
