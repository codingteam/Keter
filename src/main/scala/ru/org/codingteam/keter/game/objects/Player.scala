package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.actions.Action

import scala.concurrent.Promise

case class Player(override val name: String) extends Actor(name, "@") {

  implicit val executionContext = scala.scalajs.concurrent.JSExecutionContext.runNow

  var nextAction = Promise[Action]()

  override def playerControllable = true
  override def getNextAction() = nextAction.future.andThen({
    case _ => nextAction = Promise[Action]()
  })

}
