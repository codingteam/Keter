package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.equipment.WalkCapability
import ru.org.codingteam.keter.game.objects.{ActorId, GenericObjectAction, Person}
import ru.org.codingteam.keter.map.{Move, UniverseSnapshot}

case class WalkAction(move: Move) extends GenericObjectAction {

  override def description: String = "walk"

  override def requires = Set(WalkCapability)

  override def perform(invokerId: ActorId,
                       universe: UniverseSnapshot): UniverseSnapshot = Person.ifActorIsActive(invokerId, universe) {
    universe.updatedActorOfType[Person](invokerId) { p =>
      if (move.length == 0)
        p.withSheduledNextEventAfter(100)
      else {
        val newPosition = p.position.moveWithJumps(move)
        newPosition.surfaceAt match {
          case Some(s) if s.passable =>
            p.withPosition(newPosition).withSheduledNextEventAfter(move.length * 250)
          case _ =>
            p.withSheduledNextEventAfter(100)
        }
      }
    }
  }
}
