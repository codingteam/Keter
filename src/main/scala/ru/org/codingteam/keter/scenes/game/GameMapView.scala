package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.map.TraverseUtils.{BoardCell, BoardCoords}
import ru.org.codingteam.keter.map.{Surface, TraverseUtils}
import ru.org.codingteam.keter.ui.IView
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.Display
import ru.org.codingteam.rotjs.wrappers._

class GameMapView(parent: GameScene, width: Int, height: Int, viewModel: GameMapViewModel) extends IView with Logging {

  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  var x = 0
  var y = 0

  val keyMap = {
    import ru.org.codingteam.rotjs.interface.ROT._
    Map(
      VK_NUMPAD5 -> WaitCommand(),
      VK_NUMPAD8 -> MoveCommand(0, -1),
      VK_UP -> MoveCommand(0, -1),
      VK_NUMPAD9 -> MoveCommand(1, -1),
      VK_NUMPAD6 -> MoveCommand(1, 0),
      VK_RIGHT -> MoveCommand(1, 0),
      VK_NUMPAD3 -> MoveCommand(1, 1),
      VK_NUMPAD2 -> MoveCommand(0, 1),
      VK_DOWN -> MoveCommand(0, 1),
      VK_NUMPAD1 -> MoveCommand(-1, 1),
      VK_NUMPAD4 -> MoveCommand(-1, 0),
      VK_LEFT -> MoveCommand(-1, 0),
      VK_NUMPAD7 -> MoveCommand(-1, -1),
      VK_I -> InventoryCommand(parent)
    )
  }

  override def onKeyDown(event: KeyboardEvent): Unit = {
    for (universeState <- viewModel.universeState; player <- universeState.player) {
      keyMap.get(event.keyCode) map(_.execute(universeState, player) map {
        case Some(a) => viewModel.processAction(a)
        case None =>
      })
    }
  }

  override def render(display: Display): Unit = {
    log.debug("Render called")
    for (universeState <- viewModel.universeState; player <- universeState.player) {
      val fieldContainer = display.viewport(x, y, width, height)
      //      log.debug("Drawing field")
      val (offsetX, offsetY) = (fieldContainer.width / 2 - 1, fieldContainer.height / 2 - 1)
      val (fieldWidth, fieldHeight) = (offsetX * 2 + 1, offsetY * 2 + 1)
      val fieldView = fieldContainer.viewportCentered(fieldWidth, fieldHeight, shiftX = offsetX, shiftY = offsetY)
      val cells = TraverseUtils.DirectionLookTraverseMethod.traverse(
        viewModel.engine.universe,
        player.position,
        -offsetY,
        offsetY,
        -offsetX,
        offsetX)
      //      log.debug(s"Cell count: ${cells.size}")
      cells foreach {
        case bc@BoardCell(BoardCoords(x, y), _pos, surface, actors, playerOpt) =>
          //          log.debug(s"cell=$bc")
          val obj = playerOpt orElse actors.headOption orElse surface
          //          log.debug(s"obj=$obj")
          obj foreach {
            case a: ActorLike => fieldView.draw(x, y, a.tile.orNull, getColor(a))
            case s: Surface => fieldView.draw(x, y, s.tile)
          }
      }
    }
    //    log.debug("Render finished")
  }

  private def getColor(actor: ActorLike) = actor match {
    case p: Person => p.state match {
      case ActorActive => null
      case ActorInactive => "#aaa"
    }
    case _ => null
  }

}
