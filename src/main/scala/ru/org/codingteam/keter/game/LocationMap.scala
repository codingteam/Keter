package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.game.objects.behaviors.{PlayerBehavior, RandomBehavior}
import ru.org.codingteam.rotjs.interface.Arena

case class LocationMap(surfaces: Array[Array[Surface]], actors: Map[ActorId, Actor], playerId: ActorId) {

  def surfaceAt(x: Int, y: Int): Option[Surface] = {
    (x, y) match {
      case _ if y < 0 || x < 0 => None
      case _ if y >= surfaces.length => None
      case _ if x >= surfaces(y).length => None
      case _ => Some(surfaces(y)(x))
    }
  }

  def player = actors(playerId)

}

object LocationMap {

  val wall = Surface("wall", "#", passable = false)
  val floor = Surface("floor", ".", passable = true)

  val foundation = Faction("SCP Foundation")
  val monsters = Faction("GOC")

  def generate() = {
    val map = new Arena(7, 7)
    val surfaces = Array.ofDim[Surface](7, 7)
    map.create { (x: Int, y: Int, value: Int) =>
      surfaces(x)(y) = value match {
        case 0 => floor
        case 1 => wall
      }
    }

    val player = Actor(
      ActorId(),
      foundation,
      "Dr. Növer",
      "@",
      ActorActive,
      new PlayerBehavior,
      StatTable(health = 100),
      ObjectPosition(2, 2)
    )

    val scp = Actor(
      ActorId(),
      monsters,
      "Unknown SCP",
      "s",
      ActorActive,
      RandomBehavior,
      StatTable(health = 100),
      ObjectPosition(5, 5)
    )

    val actors = List(player, scp).map(actor => (actor.id, actor)).toMap
    LocationMap(surfaces, actors, player.id)
  }

}
