package ru.org.codingteam.keter.scenes.game

import ru.org.codingteam.keter.game.Engine
import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.map.UniverseSnapshot
import ru.org.codingteam.keter.util.Logging

import scala.concurrent.{Future, Promise}

class GameMapViewModel(val engine: Engine) extends Logging {

  case class RenderState(universe: UniverseSnapshot, promise: Promise[UniverseSnapshot])

  private var _renderState: Option[RenderState] = None
  var onRenderStateChanged: Option[() => Unit] = None

  def renderState = _renderState
  def renderState_=(rs: Option[RenderState]) = {
    _renderState = rs
    onRenderStateChanged map(_())
  }

  def universeState = renderState map(_.universe)

  def processAction(action: GenericObjectAction): Unit = renderState match {
    case Some(RenderState(universe, promise)) =>
      log.debug(s"Player action is: $action")
      universe.player match {
        case Some(player) =>
          if (action.requires subsetOf player.capabilities(universe)) {
            promise.success(action.perform(player.id, universe))
            renderState = None
          } else {
            // TODO: show the message on the screen
            log.debug(s"Player can't perform action: $action")
          }
        case _ =>
          log.warn("No player found")
      }
    case None =>
      log.warn(s"Not requested player`s action: $action")
  }

  def performPlayerAction(universe: UniverseSnapshot): Future[UniverseSnapshot] = {
    log.debug("The next player`s action requested")
    if (renderState.isDefined) {
      renderState.get.promise.failure(new RuntimeException("Next player`s action is requested, but previous one has not filled yet!"))
      renderState = None
      log.warn("Previous action was completed with failure")
    }
    val p = Promise[UniverseSnapshot]()
    renderState = Some(RenderState(universe, p))
    p.future
  }

}
