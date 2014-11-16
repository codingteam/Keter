package ru.org.codingteam.keter.game.objects.equipment

import ru.org.codingteam.keter.game.objects.ObjectAction

/**
 * Piece of character equipment that is not part of his body.
 */
abstract class EquipmentItem {

  /**
   * Name of equipment item.
   */
  def name: String

  /**
   * Actions that can be performed by the character that has this item equipped.
   */
  def actions: Set[ObjectAction]

  /**
   * Set of prerequisite capabilities required by this item. Item cannot be equipped if not all of its' capabilities
   * were met.
   */
  def requires: Set[Capability]

  /**
   * Set of capabilities provided by this equipment item.
   */
  def provides: Set[Capability]

}
