package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.UniverseSnapshot

import scala.concurrent.{Future, Promise}

class PlayerBehavior extends IActorBehavior {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  var nextAction = Promise[UniverseSnapshot]()

  override def getNextAction(actor: Actor, gameState: UniverseSnapshot): Future[UniverseSnapshot] = nextAction.future.andThen({
    case _ => nextAction = Promise[UniverseSnapshot]()
  })

}
