package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.Application
import ru.org.codingteam.keter.game.objects.{ActorId, IActorBehavior}
import ru.org.codingteam.keter.map.UniverseSnapshot
import ru.org.codingteam.keter.scenes.game.GameScene

import scala.concurrent.Future

class PlayerBehavior extends IActorBehavior {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.queue

  override def getNextAction(actorId: ActorId, state: UniverseSnapshot): Future[UniverseSnapshot] = Application.currentScene match {
    case Some(scene: GameScene) =>
      scene.viewModel.map.performPlayerAction(state)
    case _ =>
      Future.failed(new RuntimeException("Player`s action is requested, but current scene is not GameScene"))
  }

}
