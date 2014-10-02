package ru.org.codingteam.keter.map

import ru.org.codingteam.keter.game.objects.{Actor, GameObject}

import scala.annotation.tailrec

object TraverseUtils {

  case class BoardCoords(x: Int, y: Int) {
    def move(m: Move) = BoardCoords(x + m.dx, y + m.dy)

    def to(bc: BoardCoords) = Move(bc.x - x, bc.y - y)

    def atCenter = x == 0 && y == 0
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
      val dirMap = Array.ofDim[ActorPosition](bottomLimit - topLimit + 1, rightLimit - leftLimit + 1,
        Seq(topLimit, bottomLimit, leftLimit, rightLimit).map(_.abs).max + 1)
      def getDirectionCache(bc: BoardCoords): Array[ActorPosition] = {
        val newBc = {
          @tailrec def nod(a: Int, b: Int): Int = a % b match {
            case 0 => b
            case q => nod(b, q)
          }
          @inline def nodChecked(a: Int, b: Int) = if (a > b) nod(b, a) else nod(a, b)
          bc match {
            case BoardCoords(x, y) if !(x == 0 && y == 0) =>
              val q = nodChecked(math abs x, math abs y)
              BoardCoords(x / q, y / q)
            case _ => bc
          }
        }
        dirMap(newBc.y - topLimit)(newBc.x - leftLimit)
      }
      @inline def getCacheIndex(bc: BoardCoords) = math.max(math abs bc.x, math abs bc.y)
      def computeAP(bc: BoardCoords): ActorPosition = {
        val zMax = getCacheIndex(bc)
        val dir = getDirectionCache(bc)
        @inline def prevBC(z: Int) = BoardCoords((z.toDouble / zMax * bc.x).round.toInt, (z.toDouble / zMax * bc.y).round.toInt)
        def compute(z: Int, bc: BoardCoords): ActorPosition = {
          if (z == 0)
            from
          else
            dir(z) match {
              case null =>
                val pbc = prevBC(z - 1)
                val pap = compute(z - 1, pbc)
                val move = pbc to bc
                val ap = pap moveWithJumps move
                dir(z) = ap
                ap
              case ap =>
                ap
            }
        }
        compute(zMax, bc)
      }

      val cts = for (y <- topLimit to bottomLimit; x <- leftLimit to rightLimit; bc = BoardCoords(x, y)) yield
        CoordsTuple(bc, computeAP(bc))

      coordsTuplesToBoardCells(universe.current, cts)
    }
  }

  object DirectionLimitedLookTraverseMethod extends TraverseMethod {

    override def traverse(universe: Universe,
                          from: ActorPosition,
                          topLimit: Int, bottomLimit: Int, leftLimit: Int, rightLimit: Int): Seq[BoardCell] = {
      val dirMap = Array.ofDim[Option[ActorPosition]](bottomLimit - topLimit + 1, rightLimit - leftLimit + 1,
        Seq(topLimit, bottomLimit, leftLimit, rightLimit).map(_.abs).max + 1)
      def getDirectionCache(bc: BoardCoords): Array[Option[ActorPosition]] = {
        val newBc = {
          @tailrec def nod(a: Int, b: Int): Int = a % b match {
            case 0 => b
            case q => nod(b, q)
          }
          @inline def nodChecked(a: Int, b: Int) = if (a > b) nod(b, a) else nod(a, b)
          bc match {
            case BoardCoords(x, y) if !(x == 0 && y == 0) =>
              val q = nodChecked(math abs x, math abs y)
              BoardCoords(x / q, y / q)
            case _ => bc
          }
        }
        dirMap(newBc.y - topLimit)(newBc.x - leftLimit)
      }
      @inline def getCacheIndex(bc: BoardCoords) = math.max(math abs bc.x, math abs bc.y)
      def computeAP(bc: BoardCoords): Option[ActorPosition] = {
        val zMax = getCacheIndex(bc)
        val dir = getDirectionCache(bc)
        @inline def prevBC(z: Int) = BoardCoords((z.toDouble / zMax * bc.x).round.toInt, (z.toDouble / zMax * bc.y).round.toInt)
        def compute(z: Int, bc: BoardCoords): Option[ActorPosition] = {
          if (z == 0)
            Some(from)
          else
            dir(z) match {
              case null =>
                val pbc = prevBC(z - 1)
                val pap = compute(z - 1, pbc)
                val move = pbc to bc
                val ap = pap match {
                  case Some(p) if p.surfaceAt.isDefined && p.surfaceAt.get.passable =>
                    Some(p moveWithJumps move)
                  case _ => None
                }
                dir(z) = ap
                ap
              case ap =>
                ap
            }
        }
        compute(zMax, bc)
      }

      val cts = for (y <- topLimit to bottomLimit; x <- leftLimit to rightLimit; bc = BoardCoords(x, y); ap <- computeAP(bc)) yield
        CoordsTuple(bc, ap)

      coordsTuplesToBoardCells(universe.current, cts)
    }
  }

}
