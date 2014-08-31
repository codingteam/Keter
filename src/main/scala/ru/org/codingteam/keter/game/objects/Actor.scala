package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.{Faction, GameState}

case class Actor(faction: Faction,
                 name: String,
                 tile: String,
                 state: ActorState,
                 behavior: ActorBehavior,
                 stats: StatTable,
                 position: ObjectPosition) extends GameObject {

  def getNextAction(state: GameState) = behavior.getNextAction(this, state)

}
