package ru.org.codingteam.keter.game.objects

case class ObjectPosition(x: Int, y: Int) {

  def -(other: ObjectPosition): Double = {
    Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2))
  }

  def +(other: ObjectPosition): ObjectPosition = {
    ObjectPosition(x + other.x, y + other.y)
  }

}