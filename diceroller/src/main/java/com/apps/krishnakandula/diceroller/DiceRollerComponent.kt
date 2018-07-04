package com.apps.krishnakandula.diceroller

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.diceroller.roller.DiceRoller
import com.apps.krishnakandula.diceroller.roller.DiceRollerModule
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
