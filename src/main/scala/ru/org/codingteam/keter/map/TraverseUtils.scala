package ru.org.codingteam.keter.map

import ru.org.codingteam.keter.game.objects.{Actor, GameObject}
import ru.org.codingteam.keter.map.TraverseUtils.AbstractTraverseMethod.CoordsTuple
import ru.org.codingteam.keter.util.Logging

object TraverseUtils {

  case class BoardCoords(x: Int, y: Int) {
    def move(m: Move) = BoardCoords(x + m.dx, y + m.dy)

    def to(bc: BoardCoords) = Move(bc.x - x, bc.y - y)
  }

  case class BoardCell(coords: BoardCoords,
                       position: ActorPosition,
                       surface: Option[Surface],
                       objects: Seq[GameObject],
                       actors: Seq[Actor],
                       player: Option[Actor])

  abstract class AbstractTraverseMethod {

    def traverse(universe: Universe,
                 from: ActorPosition,
                 topLimit: Int, bottomLimit: Int, leftLimit: Int, rightLimit: Int): Seq[BoardCell] = {
      val currentUniverse = universe.current
      val player = currentUniverse.player
      def inRect(bc: BoardCoords) = bc.x <= rightLimit && bc.x >= leftLimit && bc.y <= bottomLimit && bc.y >= topLimit
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
      val initialPos = CoordsTuple(BoardCoords(0, 0), player.position)
      var acc = Seq(initialPos)
      var res = Seq[CoordsTuple]()
      while (acc.nonEmpty) {
        res ++= acc
        acc = fillStep(acc, inRect)
      }
      res map {
        case CoordsTuple(bc, ap) => BoardCell(
          coords = bc,
          position = ap,
          surface = ap.surfaceAt,
          objects = objectsMap.getOrElse(ap.objectPosition, Nil),
          actors = actorsMap.getOrElse(ap.objectPosition, Nil),
          player = if (ap.objectPosition == player.position.objectPosition) Some(player) else None
        )
      }
    }

    protected def fillStep(cs: Seq[CoordsTuple], inRegion: BoardCoords => Boolean): Seq[CoordsTuple]

  }

  object AbstractTraverseMethod {

    case class CoordsTuple(boardCoords: BoardCoords, actorPosition: ActorPosition)

  }

  object DiagonalTraverseMethod extends AbstractTraverseMethod {
    override protected def fillStep(cs: Seq[CoordsTuple], inRegion: (BoardCoords) => Boolean): Seq[CoordsTuple] = {
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
      cs flatMap { ct =>
        movesFrom(ct.boardCoords).flatMap { m =>
          val newBC = ct.boardCoords.move(m)
          if (inRegion(newBC))
            Some(CoordsTuple(newBC, ct.actorPosition.moveWithJumps(m)))
          else
            None
        }
      }
    }
  }

  object DirectionLookTraverseMethod extends AbstractTraverseMethod with Logging {
    private var movesCache: Map[BoardCoords, Seq[Move]] = Map()

    private def getMovesFrom(bc: BoardCoords): Seq[Move] = {
      movesCache.get(bc) match {
        case Some(ls) => ls
        case None =>
          val level = math.max(math.abs(bc.x), math.abs(bc.y))
          val n = level + 1
          def addToCache(x: Int, y: Int) = {
            val bc = BoardCoords(x, y)
            val tc = bc move toCenter(bc)
            val ls = movesCache.getOrElse(tc, Nil)
            movesCache = movesCache.updated(tc, ls :+ (tc to bc))
          }
          for (y <- Seq(-n, n); x <- -n to n) addToCache(x, y)
          for (x <- Seq(-n, n); y <- -level to level) addToCache(x, y)
          movesCache(bc)
      }
    }

    private def toCenter(p: BoardCoords): Move = {
      assert(!(p.x == 0 && p.y == 0))
      val (ax, ay) = (math.abs(p.x), math.abs(p.y))
      val (dx, dy) = (-math.signum(p.x), -math.signum(p.y))
      if (ax > ay) {
        if (ay.toDouble / ax >= 0.4) Move(dx, dy) else Move(dx, 0)
      } else {
        if (ax.toDouble / ay >= 0.4) Move(dx, dy) else Move(0, dy)
      }
    }

    override protected def fillStep(cs: Seq[CoordsTuple], inRegion: (BoardCoords) => Boolean): Seq[CoordsTuple] = {
      cs flatMap {
        case CoordsTuple(bc@BoardCoords(x, y), ap) =>
          getMovesFrom(bc).flatMap { m =>
            val newBC = bc.move(m)
            if (inRegion(newBC))
              Some(CoordsTuple(newBC, ap.moveWithJumps(m)))
            else
              None
          }
      }
    }
  }

}
