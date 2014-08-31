package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.{ObjectPosition, Actor}

case class MoveAction(override val actor: Actor, x: Int, y: Int) extends Action(actor) {

  override def process(state: GameState): GameState = {
    val map = state.map
    val position = actor.position
    val newX = position.x + x
    val newY = position.y + y
    val surface = map.surfaceAt(newX, newY)
    lazy val cannotMove = state.copy(state.messages :+ "You cannot move this way")
    surface match {
      case None => cannotMove
      case Some(tile) if !tile.passable => cannotMove
      case Some(tile) =>
        val newActor = actor.copy(position = ObjectPosition(newX, newY))
        state.copy(map = map.copy(actors = map.actors + (newActor.id -> newActor)))
    }
  }

  override def duration = (100 * Math.sqrt(Math.abs(x) + Math.abs(y))).toLong

}
