package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.actions.Action

import scala.concurrent.Promise

case class Player(override val name: String) extends Actor(name, "@") {

  var nextAction = Promise[Action]()

  override def playerControllable = true
  override def getNextAction() = nextAction.future

  override def resetNextAction(): Unit = {
    nextAction = Promise[Action]()
  }

}
