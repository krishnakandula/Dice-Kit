package com.apps.krishnakandula.diceroller.template

import android.arch.persistence.room.TypeConverter
import com.apps.krishnakandula.diceroller.Dice

class DiceTypeConverters {

    @TypeConverter
    fun fromString(str: String): Array<Dice> = str.split(" ")
            .filter { it.isNotEmpty() }
            .map { fromChar(it)!! }
            .toTypedArray()

    @TypeConverter
    fun toString(dice: Array<Dice>): String = dice.map { fromDice(it) }.joinToString { " $it" }

    private fun fromChar(str: String): Dice? {
        return when (str) {
            "2" -> Dice.D2()
            "4" -> Dice.D4()
            "6" -> Dice.D6()
            "8" -> Dice.D8()
            "10" -> Dice.D10()
            "20" -> Dice.D20()
            else -> null
        }
    }

    private fun fromDice(die: Dice): String {
        return when (die) {
            is Dice.D2 -> "2"
            is Dice.D4 -> "4"
            is Dice.D6 -> "6"
            is Dice.D8 -> "8"
            is Dice.D10 -> "10"
            is Dice.D20 -> "20"
        }
    }

}
