package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.rotjs.interface.Display

class GameScene(display: Display, var state: GameState) extends Scene(display) {

  override protected def onKeyDown(event: KeyboardEvent): Unit = {

  }

  override protected def render(): Unit = {
    display.clear()

    state.map.surfaces.zipWithIndex.foreach { case (row, y) =>
      row.zipWithIndex.foreach { case (surface, x) =>
        display.draw(x, y, surface.tile)
      }
    }
  }

}
