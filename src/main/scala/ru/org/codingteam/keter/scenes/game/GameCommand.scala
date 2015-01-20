package ru.org.codingteam.keter.scenes.game

import ru.org.codingteam.keter.game.actions.{WaitAction, WalkAction}
import ru.org.codingteam.keter.game.objects.equipment.items.Weapon
import ru.org.codingteam.keter.game.objects.{GenericObjectAction, ObjectActionToActor, Person}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}
import ru.org.codingteam.keter.scenes.inventory.InventoryScene
import ru.org.codingteam.keter.util.Logging

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

abstract class GameCommand {

  def execute(snapshot: UniverseSnapshot, player: Person): Future[Option[GenericObjectAction]]

}

case class WaitCommand() extends GameCommand {

  override def execute(snapshot: UniverseSnapshot, player: Person) = {
    Future(Some(WaitAction()))
  }

}

case class MoveCommand(dx: Int, dy: Int) extends GameCommand with Logging {

  override def execute(snapshot: UniverseSnapshot, player: Person) = {
    val move = Move(dx, dy)
    val target = player.position.moveWithJumps(move).objectPosition
    snapshot.findActors(target).headOption match {
      case None => Future(Some(WalkAction(move)))
      case Some(actor) =>
        import ru.org.codingteam.keter.util.castToOption
        // TODO: weapon selection by user.
        (for (e <- player.inventory.equipment.flatMap(castToOption[Weapon](_));
              a <- e.actions.flatMap(castToOption[ObjectActionToActor](_))
              if a.description == "attack") yield a).headOption match {
          case Some(a) =>
            log.debug("attack")
            Future(Some(a.bind(actor.id)))
          case None =>
            Future(Some(WaitAction()))
        }
    }
  }

}

case class InventoryCommand(scene: GameScene) extends GameCommand with Logging {

  override def execute(snapshot: UniverseSnapshot, player: Person) = {
    log.debug("Entering inventory screen")
    InventoryScene.edit(scene, player.inventory).map {
      case Some(inventory) =>
        log.debug("Inventory changed: " + inventory)
        None // TODO: Change inventory action
      case None =>
        None
    }
  }

}