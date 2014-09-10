package ru.org.codingteam.keter.game.objects.equipment

import ru.org.codingteam.keter.game.actions.IActionDefinition

case class EquipmentItem(name: String, actions: Seq[IActionDefinition])
