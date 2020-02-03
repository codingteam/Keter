package ru.org.codingteam.keter.scenes.game

import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.ui.viewmodels.TextViewModel

class GameViewModel(engine: Engine) {

  val map = new GameMapViewModel(engine)
  val statInfo: TextViewModel = new TextViewModel() {

    override def text: String = {
      map.universeState.map { universe =>
        universe.player.map { player =>
          s"Faction/Name: ${player.faction.name}/${player.name}\n" +
          s"Health: ${player.stats.health}\n" +
          f"Time global/local: ${universe.globalEvents.timestamp}%.03f/${player.eventQueue.timestamp}%.03f"
        }.getOrElse("")
      }.getOrElse("")
    }

  }

}
