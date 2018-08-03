package com.apps.krishnakandula.dicerollerui.view.deletetemplate

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.DiceRollerComponent
import dagger.Component

@Component(dependencies = [DiceRollerComponent::class], modules = [DeleteTemplateDialogModule::class])
@Scopes.Dialog
interface DeleteTemplateDialogComponent {
    fun inject(deleteTemplateDialogFragment: DeleteTemplateDialogFragment)
}
