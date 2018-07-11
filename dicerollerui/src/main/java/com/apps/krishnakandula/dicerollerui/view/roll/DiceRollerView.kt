package com.apps.krishnakandula.dicerollerui.view.roll

import com.apps.krishnakandula.dicerollercore.Dice
import io.reactivex.Flowable

interface DiceRollerView {

    fun setupActions()

    fun setupListeners()

    interface Actions {

        fun onClickDiceBtn(): Flowable<Dice>

        fun onClickEqualsBtn(): Flowable<List<Dice>>

        fun onClickDeleteBtn(): Flowable<Unit>
    }
}
