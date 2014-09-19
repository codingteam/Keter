/*

package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.actions.{MeleeAttackAction, WalkAction}
import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.game.objects.behaviors.{PlayerBehavior, RandomBehavior}
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.map.ObjectPosition
import ru.org.codingteam.keter.util.Logging
import ru.org.codingteam.rotjs.interface.Arena

case class LocationMap(surfaces: Array[Array[Surface]],
                       actors: Map[ActorId, Actor],
                       objects: Array[Array[List[GameObject]]],
                       playerId: ActorId) extends Logging {

  def surfaceAt(x: Int, y: Int): Option[Surface] = {
    (x, y) match {
      case _ if y < 0 || x < 0 => None
      case _ if y >= surfaces.length => None
      case _ if x >= surfaces(y).length => None
      case _ => Some(surfaces(y)(x))
    }
  }

  def actorAt(position: ObjectPosition): Option[Actor] = {
    actors.values.find(a => a.position == position)
  }

  def firstObjectAt(x: Int, y: Int): Option[GameObject] = {
    (x, y) match {
      case _ if y < 0 || x < 0 => None
      case _ if y >= objects.length => None
      case _ if x >= objects(y).length => None
      case _ if objects(y)(x).length == 0 => None
      case _ => Some(objects(y)(x)(0))
    }
  }

  def player = actors(playerId)

}

object LocationMap {

  val wall = Surface("wall", "#", passable = false)
  val floor = Surface("floor", ".", passable = true)

  val foundation = Faction("SCP Foundation")
  val monsters = Faction("GOC")

  val xDim = 7
  val yDim = 7

  def generate() = {
    val map = new Arena(xDim, yDim)
    val surfaces = Array.ofDim[Surface](xDim, yDim)
    map.create { (x: Int, y: Int, value: Int) =>
      surfaces(x)(y) = value match {
        case 0 => floor
        case 1 => wall
      }
    }

    val player = human(
      new PlayerBehavior,
      foundation,
      "Dr. Növer",
      "@",
      ActorId(),
      ObjectPosition(2, 2)
    )

    val scp = human(
      RandomBehavior,
      monsters,
      "Unknown SCP",
      "s",
      ActorId(),
      ObjectPosition(5, 5)
    )

    val door = Door(
      ActorId(),
      "door",
      "▯",
      false,
      false,
      "|",
      "▯"
    )

    val actors = List(player, scp).map(actor => (actor.id, actor)).toMap
    val objects = Array.fill[List[GameObject]](xDim,yDim)(Nil)
    objects(3)(4) =  door :: objects(3)(4)
    LocationMap(surfaces, actors, objects, player.id)
  }

  def human(behavior: IActorBehavior,
            faction: Faction,
            name: String,
            tile: String,
            id: ActorId,
            position: ObjectPosition): Actor = {
    val legs = EquipmentItem("Legs", Vector(WalkAction))
    val hands = EquipmentItem("Hands", Vector(MeleeAttackAction))

    Actor(
      id,
      faction,
      name,
      tile,
      ActorActive,
      behavior,
      StatTable(health = 100),
      Vector(legs, hands),
      position
    )
  }

}
*/