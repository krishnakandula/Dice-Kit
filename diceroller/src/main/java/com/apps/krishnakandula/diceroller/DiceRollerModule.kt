package com.apps.krishnakandula.diceroller

import com.apps.krishnakandula.common.Scopes
import dagger.Module
import dagger.Provides

@Module
class DiceRollerModule {

    @Provides
    @Scopes.Application
    fun bindDiceRoller(diceRollerImpl: DiceRollerImpl): DiceRoller = diceRollerImpl
}