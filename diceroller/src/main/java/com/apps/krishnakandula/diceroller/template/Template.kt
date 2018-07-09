package com.apps.krishnakandula.diceroller.template

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.apps.krishnakandula.diceroller.Dice

@Entity(tableName = TemplateSchema.TABLE_NAME)
data class Template(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = TemplateSchema.Cols.ID) var id: Long? = null,
                    @ColumnInfo(name = TemplateSchema.Cols.NAME) var name: String = "",
                    @ColumnInfo(name = TemplateSchema.Cols.ROLLS)
                    @TypeConverters(DiceTypeConverters::class) var rolls: Array<Dice> = emptyArray())

object TemplateSchema {
    const val TABLE_NAME = "template"

    object Cols {
        const val ID = "id"
        const val NAME = "name"
        const val ROLLS = "rolls"
    }
}
