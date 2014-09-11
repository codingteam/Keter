package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.actions._
import ru.org.codingteam.keter.game.objects.behaviors.PlayerBehavior
import ru.org.codingteam.keter.game.objects.{Actor, ActorActive, ActorInactive, ObjectPosition}
import ru.org.codingteam.keter.game.{Engine, GameState, LocationMap}
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrappers._

class GameScene(display: Display, engine: Engine) extends Scene(display) with Logging {

  setGameState(engine.gameState)
  engine.registerCallback(setGameState)

  override protected def onKeyDown(event: KeyboardEvent): Unit = {
    if (event.keyCode == ROT.VK_NUMPAD5) {
      processAction(WaitAction, player.position)
    } else {
      val target = event.keyCode match {
        case x if x == ROT.VK_NUMPAD8 || x == ROT.VK_UP => Some(playerVector(0, -1))
        case x if x == ROT.VK_NUMPAD9 => Some(playerVector(1, -1))
        case x if x == ROT.VK_NUMPAD6 || x == ROT.VK_RIGHT => Some(playerVector(1, 0))
        case x if x == ROT.VK_NUMPAD3 => Some(playerVector(1, 1))
        case x if x == ROT.VK_NUMPAD2 || x == ROT.VK_DOWN => Some(playerVector(0, 1))
        case x if x == ROT.VK_NUMPAD1 => Some(playerVector(-1, 1))
        case x if x == ROT.VK_NUMPAD4 || x == ROT.VK_LEFT => Some(playerVector(-1, 0))
        case x if x == ROT.VK_NUMPAD7 => Some(playerVector(-1, -1))
        case _ => None
      }

      target match {
        case None =>
        case Some(t) =>
          gameState.map.actorAt(t) match {
            case None => processAction(WalkAction, t)
            case Some(a) => processAction(MeleeAttackAction, t)
          }
      }
    }

    render()
  }

  override protected def render(): Unit = {
    display.clear()

    val GameState(messages, locationMap@LocationMap(surfaces, actors, objects, _), time) = gameState
    val player = locationMap.player
    val fieldView = display.viewport(1, 1, display.width - 2, display.height - 5)

    log.debug("Drawing surfaces")
    surfaces.zipWithIndex.foreach { case (row, y) =>
      row.zipWithIndex.foreach { case (surface, x) =>
        fieldView.draw(x, y, surface.tile)
      }
    }

    log.debug("Drawing objects")
    objects.zipWithIndex.foreach { case (row, y) =>
      row.zipWithIndex.foreach { case (obj, x) =>
        if (obj.length > 0) {
          log.debug(s"Drawing object $obj(0).name at $x, $y")
          fieldView.draw(x, y, obj(0).tile)
        }
      }
    }

    log.debug(s"Drawing ${actors.size} actors")
    actors.values foreach (actor => fieldView.draw(actor.position.x, actor.position.y, actor.tile, getColor(actor)))
    // display stats.
    val statsView = display.viewport(0, display.height - 2, display.width, 2)
    statsView.drawTextCentered(s"Faction/Name: ${player.faction.name}/${player.name}", Some(0))
    statsView.drawTextCentered(s"Health: ${player.stats.health} Time passed: $time", Some(1))
  }

  private def player = gameState.map.player

  private def playerVector(dx: Int, dy: Int) = player.position + ObjectPosition(dx, dy)

  private var gameState: GameState = engine.gameState

  private def setGameState(state: GameState): Unit = {
    gameState = state
  }

  private def processAction(action: IActionDefinition, target: ObjectPosition): Unit = {
    log.debug(s"Scheduling player action: $action, target: $target")
    val behavior = player.behavior.asInstanceOf[PlayerBehavior]
    behavior.nextAction.success(Action(player, action, target))
  }

  private def getColor(actor: Actor) = {
    actor.state match {
      case ActorActive => null
      case ActorInactive => "#aaa"
    }
  }

}
