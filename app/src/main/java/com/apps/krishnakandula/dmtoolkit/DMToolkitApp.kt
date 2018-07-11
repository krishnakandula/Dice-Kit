package com.apps.krishnakandula.dmtoolkit

import android.app.Application
import com.apps.krishnakandula.dicerollercore.DaggerDiceRollerComponent
import com.apps.krishnakandula.dicerollercore.DiceRollerComponent
import com.apps.krishnakandula.dicerollercore.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollercore.roller.DiceRollerModule

class DMToolkitApp : Application(), DiceRollerComponentProvider {

    private lateinit var diceRollerComponent: DiceRollerComponent

    override fun onCreate() {
        super.onCreate()
        diceRollerComponent = DaggerDiceRollerComponent.builder()
                .diceRollerModule(DiceRollerModule())
                .build()
    }

    override fun diceRollerComponent(): DiceRollerComponent = diceRollerComponent
}
