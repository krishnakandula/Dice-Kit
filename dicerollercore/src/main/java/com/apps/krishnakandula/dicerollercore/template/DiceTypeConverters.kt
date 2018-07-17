package com.apps.krishnakandula.dicerollercore.template

import android.arch.persistence.room.TypeConverter
import com.apps.krishnakandula.dicerollercore.Dice

class DiceTypeConverters {

    @TypeConverter
    fun fromRolls(rolls: Array<Array<Dice>>): String = rolls.map { fromRoll(it) }.joinToString("|")

    @TypeConverter
    fun fromString(str: String): Array<Array<Dice>> = str.split("|")
                .filter { it.isNotBlank() }
                .map { rollFromStr(it) }
                .toTypedArray()

    private fun rollFromStr(str: String): Array<Dice> = str.split(" ")
            .filter { it.isNotEmpty() }
            .map { fromChar(it)!! }
            .toTypedArray()

    private fun fromRoll(roll: Array<Dice>): String = roll.map { fromDie(it) }.joinToString(" ")

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

    private fun fromDie(die: Dice): String {
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
