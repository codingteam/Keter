package ru.org.codingteam.keter.scenes.game

import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.ui.viewmodels.TextViewModel

class GameViewModel(engine: Engine) {

  val map = new GameMapViewModel(engine)
  val statInfo = new TextViewModel() {

    override def text: String = {
      map.universeState.map { universe =>
        universe.player.map { player =>
          s"Faction/Name: ${player.faction.name}/${player.name}\n" +
          s"Health: ${player.stats.health}\n" +
          s"Time global/local: ${universe.globalEvents.timestamp}/${player.eventQueue.timestamp}"
        }.getOrElse("")
      }.getOrElse("")
    }

  }

}
