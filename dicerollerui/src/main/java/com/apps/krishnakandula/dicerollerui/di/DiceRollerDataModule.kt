package com.apps.krishnakandula.dicerollerui.di

import android.arch.persistence.room.Room
import android.content.Context
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.diceroller.template.TemplateRepository
import com.apps.krishnakandula.dicerollerui.data.DiceRollerRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DiceRollerDataModule(private val context: Context) {

    @Provides
    @Named("dice_roller_room_db_name")
    fun provideDiceRollerRoomDbName(): String = "dice_roller_room_db"

    @Provides
    @Scopes.Feature
    fun provideDiceRollerRoomDbClass(): Class<DiceRollerRoomDatabase> = DiceRollerRoomDatabase::class.java

    @Provides
    @Scopes.Feature
    fun provideDiceRollerRoomDb(dbClass: Class<DiceRollerRoomDatabase>,
                                @Named("dice_roller_room_db_name") dbName: String): DiceRollerRoomDatabase {
        return Room.databaseBuilder(context, dbClass, dbName).build()
    }

    @Provides
    @Scopes.Feature
    fun provideTemplateRepository(db: DiceRollerRoomDatabase): TemplateRepository = db.templateRepository()
}
