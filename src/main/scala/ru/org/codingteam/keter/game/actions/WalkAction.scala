package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

case class WalkAction(actor: Actor,
                      move: Move) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    val newPosition = actor.position.moveWithJumps(move)
    newPosition.surfaceAt match {
      case Some(s) if s.passable =>
        state.updatedActor(actor.id)(a => a.copy(position = newPosition))
      case _ => state
    }
  }

  override def duration(state: UniverseSnapshot) = (100 * move.length).toLong
}
