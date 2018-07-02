package com.apps.krishnakandula.dmtoolkit

import android.app.Application
import com.apps.krishnakandula.diceroller.DaggerDiceRollerComponent
import com.apps.krishnakandula.diceroller.DiceRollerComponent
import com.apps.krishnakandula.diceroller.DiceRollerComponentProvider
import com.apps.krishnakandula.diceroller.DiceRollerModule

class DMToolkitApp : Application(), DiceRollerComponentProvider {

    private lateinit var diceRollerComponent: DiceRollerComponent

    override fun onCreate() {
        super.onCreate()
        diceRollerComponent = DaggerDiceRollerComponent.builder()
                .diceRollerModule(DiceRollerModule())
                .build()
    }

    override fun provideDiceRollerComponent(): DiceRollerComponent = diceRollerComponent
}