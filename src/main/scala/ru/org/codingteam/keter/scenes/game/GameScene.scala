package ru.org.codingteam.keter.scenes.game

import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.ui.ViewScene
import ru.org.codingteam.keter.ui.shape.Rectangle
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.ROT.Display
import ru.org.codingteam.rotjs.wrappers._

class GameScene(display: Display, engine: Engine) extends ViewScene(display) with Logging {

  val viewModel = new GameViewModel(engine)
  override def components = Vector(
    gameMapView(Rectangle(1, 1, display.width - 2, display.height - 5), viewModel.map),
    textView(Rectangle(0, display.height - 3, display.width, 3), viewModel.statInfo))

  // GameScene shouldn't be rendered on key down and will be rendered on asynchronous game state change
  override protected def renderOnKeyDown: Boolean = false

  viewModel.map.onRenderStateChanged = Some(() => {
    if (viewModel.map.renderState.isDefined) {
      render()
    }
  })

  def gameMapView(shape: Rectangle, viewModel: GameMapViewModel) = new GameMapView(shape, this, viewModel)
}
