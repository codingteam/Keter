package ru.org.codingteam.keter

import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.util.Logging

import scala.annotation.tailrec
import scala.reflect.ClassTag

package object map {

  case class ActorPosition(submap: Submap, coords: ActorCoords, subspaceMatrix: SubspaceMatrix = SubspaceMatrix.identity) {
    private def move(m: Move): ActorPosition = copy(coords = coords + subspaceMatrix.convertMove(m))

    private def jump(jump: Jump): ActorPosition = copy(
      submap = jump submapFunc submap,
      coords = jump coordsFunc coords,
      subspaceMatrix = jump matrixFunc subspaceMatrix)

    def moveWithJumps(m: Move): ActorPosition = {
      @tailrec
      def processJumps(pos: ActorPosition): ActorPosition = pos.surfaceAt match {
        case Some(j: Jump) => processJumps(pos jump j)
        case _ => pos
      }
      processJumps(move(m))
    }

    def withUpdatedTime(f: Int => Int): ActorPosition = {
      copy(coords = coords.copy(t = f(coords.t)))
    }

    lazy val surfaceAt = submap.surfaceAt(coords.x, coords.y)

    lazy val objectPosition = ObjectPosition(submap, ObjectCoords(coords.x, coords.y))
  }

  case class ActorCoords(x: Int, y: Int, t: Int = 0) {
    def +(m: Move) = ActorCoords(x + m.dx, y + m.dy, t + m.dt)
  }

  case class ObjectPosition(submap: Submap, coords: ObjectCoords)

  case class ObjectCoords(x: Int, y: Int)

  case class Move(dx: Int, dy: Int, dt: Int = 0) {
    def length = math.hypot(dx, dy)
  }

  class Submap(val surfaces: IndexedSeq[IndexedSeq[Option[Surface]]]) {
    def surfaceAt(x: Int, y: Int): Option[Surface] = {
      if (surfaces.indices.contains(y) && surfaces(y).indices.contains(x)) surfaces(y)(x) else None
    }
  }

  sealed abstract class Surface(val name: String, val tile: String, val passable: Boolean)

  case class Wall() extends Surface(name = "Wall", tile = "#", passable = false)

  case class Floor() extends Surface(name = "Floor", tile = ".", passable = true)

  case class Jump(submapFunc: Submap => Submap = identity,
                  coordsFunc: ActorCoords => ActorCoords = identity,
                  matrixFunc: SubspaceMatrix => SubspaceMatrix = identity) extends Surface(name = "Jump", tile = ">", passable = true)

  case class SubspaceMatrix(m00: Double, m01: Double, m02: Double,
                            m10: Double, m11: Double, m12: Double,
                            m20: Double, m21: Double, m22: Double) {
    def convertMove(m: Move) = Move(
      (m.dx * m00 + m.dy * m01 + m.dt * m02).round.toInt,
      (m.dx * m10 + m.dy * m11 + m.dt * m12).round.toInt,
      (m.dx * m20 + m.dy * m21 + m.dt * m22).round.toInt)

    def rotateMoveBack(m: Move) = rotateBackMatrix.convertMove(m)

    def rotateBackMatrix = {
      val det = m00 * m11 - m01 * m10
      SubspaceMatrix(
        m11 / det, -m01 / det, 0,
        -m10 / det, m00 / det, 0,
        0, 0, m22)
    }

    def *(m: SubspaceMatrix) = SubspaceMatrix(
      this.m00 * m.m00 + this.m01 * m.m10 + this.m02 * m.m20,
      this.m00 * m.m01 + this.m01 * m.m11 + this.m02 * m.m21,
      this.m00 * m.m02 + this.m01 * m.m12 + this.m02 * m.m22,
      this.m10 * m.m00 + this.m11 * m.m10 + this.m12 * m.m20,
      this.m10 * m.m01 + this.m11 * m.m11 + this.m12 * m.m21,
      this.m10 * m.m02 + this.m11 * m.m12 + this.m12 * m.m22,
      this.m20 * m.m00 + this.m21 * m.m10 + this.m22 * m.m20,
      this.m20 * m.m01 + this.m21 * m.m11 + this.m22 * m.m21,
      this.m20 * m.m02 + this.m21 * m.m12 + this.m22 * m.m22)

    def timeCompressionQuotient = m22
  }

  object SubspaceMatrix {
    lazy val identity = SubspaceMatrix(1, 0, 0, 0, 1, 0, 0, 0, 1)
  }

  case class UniverseSnapshot(actors: Map[ActorId, ActorLike],
                              playerId: Option[ActorId],
                              globalEvents: EventQueue) extends Logging {
    lazy val player = playerId flatMap findActorOfType[Person]

    def updatedActorOfType[T <: ActorLike : ClassTag](id: ActorId)(f: T => T): UniverseSnapshot = {
      findActor(id) match {
        case Some(a: T) =>
          val newActor = f(a)
          if (newActor.id == id)
            copy(actors = actors + (newActor.id -> newActor))
          else
            copy(actors = actors - id + (newActor.id -> newActor))
        case _ => this
      }
    }

    def updatedActor(id: ActorId)(f: ActorLike => ActorLike): UniverseSnapshot = updatedActorOfType[ActorLike](id)(f)

    def findActor(id: ActorId): Option[ActorLike] = actors get id

    def findActorOfType[T <: ActorLike : ClassTag](id: ActorId): Option[T] = findActor(id) flatMap util.castToOption[T]

    def findActors(position: ObjectPosition) = actors filter (_._2.position.objectPosition == position) map (_._2)

    def findActorsOfType[T <: ActorLike : ClassTag](position: ObjectPosition) = findActors(position).flatMap(util.castToOption[T](_))

    def createActorsMap: Map[ObjectPosition, List[ActorLike]] = {
      var map = Map[ObjectPosition, List[ActorLike]]()
      actors foreach {
        case (_id, a) =>
          val pos = a.position.objectPosition
          val objects = map.getOrElse(pos, Nil)
          map = map.updated(pos, objects :+ a)
      }
      map
    }

    def nextEvent: Option[(Double, ScheduledEvent, UniverseSnapshot)] = {
      val delays = (globalEvents.nextEventDelay map ((_, this))).toIndexedSeq ++
        actors.values.flatMap(a => a.nextEventDelay.map((_, a)))
      log.debug(s"Timestamp: ${globalEvents.timestamp}; First event count: ${delays.size}")
      if (delays.isEmpty)
        None
      else
        Some(delays.minBy(_._1) match {
          case (delay, _self: UniverseSnapshot) =>
            val (nextTimestamp, nextAction) = globalEvents.nextEvent.get
            val nextUniverse = copy(
              globalEvents = globalEvents.dropNextEvent().addTime(delay),
              actors = actors.mapValues(_.addTime(delay)))
            log.debug(s"Event from universe, delay=$delay, next timestamp=$nextTimestamp")
            (nextTimestamp, nextAction, nextUniverse)
          case (delay, actor: ActorLike) =>
            val nextTimestamp = globalEvents.timestamp + delay
            val nextAction = actor.eventQueue.nextEvent.get._2
            val nextUniverse = copy(
              globalEvents = globalEvents.addTime(delay),
              actors = actors.mapValues(_.addTime(delay))).updatedActor(actor.id)(_.dropNextEvent())
            log.debug(s"Event from actor, delay=$delay, next timestamp=$nextTimestamp")
            (nextTimestamp, nextAction, nextUniverse)
        })
    }
  }

  sealed class Universe {
    private var _snapshots: Seq[UniverseSnapshot] = Nil

    def snapshots = _snapshots

    private var _current: UniverseSnapshot = _

    def current = _current

    def current_=(state: UniverseSnapshot) = {
      _snapshots :+= state
      _current = state
    }

    private var _messages: List[String] = Nil

    def messages = _messages

    def addMessage(msg: String) = _messages ::= msg
  }

  object Universe {
    def apply(initialState: UniverseSnapshot) = {
      val u = new Universe
      u.current = initialState
      u
    }
  }

}
