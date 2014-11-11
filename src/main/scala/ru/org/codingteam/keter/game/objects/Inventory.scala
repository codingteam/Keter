package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart

case class Inventory(body: Set[Bodypart], equipment: Set[EquipmentItem])
