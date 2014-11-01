package ru.org.codingteam.keter.game.objects.equipment

abstract class EquipmentItem {
  def name: String
  def actions: Set[Capability]
}
