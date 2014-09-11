package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.Action
import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}

import scala.concurrent.{Promise, Future}

class PlayerBehavior extends IActorBehavior {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  var nextAction = Promise[Action]()

  override def getNextAction(actor: Actor, gameState: GameState): Future[Action] = nextAction.future.andThen({
    case _ => nextAction = Promise[Action]()
  })

}
