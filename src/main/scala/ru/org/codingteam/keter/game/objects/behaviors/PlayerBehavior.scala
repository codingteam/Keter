package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.actions.{WalkAction, Action}
import ru.org.codingteam.keter.game.objects.{Actor, IActorBehavior}
import ru.org.codingteam.keter.map.UniverseSnapshot

import scala.concurrent.{Future, Promise}

class PlayerBehavior extends IActorBehavior {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  var nextAction = Promise[Action]()

  override def getNextAction(actor: Actor, gameState: UniverseSnapshot): Future[Action] = nextAction.future.andThen({
    /*case WalkAction => if (actor.can(WalkAction)) Promise[Action]()*/
    case _ => nextAction = Promise[Action]()
  })

}
