package ru.org.codingteam.keter.game.objects.behaviors

import ru.org.codingteam.keter.game.GameState
import ru.org.codingteam.keter.game.actions.{MoveAction, WaitAction}
import ru.org.codingteam.keter.game.objects.{Actor, ActorBehavior}

import scala.concurrent.Future

trait StupidMoverBehavior extends ActorBehavior {
  val to: Int

  def getNextAction(actor: Actor, state: GameState) = Future.successful({
    var (x, y) = (0, 0)

    for (a <- state.map.actors) {
      if (a.faction != actor.faction) {
        x = math.signum(a.position.x - actor.position.x) * to
        y = math.signum(a.position.y - actor.position.y) * to
      }
    }

    if (x == 0 && y == 0) {
      WaitAction(actor)
    } else {
      MoveAction(actor, x, y)
    }
  })

}
