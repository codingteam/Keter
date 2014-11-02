package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.ActorPosition

final case class Door(id: ActorId,
                      name: String,
                      open: Boolean,
                      openTile: String,
                      closedTile: String,
                      position: ActorPosition,
                      eventQueue: EventQueue = EventQueue.empty) extends ActorLike {
  def tile = if (open) Some(openTile) else Some(closedTile)

  def passable = open

  override def withEventQueue(e: EventQueue): Door = copy(eventQueue = e)

  override def withPosition(p: ActorPosition): Door = copy(position = p)
}
