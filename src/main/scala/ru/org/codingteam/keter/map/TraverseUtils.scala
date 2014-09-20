package ru.org.codingteam.keter.map

import ru.org.codingteam.keter.game.objects.{Actor, GameObject}

object TraverseUtils {

  case class BoardCoords(x: Int, y: Int) {
    def move(m: Move) = BoardCoords(x + m.dx, y + m.dy)
  }

  case class BoardCell(coords: BoardCoords,
                       position: ActorPosition,
                       surface: Option[Surface],
                       objects: Seq[GameObject],
                       actors: Seq[Actor],
                       player: Option[Actor])

  def traverseUniverse(universe: Universe, from: ActorPosition, top: Int, bottom: Int, left: Int, right: Int): Seq[BoardCell] = {
    val currentUniverse = universe.current
    val player = currentUniverse.player

    def inRect(bc: BoardCoords) = bc.x <= right && bc.x >= left && bc.y <= bottom && bc.y >= top
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
        if (inRect(newBC))
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
}
