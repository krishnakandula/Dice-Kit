package com.apps.krishnakandula.diceroller

import com.apps.krishnakandula.common.Scopes
import dagger.Component

@Scopes.Application
@Component(modules = [DiceRollerModule::class])
interface DiceRollerComponent {

    fun diceRoller(): DiceRoller

    @Component.Builder
    interface Builder {

        fun build(): DiceRollerComponent

        fun diceRollerModule(diceRollerModule: DiceRollerModule): Builder
    }
}