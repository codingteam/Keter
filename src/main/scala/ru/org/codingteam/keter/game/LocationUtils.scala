package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.map._

object LocationUtils {

  class SubmapSkeleton private[LocationUtils](val data: IndexedSeq[IndexedSeq[Char]], val replacements: Map[Char, Char] = Map()) {

    def this(definition: String) = this(definition.split("\n").map(_.toIndexedSeq))

    val excludeFromSearch = Set(' ', '#', '.')

    val symbolMap = (for (y <- data.indices; row = data(y);
                          x <- row.indices; c = row(x);
                          if !excludeFromSearch.contains(c)) yield (c, (x, y))).toMap

    def coordsOf(sym: Char): (Int, Int) = symbolMap(sym)

    def buildSubmap(jumps: Map[(Int, Int), Jump] = Map()) = new Submap(
      for (y <- data.indices; row = data(y)) yield
        for (x <- row.indices; c = row(x)) yield
          jumps.get((x, y)).orElse(replacements.getOrElse(c, c) match {
            case '#' => Some(Wall())
            case '.' => Some(Floor())
            case _ => None
          }))

    def replacing(syms: Char*) = new Replacement(this, syms)
  }

  sealed class Replacement(skel: SubmapSkeleton, syms: Seq[Char]) {
    def `with`(sym: Char) = new SubmapSkeleton(skel.data, skel.replacements ++ (syms map ((_, sym))))
  }

  def coordFuncFromXY(xy: (Int, Int)): ActorCoords => ActorCoords = _.copy(x = xy._1, y = xy._2)

  implicit class TupleExt(val xy: (Int, Int)) extends AnyVal {
    def coords: ActorCoords = ActorCoords(xy._1, xy._2)
  }

}
