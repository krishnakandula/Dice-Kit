package com.apps.krishnakandula.dicerollercore.template

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.apps.krishnakandula.dicerollercore.Dice

@Entity(tableName = TemplateSchema.TABLE_NAME)
data class Template(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = TemplateSchema.Cols.ID) var id: Long? = null,
                    @ColumnInfo(name = TemplateSchema.Cols.NAME) var name: String = "",
                    @ColumnInfo(name = TemplateSchema.Cols.ROLLS)
                    @TypeConverters(DiceTypeConverters::class) var rolls: Array<Array<Dice>> = emptyArray()) {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Template) return false
        if (other.id == null && this.id != null) return false

        return other.id == this.id
                && other.name == this.name
                && checkRollsEqual(this.rolls, other.rolls)
    }

    private fun checkRollsEqual(rolls1: Array<Array<Dice>>, rolls2: Array<Array<Dice>>): Boolean {
        if (rolls1.size != rolls2.size) return false
        rolls1.forEachIndexed { index, dice ->
            val dice2 = rolls2[index]
            if (dice2.size != dice.size) return false
            dice.forEachIndexed { i, die ->
                if (dice2[i] != die) return false
            }
        }

        return true
    }
}

object TemplateSchema {
    const val TABLE_NAME = "template"

    object Cols {
        const val ID = "id"
        const val NAME = "name"
        const val ROLLS = "rolls"
    }
}
