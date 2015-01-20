package ru.org.codingteam.keter.scenes.menu

import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.game.{Engine, Location}
import ru.org.codingteam.keter.map.Universe
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.scenes.game.GameScene
import ru.org.codingteam.keter.ui.viewmodels.MenuViewModel

class MainMenuViewModel(scene: Scene) extends MenuViewModel {

  override val items = Vector(
    "New Game" -> newGame _,
    "Load Game" -> notImplemented _
  )

  def newGame(): Unit = {
    val universe = Universe(Location.createLocation())
    val engine = new Engine(universe)
    val gameScene = new GameScene(scene.display, engine)
    Application.setScene(gameScene)
    engine.start()
  }

  def notImplemented(): Unit = {
    Application.setScene(new NotImplementedScene(scene.display, scene))
  }

}
