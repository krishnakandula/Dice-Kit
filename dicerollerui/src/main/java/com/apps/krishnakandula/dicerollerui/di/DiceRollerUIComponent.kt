package com.apps.krishnakandula.dicerollerui.di

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.diceroller.DiceRollerComponent
import com.apps.krishnakandula.dicerollerui.view.DiceRollerFragment
import dagger.Component

@Scopes.Feature
@Component(dependencies = [DiceRollerComponent::class],
        modules = [DiceRollerUIModule::class, DiceRollerDataModule::class])
interface DiceRollerUIComponent {

    fun inject(diceRollerFragment: DiceRollerFragment)

    @Component.Builder
    interface Builder {
        fun build(): DiceRollerUIComponent
        fun diceRollerComponent(diceRollerComponent: DiceRollerComponent): Builder
        fun diceRollerUIModule(diceRollerUIModule: DiceRollerUIModule): Builder
        fun diceRollerDataModule(diceRollerDataModule: DiceRollerDataModule): Builder
    }
}
