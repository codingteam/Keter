package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.actions.{Action, MoveAction, WaitAction}
import ru.org.codingteam.keter.game.objects.behaviors.PlayerBehavior
import ru.org.codingteam.keter.game.{Engine, GameState, LocationMap}
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.rotjs.interface.{Display, ROT}
import ru.org.codingteam.rotjs.wrapper.Wrappers._

class GameScene(display: Display, engine: Engine) extends Scene(display) {

  setGameState(engine.gameState)
  engine.registerCallback(setGameState)

  override protected def onKeyDown(event: KeyboardEvent): Unit = {
    event.keyCode match {
      case x if x == ROT.VK_NUMPAD8 || x == ROT.VK_UP => processAction(MoveAction(player, 0, -1))
      case x if x == ROT.VK_NUMPAD9 => processAction(MoveAction(player, 1, -1))
      case x if x == ROT.VK_NUMPAD6 || x == ROT.VK_RIGHT => processAction(MoveAction(player, 1, 0))
      case x if x == ROT.VK_NUMPAD3 => processAction(MoveAction(player, 1, 1))
      case x if x == ROT.VK_NUMPAD2 || x == ROT.VK_DOWN => processAction(MoveAction(player, 0, 1))
      case x if x == ROT.VK_NUMPAD1 => processAction(MoveAction(player, -1, 1))
      case x if x == ROT.VK_NUMPAD4 || x == ROT.VK_LEFT => processAction(MoveAction(player, -1, 0))
      case x if x == ROT.VK_NUMPAD7 => processAction(MoveAction(player, -1, -1))
      case x if x == ROT.VK_NUMPAD5 => processAction(WaitAction(player))
      case _ =>
    }

    render()
  }

  override protected def render(): Unit = {
    display.clear()

    val GameState(messages, locationMap@LocationMap(surfaces, actors, _), time) = gameState
    val player = locationMap.player

    surfaces.zipWithIndex.foreach { case (row, y) =>
      row.zipWithIndex.foreach { case (surface, x) =>
        display.draw(x, y, surface.tile)
      }
    }


    println(s"Drawing ${actors.size} objects")
    actors.values foreach (actor => display.draw(actor.position.x, actor.position.y, actor.tile))
    // display stats.
    display.drawTextCentered(s"Faction/Name: ${player.faction.name}/${player.name}", Some(display.height - 2))
    display.drawTextCentered(s"Health: ${player.stats.health} Time passed: $time", Some(display.height - 1))
  }

  private def player = gameState.map.actors.values.filter(_.behavior.isInstanceOf[PlayerBehavior]).head

  private var gameState: GameState = engine.gameState

  private def setGameState(state: GameState): Unit = {
    gameState = state
  }

  private def processAction(action: Action): Unit = {
    println(s"Scheduling player action: $action")
    val behavior = player.behavior.asInstanceOf[PlayerBehavior]
    behavior.nextAction.success(action)
  }

}
