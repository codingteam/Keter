package ru.org.codingteam.keter.game.objects

case class ActorId(id: Long)

object ActorId {
  private var id = 0L
  def apply(): ActorId = {
    val nextId = id
    id += 1
    ActorId(nextId)
  }
}

