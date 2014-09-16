package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.map.UniverseSnapshot

case class WaitAction(actor: Actor, duration: Long = 50L) extends Action {
  def duration(state: UniverseSnapshot): Long = duration

  def process(state: UniverseSnapshot, engine: IEngine): UniverseSnapshot = {
    engine.addMessage(s"Time passed for ${actor.name} but nothing has changed")
    state
  }
}
