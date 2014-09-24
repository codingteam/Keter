package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.game.objects.behaviors.{PlayerBehavior, RandomBehavior}
import ru.org.codingteam.keter.game.objects.equipment.bodyparts._
import ru.org.codingteam.keter.game.objects.equipment.{EquipmentItem, MeleeAttackCapability, WalkCapability}
import ru.org.codingteam.keter.map._
import ru.org.codingteam.keter.util.Logging

object Location extends Logging {

  val foundation = Faction("SCP Foundation")
  val monsters = Faction("GOC")

  val mapDef1 =
    """
      |###########################
      |#.........................#
      |#.........................#
      |#.........................#
      |#...................#.....#
      |#...................#.....#
      |#...................#.....#
      |#...................#######
      |#......................>..#
      |#.....................#####
      |#######################
      | """.stripMargin.trim

  val mapDef2 =
    """
      |###########################
      |#...................#...>.#
      |#...................#.....#
      |#...................#.....#
      |#......######..######.....#
      |#......#..................#
      |#......#..................#
      |#......#............#######
      |#......#..................#
      |#......#..................#
      |###########################
      | """.stripMargin.trim

  def submapFromString(definition: String, jumps: Map[(Int, Int), Jump] = Map()) = {
    val rows = definition.split('\n')
    new Submap(
      for (y <- rows.indices; row = rows(y)) yield
        for (x <- row.indices; c = row(x)) yield
          c match {
            case '#' => Some(Wall())
            case '.' => Some(Floor())
            case '>' =>
              log.debug(s"Submap build: jump at ($y,$x).")
              jumps.get((x, y))
            case _ => None
          })
  }

  def createLocation(): UniverseSnapshot = {

    lazy val submap1: Submap = submapFromString(mapDef1, Map((23, 8) -> jump1))
    lazy val submap2: Submap = submapFromString(mapDef2, Map((24, 1) -> jump2))
    lazy val jump1: Jump = Jump(_ => submap2, c => ActorCoords(3, 8, c.t), identity)
    lazy val jump2: Jump = Jump(_ => submap1, c => ActorCoords(23, 6, c.t), _ * SubspaceMatrix(-1, 0, 0, 0, 1, 0, 0, 0, 1))

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
            position: ActorPosition,
            bodyparts: Set[Bodypart] = Set[Bodypart](
              Leg("left leg", 75.0),
              Leg("right leg", 75.0),
              Arm("left arm", 50.0),
              Arm("right arm", 50.0),
              Head("head", 75.0),
              Torso("torso", 100.0)
              )
            ): Actor = {
      Actor(id,
           faction,
           name,
           tile,
           ActorActive,
           behavior,
           StatTable(health = 100),
           Seq[EquipmentItem](),
           position: ActorPosition,
           bodyparts: Set[Bodypart]
     )
  }
}
