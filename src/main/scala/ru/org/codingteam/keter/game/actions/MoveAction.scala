package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.objects.Actor

case class MoveAction(x: Int, y: Int) extends Action {

  override def process(actor: Actor, state: GameState): GameState = {
    val map = state.map
    val objects = map.objects
    val position = objects(actor)
    val newX = position._1 + x
    val newY = position._2 + y
    val surface = map.surfaceAt(newX, newY)
    lazy val cannotMove = state.copy(state.messages :+ "You cannot move this way")
    surface match {
      case None => cannotMove
      case Some(tile) if !tile.passable => cannotMove
      case Some(tile) => state.copy(map = map.copy(objects = map.objects + (actor -> (newX, newY))))
    }
  }

  override def duration = (100 * Math.sqrt(Math.abs(x) + Math.abs(y))).toLong

}
