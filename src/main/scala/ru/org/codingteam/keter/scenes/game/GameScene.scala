package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.game.actions.{WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.game.objects.equipment.items.Weapon
import ru.org.codingteam.keter.map.TraverseUtils.{BoardCell, BoardCoords}
import ru.org.codingteam.keter.map.{Move, Surface, TraverseUtils, UniverseSnapshot}
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrappers._

import scala.concurrent.{Future, Promise}

class GameScene(display: Display, engine: Engine) extends Scene(display) with Logging {

  case class RenderState(universe: UniverseSnapshot, promise: Promise[UniverseSnapshot])

  private var _renderState: Option[RenderState] = None

  def renderState = _renderState

  def renderState_=(rs: Option[RenderState]) = {
    _renderState = rs
    render()
  }

  abstract class PlayerCommand() {
    def execute(snapshot: UniverseSnapshot, player: Person): Unit
  }

  case class WaitCommand() extends PlayerCommand() {
    override def execute(snapshot: UniverseSnapshot, player: Person): Unit = {
      processAction(WaitAction())
    }
  }

  case class MoveCommand(dx: Int, dy: Int) extends PlayerCommand() {
    override def execute(snapshot: UniverseSnapshot, player: Person): Unit = {
      val move = Move(dx, dy)
      val target = player.position.moveWithJumps(move).objectPosition
      snapshot.findActors(target).headOption match {
        case None => processAction(WalkAction(move))
        case Some(actor) =>
          import ru.org.codingteam.keter.util.castToOption
          // TODO: weapon selection by user.
          (for (e <- player.inventory.equipment.flatMap(castToOption[Weapon](_));
                a <- e.actions.flatMap(castToOption[ObjectActionToActor](_))
                if a.description == "attack") yield a).headOption match {
            case Some(a) =>
              log.debug("attack")
              processAction(a.bind(actor.id))
            case None =>
              processAction(WaitAction())
          }
      }
    }
  }

  case class InventoryCommand() extends PlayerCommand() {
    override def execute(snapshot: UniverseSnapshot, player: Person): Unit = {
      log.info("Entering inventory screen")
      import scalajs.concurrent.JSExecutionContext.Implicits.queue
      InventoryScene.edit(GameScene.this, player.inventory).onSuccess {
        case Some(inventory) =>
          // TODO: Set inventory to player
          log.info("Inventory changed: " + inventory)
        case None =>
      }
    }
  }

  val keys = {
    import ROT._
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
      VK_I -> InventoryCommand()
    )
  }

  override protected def onKeyDown(event: KeyboardEvent): Unit = {
    for (RenderState(universeState, _) <- renderState; player <- universeState.player) {
      keys.get(event.keyCode) map(_.execute(universeState, player))
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

  private def processAction(action: GenericObjectAction): Unit = renderState match {
    case Some(RenderState(universe, promise)) =>
      log.debug(s"Player action is: $action")
      universe.player match {
        case Some(player) =>
          if (action.requires subsetOf player.capabilities(universe)) {
            promise.success(action.perform(player.id, universe))
            renderState = None
          } else {
            // TODO: show the message on the screen
            log.debug(s"Player can't perform action: $action")
          }
        case _ =>
          log.warn("No player found")
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

  def performPlayerAction(universe: UniverseSnapshot): Future[UniverseSnapshot] = {
    log.debug("The next player`s action requested")
    if (renderState.isDefined) {
      renderState.get.promise.failure(new RuntimeException("Next player`s action is requested, but previous one has not filled yet!"))
      renderState = None
      log.warn("Previous action was completed with failure")
    }
    val p = Promise[UniverseSnapshot]()
    renderState = Some(RenderState(universe, p))
    p.future
  }

}
