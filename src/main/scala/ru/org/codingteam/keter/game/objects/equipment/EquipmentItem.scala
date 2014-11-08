package ru.org.codingteam.keter.game.objects.equipment

import ru.org.codingteam.keter.game.objects.ObjectAction

abstract class EquipmentItem {
  def name: String

  def actions: Set[ObjectAction]
}
