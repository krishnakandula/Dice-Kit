package com.apps.krishnakandula.dicerollerui.di

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.DiceRollerComponent
import com.apps.krishnakandula.dicerollerui.view.savetemplate.SaveTemplateDialogFragment
import dagger.Component

@Component(dependencies = [DiceRollerComponent::class], modules = [SaveTemplateUIModule::class])
@Scopes.Dialog
interface SaveTemplateUIComponent {
    fun inject(saveTemplateDialogFragment: SaveTemplateDialogFragment)

    @Component.Builder
    interface Builder {
        fun build(): SaveTemplateUIComponent
        fun diceRollerComponent(diceRollerComponent: DiceRollerComponent): Builder
        fun saveTemplateUIModule(saveTemplateUIModule: SaveTemplateUIModule): Builder
    }
}
