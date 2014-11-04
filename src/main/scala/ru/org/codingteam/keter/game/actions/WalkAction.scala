package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.ActorId
import ru.org.codingteam.keter.game.objects.equipment.WalkCapability
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

case class WalkAction(actorId: ActorId,
                      move: Move) extends Action {

  override def process(state: UniverseSnapshot, engine: IEngine) = {
    state.findActor(actorId) match {
      case Some(actor) =>
        val newPosition = actor.position.moveWithJumps(move)
        newPosition.surfaceAt match {
          case Some(s) if s.passable =>
            state.updatedActor(actor.id)(_ withPosition newPosition)
          case _ => state
        }
      case None => state
    }
  }

  override def capabilities = Map(
    WalkCapability -> 1
  )
}
