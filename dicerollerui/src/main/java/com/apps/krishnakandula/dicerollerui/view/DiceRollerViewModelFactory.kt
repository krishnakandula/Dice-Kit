package com.apps.krishnakandula.dicerollerui.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.diceroller.roller.DiceRoller
import javax.inject.Inject

@Scopes.Feature
class DiceRollerViewModelFactory @Inject constructor(private val diceRoller: DiceRoller) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceRollerViewModel::class.java)) {
            return DiceRollerViewModel(diceRoller) as T
        }
        throw IllegalArgumentException("Unknown view model class $modelClass")
    }
}
