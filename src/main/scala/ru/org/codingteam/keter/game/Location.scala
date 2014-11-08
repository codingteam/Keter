package ru.org.codingteam.keter.game

import ru.org.codingteam.keter.game.LocationUtils.SubmapSkeleton
import ru.org.codingteam.keter.game.objects._
import ru.org.codingteam.keter.game.objects.behaviors.{PlayerBehavior, RandomBehavior}
import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.equipment.bodyparts._
import ru.org.codingteam.keter.game.objects.equipment.items.Knife
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
      |#.......O.................#
      |#...................#.....#
      |#...@...............#...b.#
      |#..............s....#.....#
      |#...................#######
      |#......................A..#
      |#......d..............#####
      |#######C###############
      | """.stripMargin.trim

  val mapDef2 =
    """
      |###########################
      |#...................#...B.#
      |#...................#.....#
      |#...................#.....#
      |#......######..######.....#
      |#......#..................#
      |#......#..................#
      |#......#............#######
      |#...a..#..................#
      |#......#..................#
      |###########################
      | """.stripMargin.trim

  val mapDef3 =
    """
      |####D#############
      |Fe..c...........fE
      |#..#..##..###....#
      |####...####......#
      |#......#..###..###
      |#....J.#.........#
      |#......#...#######
      |#......#...#
      |############
      | """.stripMargin.trim

  def createLocation(): UniverseSnapshot = {

    lazy val skel1 = new SubmapSkeleton(mapDef1) replacing('A', 'C', 'b', 'd', '@', 's', 'O') `with` '.'
    lazy val skel2 = new SubmapSkeleton(mapDef2) replacing('B', 'a') `with` '.'
    lazy val skel3 = new SubmapSkeleton(mapDef3) replacing('D', 'E', 'F', 'c', 'f', 'e', 'J') `with` '.'

    lazy val submap1: Submap = skel1.buildSubmap(Map(
      skel1.coordsOf('A') -> jump11,
      skel1.coordsOf('C') -> jump12))
    lazy val submap2: Submap = skel2.buildSubmap(Map(
      skel2.coordsOf('B') -> jump21))
    lazy val submap3: Submap = skel3.buildSubmap(Map(
      skel3.coordsOf('D') -> jump31,
      skel3.coordsOf('E') -> jump32,
      skel3.coordsOf('F') -> jump33,
      skel3.coordsOf('J') -> jump34))

    import ru.org.codingteam.keter.game.LocationUtils.{TupleExt, coordFuncFromXY}

    lazy val jump11: Jump = Jump(_ => submap2, coordFuncFromXY(skel2 coordsOf 'a'))
    lazy val jump21: Jump = Jump(_ => submap1, coordFuncFromXY(skel1 coordsOf 'b'), _ * SubspaceMatrix(-1, 0, 0, 0, 1, 0, 0, 0, 1))
    lazy val jump12: Jump = Jump(_ => submap3, coordFuncFromXY(skel3 coordsOf 'c'))
    lazy val jump31: Jump = Jump(_ => submap1, coordFuncFromXY(skel1 coordsOf 'd'))
    lazy val jump32: Jump = Jump(coordsFunc = coordFuncFromXY(skel3 coordsOf 'e'), matrixFunc = _ * SubspaceMatrix(1, 0, 0, 0, 1, 0, 0, 0, 2))
    lazy val jump33: Jump = Jump(coordsFunc = coordFuncFromXY(skel3 coordsOf 'f'), matrixFunc = _ * SubspaceMatrix(1, 0, 0, 0, 1, 0, 0, 0, 0.5))
    lazy val jump34: Jump = Jump(coordsFunc = _ + Move(-1, 0))

    val playerId = ActorId()
    var player = human(
      new PlayerBehavior,
      foundation,
      "Dr. Növer",
      "@",
      playerId,
      ActorPosition(
        submap = submap1,
        coords = skel1.coordsOf('@').coords,
        subspaceMatrix = SubspaceMatrix.identity)
    )
    player = player.copy(equipment = player.equipment :+ Knife("Knife"))

    val scp = human(
      RandomBehavior,
      monsters,
      "Unknown SCP",
      "s",
      ActorId(),
      ActorPosition(
        submap = submap1,
        coords = skel1.coordsOf('s').coords,
        subspaceMatrix = SubspaceMatrix.identity)
    )
    val door = Door(
      ActorId(),
      "door",
      open = false,
      "|",
      "▯",
      position = ActorPosition(submap1, skel1.coordsOf('O').coords)
    )

    UniverseSnapshot(
      actors = Seq(player, scp, door).map(a => (a.id, a)).toMap,
      playerId = Some(playerId),
      globalEvents = EventQueue.empty)
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
              Torso("torso", 100.0))): Person = {
    Person(id,
      faction,
      name,
      tile = Some(tile),
      ActorActive,
      behavior,
      StatTable(health = 100),
      Seq[EquipmentItem](),
      position: ActorPosition,
      bodyparts: Set[Bodypart]
    ).withSheduledNextEventAfter(100)
  }
}
