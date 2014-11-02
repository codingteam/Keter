package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.map.UniverseSnapshot

case class WaitAction(actor: Actor, duration: Double = 50.0) extends Action {
  def duration(state: UniverseSnapshot): Double = duration

  def process(state: UniverseSnapshot, engine: IEngine): UniverseSnapshot = {
    engine.addMessage(s"Time passed for ${actor.name} but nothing has changed")
    state
  }
}
