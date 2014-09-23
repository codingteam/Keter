package ru.org.codingteam.keter.map

import ru.org.codingteam.keter.game.objects.{Actor, GameObject}

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

  case class CoordsTuple(boardCoords: BoardCoords, actorPosition: ActorPosition)

  def coordsTuplesToBoardCells(state: UniverseSnapshot, cts: Seq[CoordsTuple]): Seq[BoardCell] = {
    val actorsMap = state.createActorsMap
    val objectsMap = state.objects
    val player = state.player
    cts map {
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

  def createRectLimitsCheck(topLimit: Int, bottomLimit: Int, leftLimit: Int, rightLimit: Int) =
    (bc: BoardCoords) => bc.x <= rightLimit && bc.x >= leftLimit && bc.y <= bottomLimit && bc.y >= topLimit

  trait TraverseMethod {

    def traverse(universe: Universe,
                 from: ActorPosition,
                 topLimit: Int, bottomLimit: Int, leftLimit: Int, rightLimit: Int): Seq[BoardCell]
  }

  object DiagonalTraverseMethod extends TraverseMethod {
    override def traverse(universe: Universe,
                          from: ActorPosition,
                          topLimit: Int, bottomLimit: Int, leftLimit: Int, rightLimit: Int): Seq[BoardCell] = {
      val currentUniverse = universe.current
      val player = currentUniverse.player
      val inRect = createRectLimitsCheck(topLimit, bottomLimit, leftLimit, rightLimit)
      def fillStep(cs: Seq[CoordsTuple]): Seq[CoordsTuple] = {
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
            if (inRect(newBC))
              Some(CoordsTuple(newBC, ct.actorPosition.moveWithJumps(m)))
            else
              None
          }
        }
      }
      val initialPos = CoordsTuple(BoardCoords(0, 0), player.position)
      var acc = Seq(initialPos)
      var res = Seq[CoordsTuple]()
      while (acc.nonEmpty) {
        res ++= acc
        acc = fillStep(acc)
      }
      coordsTuplesToBoardCells(currentUniverse, res)
    }
  }

  object DirectionLookTraverseMethod extends TraverseMethod {

    override def traverse(universe: Universe,
                          from: ActorPosition,
                          topLimit: Int, bottomLimit: Int, leftLimit: Int, rightLimit: Int): Seq[BoardCell] = {
      // TODO: computational complexity is O(N^3) now, and it should be improved by dynamic programming...
      val cts = for (y <- topLimit to bottomLimit; x <- leftLimit to rightLimit) yield {
        val (ax, ay) = (math abs x, math abs y)
        val (sx, sy) = (math signum x, math signum y)
        val cs = if (ax >= ay)
          for (i <- 0 to ax; xx = i * sx; yy = (i * sy * ay.toDouble / ax).round.toInt) yield BoardCoords(xx, yy)
        else
          for (i <- 0 to ay; yy = i * sy; xx = (i * sx * ax.toDouble / ay).round.toInt) yield BoardCoords(xx, yy)
        val mvs = cs zip cs.tail map { case (f, t) => f to t}
        val ap = mvs.foldLeft(from)(_ moveWithJumps _)
        CoordsTuple(BoardCoords(x, y), ap)
      }
      coordsTuplesToBoardCells(universe.current, cts)
    }
  }

}
