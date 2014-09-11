package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{Door, ObjectPosition, Actor}

case object WalkAction extends IActionDefinition {

  override def process(actor: Actor, target: ObjectPosition, state: GameState): GameState = {
    val map = state.map
    val newX = target.x
    val newY = target.y
    val surface = map.surfaceAt(newX, newY)
    val topObject = map.firstObjectAt(newX, newY)
    lazy val cannotMove = state.copy(state.messages :+ "You cannot move this way")
    if (actor.position - target >= 2) {
      cannotMove
    } else {
      surface match {
        case None => cannotMove
        case Some(tile) if !tile.passable => cannotMove
        case Some(tile) =>
          val newObjects = map.objects
          topObject match {
            case Some(door: Door) if !door.open =>
              val newDoor = door.copy(tile = door.openTile, open = true, passable = true)
              newObjects(newY)(newX) = newObjects(newY)(newX).updated(0, newDoor)
            case Some(_) =>
            case None =>
          }
          val newActor = actor.copy(position = ObjectPosition(newX, newY))
          val newActors = map.actors + (newActor.id -> newActor)
          state.copy(map = map.copy(actors = newActors, objects = newObjects))
      }
    }
  }

  override def duration(actor: Actor, target: ObjectPosition, state: GameState) =
    (100 * Math.sqrt(Math.abs(target.x) + Math.abs(target.y))).toLong

}
