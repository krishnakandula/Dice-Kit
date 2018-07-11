package com.apps.krishnakandula.dicerollercore

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.data.DiceRollerDataModule
import com.apps.krishnakandula.dicerollercore.roller.DiceRoller
import com.apps.krishnakandula.dicerollercore.roller.DiceRollerModule
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import dagger.Component

@Scopes.Application
@Component(modules = [DiceRollerModule::class, DiceRollerDataModule::class])
interface DiceRollerComponent {

    fun diceRoller(): DiceRoller
    fun templateRepository(): TemplateRepository

    @Component.Builder
    interface Builder {

        fun build(): DiceRollerComponent
        fun diceRollerModule(diceRollerModule: DiceRollerModule): Builder
        fun diceRollerDataModule(diceRollerDataModule: DiceRollerDataModule): Builder
    }
}
