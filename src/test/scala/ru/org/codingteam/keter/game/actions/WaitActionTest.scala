package ru.org.codingteam.keter.game.actions

import ru.org.codingteam.keter.game.objects.{EventQueue, ActorId}
import ru.org.codingteam.keter.map.UniverseSnapshot
import utest._

object WaitActionTest extends TestSuite{
  val playerId = ActorId(1L)
  val universe = UniverseSnapshot(Map(), Some(playerId), EventQueue.empty)
  val action = WaitAction(50.0)

  val tests = TestSuite {
    'shouldWait {
      val newState = action.perform(playerId, universe)
      assert(newState == universe)
    }
  }
}