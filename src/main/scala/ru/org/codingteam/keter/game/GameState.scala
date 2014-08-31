package ru.org.codingteam.keter.game

class GameState(var messages: Vector[String], var map: LocationMap, var time: Long)

object GameState {

  def apply(): GameState = {
    new GameState(Vector(), LocationMap(Array.ofDim(0, 0), Map()), 0)
  }

}