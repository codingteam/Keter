package ru.org.codingteam.keter.scenes.game

import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.ui.ViewScene
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.Display
import ru.org.codingteam.rotjs.wrappers._

class GameScene(display: Display, engine: Engine) extends ViewScene(display) with Logging {

  val viewModel = new GameViewModel(engine)
  override def components = Vector(
    gameMapView(1, 1, display.width - 2, display.height - 5, viewModel.map),
    textView(0, display.height - 3, display.width, 3, viewModel.statInfo))

  viewModel.map.onRenderStateChanged = Some(() => render())

  def gameMapView(x: Int, y: Int, width: Int, height: Int, viewModel: GameMapViewModel) = {
    val view = new GameMapView(this, width, height, viewModel)
    view.x = x
    view.y = y
    view
  }

}
