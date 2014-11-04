package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.{Actor, ActorId}
import ru.org.codingteam.keter.map.UniverseSnapshot

case class WaitAction(actorId: ActorId, duration: Double = 50.0) extends Action {

  def process(state: UniverseSnapshot, engine: IEngine): UniverseSnapshot = {
    state.findActorOfType[Actor](actorId) foreach { actor =>
      engine.addMessage(s"Time passed for ${actor.name} but nothing has changed")
    }
    state
  }
}
