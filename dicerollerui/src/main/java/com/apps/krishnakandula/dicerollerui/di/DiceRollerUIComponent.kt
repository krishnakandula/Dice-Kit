package com.apps.krishnakandula.dicerollerui.di

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.DiceRollerComponent
import com.apps.krishnakandula.dicerollerui.view.roll.DiceRollerFragment
import dagger.Component

@Scopes.Fragment
@Component(dependencies = [DiceRollerComponent::class], modules = [DiceRollerUIModule::class])
interface DiceRollerUIComponent {

    fun inject(diceRollerFragment: DiceRollerFragment)

    @Component.Builder
    interface Builder {
        fun build(): DiceRollerUIComponent
        fun diceRollerComponent(diceRollerComponent: DiceRollerComponent): Builder
        fun diceRollerUIModule(diceRollerUIModule: DiceRollerUIModule): Builder
    }
}
