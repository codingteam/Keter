package ru.org.codingteam.keter.game

import ru.org.codingteam.rotjs.interface.Arena

case class LocationMap(surfaces: Array[Array[Surface]], objects: List[GameObject])

object LocationMap {

  val wall = Surface("wall", "#")
  val floor = Surface("floor", ".")

  def generate() = {
    val map = new Arena(7, 7)
    val surfaces = Array.ofDim[Surface](7, 7)
    map.create({ (x: Int, y: Int, value: Int) =>
      surfaces(x)(y) = value match {
        case 0 => floor
        case 1 => wall
      }
    })

    LocationMap(surfaces, Nil)
  }

}
