package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.game.actions._
import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.map.TraverseUtils.{BoardCell, BoardCoords}
import ru.org.codingteam.keter.map.{Move, Surface, TraverseUtils, UniverseSnapshot}
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrappers._

import scala.concurrent.{Future, Promise}

class GameScene(display: Display, engine: Engine) extends Scene(display) with Logging {

  case class RenderState(universe: UniverseSnapshot, promise: Promise[Action])

  private var _renderState: Option[RenderState] = None

  def renderState = _renderState

  def renderState_=(rs: Option[RenderState]) = {
    _renderState = rs
    render()
  }

  override protected def onKeyDown(event: KeyboardEvent): Unit = {
    for (RenderState(universeState, _) <- renderState; player <- universeState.player) {
      if (event.keyCode == ROT.VK_NUMPAD5) {
        processAction(WaitAction(player.id))
      } else {
        val move = event.keyCode match {
          case x if x == ROT.VK_NUMPAD8 || x == ROT.VK_UP =>
            Some(Move(0, -1))
          case x if x == ROT.VK_NUMPAD9 =>
            Some(Move(1, -1))
          case x if x == ROT.VK_NUMPAD6 || x == ROT.VK_RIGHT =>
            Some(Move(1, 0))
          case x if x == ROT.VK_NUMPAD3 =>
            Some(Move(1, 1))
          case x if x == ROT.VK_NUMPAD2 || x == ROT.VK_DOWN =>
            Some(Move(0, 1))
          case x if x == ROT.VK_NUMPAD1 =>
            Some(Move(-1, 1))
          case x if x == ROT.VK_NUMPAD4 || x == ROT.VK_LEFT =>
            Some(Move(-1, 0))
          case x if x == ROT.VK_NUMPAD7 =>
            Some(Move(-1, -1))
          case _ => None
        }
        move map { m =>
          val target = player.position.moveWithJumps(m).objectPosition
          universeState.findActors(target).headOption match {
            case None => processAction(WalkAction(player.id, m))
            case Some(actor) => processAction(MeleeAttackAction(player.id, target))
          }
        }
      }
    }
  }

  override protected def render(): Unit = {
    log.debug("Render called")
    for (RenderState(universeState, _) <- renderState; player <- universeState.player) {
      //      log.debug("Drawing field")
      val fieldContainer = display.viewport(1, 1, display.width - 2, display.height - 5)
      val (offsetX, offsetY) = (fieldContainer.width / 2 - 1, fieldContainer.height / 2 - 1)
      val (fieldWidth, fieldHeight) = (offsetX * 2 + 1, offsetY * 2 + 1)
      val fieldView = fieldContainer.viewportCentered(fieldWidth, fieldHeight, shiftX = offsetX, shiftY = offsetY)
      val cells = TraverseUtils.DirectionLookTraverseMethod.traverse(engine.universe, player.position, -offsetY, offsetY, -offsetX, offsetX)
      //      log.debug(s"Cell count: ${cells.size}")
      display.clear()
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
      // display stats.
      //      log.debug("Drawing stats")
      val statsView = display.viewport(0, display.height - 3, display.width, 3)
      statsView.drawTextCentered(s"Faction/Name: ${player.faction.name}/${player.name}", Some(0))
      statsView.drawTextCentered(s"Health: ${player.stats.health}", Some(1))
      statsView.drawTextCentered(s"Time global/local: ${universeState.globalEvents.timestamp}/${player.eventQueue.timestamp}", Some(2))
    }
    //    log.debug("Render finished")
  }

  private def processAction(action: Action): Unit = renderState match {
    case Some(RenderState(universe, promise)) =>
      log.debug(s"Player action is: $action")
      universe.player match {
        case Some(a) =>
          if (action canAct a) {
            promise.success(action)
            renderState = None
          } else {
            // TODO: show the message on the screen
            log.debug(s"Player can't perform action: $action")
          }
        case _ =>
          promise.success(action)
          renderState = None
      }
    case None =>
      log.warn(s"Not requested player`s action: $action")
  }


  private def getColor(actor: ActorLike) = actor match {
    case p: Person => p.state match {
      case ActorActive => null
      case ActorInactive => "#aaa"
    }
    case _ => null
  }

  def nextPlayerAction(universe: UniverseSnapshot): Future[Action] = {
    log.debug("The next player`s action requested")
    if (renderState.isDefined) {
      renderState.get.promise.failure(new RuntimeException("Next player`s action is requested, but previous one has not filled yet!"))
      renderState = None
      log.warn("Previous action was completed with failure")
    }
    val p = Promise[Action]()
    renderState = Some(RenderState(universe, p))
    p.future
  }

}
