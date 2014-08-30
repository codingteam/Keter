package ru.org.codingteam.keter.scenes.game

import org.scalajs.dom.KeyboardEvent
import ru.org.codingteam.keter.game.actions.{Action, WaitAction, MoveAction}
import ru.org.codingteam.keter.game.objects.Player
import ru.org.codingteam.keter.game.{LocationMap, GameState}
import ru.org.codingteam.keter.scenes.Scene
import ru.org.codingteam.rotjs.interface.{ROT, Display}

class GameScene(display: Display, var state: GameState) extends Scene(display) {

  override protected def onKeyDown(event: KeyboardEvent): Unit = {
    event.keyCode match {
      case x if x == ROT.VK_NUMPAD8 || x == ROT.VK_UP => processAction(MoveAction(0, -1))
      case x if x == ROT.VK_NUMPAD9 => processAction(MoveAction(1, -1))
      case x if x == ROT.VK_NUMPAD6 || x == ROT.VK_RIGHT => processAction(MoveAction(1, 0))
      case x if x == ROT.VK_NUMPAD3 => processAction(MoveAction(1, 1))
      case x if x == ROT.VK_NUMPAD2 || x == ROT.VK_DOWN => processAction(MoveAction(0, 1))
      case x if x == ROT.VK_NUMPAD1 => processAction(MoveAction(-1, 1))
      case x if x == ROT.VK_NUMPAD4 || x == ROT.VK_LEFT => processAction(MoveAction(-1, 0))
      case x if x == ROT.VK_NUMPAD7 => processAction(MoveAction(-1, -1))
      case x if x == ROT.VK_NUMPAD5 => processAction(WaitAction())
      case _ =>
    }
  }

  override protected def render(): Unit = {
    display.clear()

    val GameState(messages, LocationMap(surfaces, objects)) = state
    surfaces.zipWithIndex.foreach { case (row, y) =>
      row.zipWithIndex.foreach { case (surface, x) =>
        display.draw(x, y, surface.tile)
      }
    }

    objects foreach { case (obj, (x, y)) =>
      display.draw(x, y, obj.tile)
    }
  }

  private def processAction(action: Action): Unit = {
    println(s"Processing player action: $action")

    val player = state.map.objects.keys.filter({
      case Player(_) => true
      case _ => false
    }).head.asInstanceOf[Player]

    state = action.process(player, state)

    println(s"Messages: ${state.messages}")
  }

}
