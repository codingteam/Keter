package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.IEngine
import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.map.UniverseSnapshot

trait Action {
  def actor: Actor

  def duration(state: UniverseSnapshot): Long

  def process(state: UniverseSnapshot, engine: IEngine): UniverseSnapshot

}
