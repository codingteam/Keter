package ru.org.codingteam.keter.game.objects.equipment

object EquipmentCategory extends Enumeration {
  type EquipmentCategory = Value
  val All, Hat, Weapon = Value

  implicit class RichEquipmentCategory(v: EquipmentCategory) {

    /**
     * Checks whether this equipment category is greater or equal than other.
     * @param other category to be contained.
     */
    def contains(other: EquipmentCategory) = v == All || v == other
  }
}
