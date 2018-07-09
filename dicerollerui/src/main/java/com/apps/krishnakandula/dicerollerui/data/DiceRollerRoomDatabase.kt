package com.apps.krishnakandula.dicerollerui.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.apps.krishnakandula.diceroller.template.DiceTypeConverters
import com.apps.krishnakandula.diceroller.template.Template
import com.apps.krishnakandula.diceroller.template.room.RoomTemplateRepository

@Database(entities = [Template::class], version = 1)
@TypeConverters(DiceTypeConverters::class)
abstract class DiceRollerRoomDatabase : RoomDatabase() {
    abstract fun templateRepository(): RoomTemplateRepository
}
