package ru.org.codingteam.keter.game.objects

import ru.org.codingteam.keter.game.objects.equipment.EquipmentItem
import ru.org.codingteam.keter.game.objects.equipment.bodyparts.Bodypart

/**
 * Person inventory.
 * @param body body parts.
 * @param equipment equipped items.
 * @param backpack items not equipped.
 */
case class Inventory(body: Set[Bodypart], equipment: Set[EquipmentItem], backpack: Set[EquipmentItem])
