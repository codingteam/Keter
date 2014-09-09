package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{Door, ObjectPosition, Actor}

case class MoveAction(override val actor: Actor, x: Int, y: Int) extends Action(actor) {

  override def process(state: GameState): GameState = {
    val map = state.map
    val position = actor.position
    val newX = position.x + x
    val newY = position.y + y
    val surface = map.surfaceAt(newX, newY)
    val topObject = map.firstObjectAt(newX, newY)
    lazy val cannotMove = state.copy(state.messages :+ "You cannot move this way")
    surface match {
      case None => cannotMove
      case Some(tile) if !tile.passable => cannotMove
      case Some(tile) => {
        val newObjects = map.objects
        println(1)
        topObject match {
          case None => {}
          case Some(o) if (o.isInstanceOf[Door]) => {
            println(2)
            val door = o.asInstanceOf[Door]
            println(3)
            val newDoor = door.copy(tile = door.openTile, open = false, passable = true)
            println(4)
            newObjects(newY)(newX) = newObjects(newY)(newX).updated(0, newDoor)
            println(5)
          }
        }
        val newActor = actor.copy(position = ObjectPosition(newX, newY))
        val newActors = map.actors + (newActor.id -> newActor)
        state.copy(map = map.copy(actors = newActors, objects = newObjects))
      }
    }
  }

  override def duration = (100 * Math.sqrt(Math.abs(x) + Math.abs(y))).toLong

}
