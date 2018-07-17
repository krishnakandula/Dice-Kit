package com.apps.krishnakandula.dicerollerui.view.roll

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.roller.DiceRoller
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import javax.inject.Inject

@Scopes.Fragment
class DiceRollerViewModelFactory @Inject constructor(private val diceRoller: DiceRoller,
                                                     private val templateRepository: TemplateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceRollerViewModel::class.java)) {
            return DiceRollerViewModel(diceRoller, templateRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class $modelClass")
    }
}
