package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.map.UniverseSnapshot

import scala.concurrent.Future

trait ScheduledAction {
  def perform(universe: UniverseSnapshot): Future[UniverseSnapshot]
}
