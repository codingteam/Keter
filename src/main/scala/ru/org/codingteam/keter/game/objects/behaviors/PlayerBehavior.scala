package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.UniverseSnapshot
import ru.org.codingteam.keter.scenes.game.GameScene

import scala.concurrent.Future

class PlayerBehavior extends IActorBehavior {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.queue

  override def getNextAction(actor: Actor, gameState: UniverseSnapshot): Future[UniverseSnapshot] = Application.currentScene match {
    case Some(scene: GameScene) =>
      scene.nextPlayerAction(gameState) map (Actor.processAction(_, gameState))
    case _ =>
      Future.failed(new RuntimeException("Player`s action is requested, but current scene is not GameScene"))
  }

}
