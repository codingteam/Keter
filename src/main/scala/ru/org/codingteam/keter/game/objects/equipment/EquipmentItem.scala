package ru.org.codingteam.keter.game.objects.equipment

import ru.org.codingteam.keter.game.actions.Capability

case class EquipmentItem(name: String, actions: Seq[Capability])
