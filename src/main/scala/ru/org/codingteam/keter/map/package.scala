package ru.org.codingteam.keter

import ru.org.codingteam.keter.game.objects.{Actor, ActorId, GameObject}

import scala.annotation.tailrec

package object map {

  case class ActorPosition(submap: Submap, coords: ActorCoords, subspaceMatrix: SubspaceMatrix) {
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

  case class Jump(submapFunc: Submap => Submap,
                  coordsFunc: ActorCoords => ActorCoords,
                  matrixFunc: SubspaceMatrix => SubspaceMatrix) extends Surface(name = "Jump", tile = ">", passable = true)

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
  }

  object SubspaceMatrix {
    lazy val identity = SubspaceMatrix(1, 0, 0, 0, 1, 0, 0, 0, 1)
  }

  case class UniverseSnapshot(timestamp: Long,
                              actors: Seq[Actor],
                              playerId: ActorId,
                              objects: Map[ObjectPosition, List[GameObject]]) {
    lazy val player = findActor(playerId).getOrElse {
      throw new IllegalStateException("actor with playerId must exist")
    }

    def updatedObjects(pos: ObjectPosition)(f: List[GameObject] => List[GameObject]): UniverseSnapshot = {
      copy(objects = objects.updated(pos, f(objects.getOrElse(pos, Nil))))
    }

    def updatedActor(id: ActorId)(f: Actor => Actor): UniverseSnapshot = {
      copy(actors = actors map (a => if (a.id == id) f(a) else a))
    }

    def updatedTimestamp(f: Long => Long): UniverseSnapshot = {
      val newTime = f(timestamp)
      copy(timestamp = newTime, actors = actors map (a => a.copy(position = a.position.withUpdatedTime(_ => newTime.toInt))))
    }

    def findActor(id: ActorId): Option[Actor] = actors find (_.id == id)

    def findActors(position: ObjectPosition) = actors filter (_.position.objectPosition == position)
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

  object RenderUtils {
    def renderToSeq(universe: Universe, top: Int, bottom: Int, left: Int, right: Int): Seq[(Int, Int, String)] = {
      val currentUniverse = universe.current
      val player = currentUniverse.player
      case class BoardCoords(x: Int, y: Int) {
        def inRect = x <= right && x >= left && y <= bottom && y >= top

        def move(m: Move) = BoardCoords(x + m.dx, y + m.dy)
      }
      case class CoordsTuple(boardCoords: BoardCoords, actorPosition: ActorPosition)
      val actorsMap = {
        var map = Map[ObjectPosition, List[Actor]]()
        currentUniverse.actors foreach {
          a =>
            val pos = a.position.objectPosition
            val objects = map.getOrElse(pos, Nil)
            map = map.updated(pos, objects :+ a)
        }
        map
      }
      val objectsMap = currentUniverse.objects

      def movesFrom(p: BoardCoords): Seq[Move] = {
        val (dx, dy) = (math.signum(p.x), math.signum(p.y))
        val xyDir = math.abs(p.x) - math.abs(p.y)
        if (dx == 0 && dy == 0)
          for (xx <- -1 to 1; yy <- -1 to 1; if !(xx == 0 && yy == 0)) yield
            Move(xx, yy)
        else if (xyDir == 0)
          Seq(Move(dx, dy), Move(dx, 0), Move(0, dy))
        else
          Seq(if (xyDir > 0) Move(dx, 0) else Move(0, dy))
      }

      def fillStep(cs: Seq[CoordsTuple]): Seq[CoordsTuple] = cs flatMap { ct =>
        movesFrom(ct.boardCoords).flatMap { m =>
          val newBC = ct.boardCoords.move(m)
          if (newBC.inRect)
            Some(CoordsTuple(newBC, ct.actorPosition.moveWithJumps(m)))
          else
            None
        }
      }

      val initialPos = CoordsTuple(BoardCoords(0, 0), player.position)
      var acc = Seq(initialPos)
      var res = Seq[CoordsTuple]()
      while (acc.nonEmpty) {
        res ++= acc
        acc = fillStep(acc)
      }
      res.flatMap {
        case CoordsTuple(BoardCoords(x, y), ap) =>
          lazy val surfaceTile = ap.surfaceAt map (_.tile)
          lazy val objectTile = objectsMap.getOrElse(ap.objectPosition, Nil).headOption map (_.tile)
          lazy val actorTile = actorsMap.getOrElse(ap.objectPosition, Nil).headOption map (_.tile)
          lazy val playerTile = if (ap.objectPosition == player.position.objectPosition) Some(player.tile) else None
          def next[A](a: Option[A], next: => Option[A]) = if (a.isDefined) a else next
          val anyTile = next(playerTile, next(actorTile, next(objectTile, next(surfaceTile, None))))
          anyTile map ((x, y, _))
      }
    }
  }

}
