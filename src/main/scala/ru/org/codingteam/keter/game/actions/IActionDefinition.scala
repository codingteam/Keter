package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.Actor
import ru.org.codingteam.keter.map.{ObjectPosition, Universe}

trait IActionDefinition {

  def process(actor: Actor, target: ObjectPosition, universe: Universe)

  def duration(actor: Actor, target: ObjectPosition, universe: Universe): Long

}
