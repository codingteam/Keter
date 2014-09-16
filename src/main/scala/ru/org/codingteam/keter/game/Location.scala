package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.actions._
import ru.org.codingteam.keter.game.objects.behaviors.{PlayerBehavior, RandomBehavior}
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.{Surface, _}
import ru.org.codingteam.keter.map._

object Location {

  val foundation = Faction("SCP Foundation")
  val monsters = Faction("GOC")

  val map1 =
    """
      |###########################
      |#.........................#
      |#.........................#
      |#.........................#
      |#...................#.....#
      |#...................#.....#
      |#...................#.....#
      |#...................#######
      |#.........................#
      |#.....................#####
      |#######################
      | """.stripMargin.trim

  def submapFromString(definition: String) = new Submap(
    definition.split('\n') map {
      row => row map {
        case '#' => Some(Wall())
        case '.' => Some(Floor())
        case _ => None
      }
    })


  def createLocation(): UniverseSnapshot = {
    val submap1 = submapFromString(map1)
    val playerId = ActorId()
    val player = human(
      new PlayerBehavior,
      foundation,
      "Dr. Növer",
      "@",
      playerId,
      ActorPosition(
        submap = submap1,
        coords = ActorCoords(2, 3),
        subspaceMatrix = SubspaceMatrix.identity)
    )
    val scp = human(
      RandomBehavior,
      monsters,
      "Unknown SCP",
      "s",
      ActorId(),
      ActorPosition(
        submap = submap1,
        coords = ActorCoords(8, 9),
        subspaceMatrix = SubspaceMatrix.identity)
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
    UniverseSnapshot(
      actors = Seq(player, scp),
      playerId = playerId,
      timestamp = 0L,
      objects = Map(ObjectPosition(submap1, ObjectCoords(5, 3)) -> List(door)))
  }

  def human(behavior: IActorBehavior,
            faction: Faction,
            name: String,
            tile: String,
            id: ActorId,
            position: ActorPosition): Actor = {
    val legs = EquipmentItem("Legs", Seq(WalkCapability))
    val hands = EquipmentItem("Hands", Seq(MeleeAttackCapability))
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
