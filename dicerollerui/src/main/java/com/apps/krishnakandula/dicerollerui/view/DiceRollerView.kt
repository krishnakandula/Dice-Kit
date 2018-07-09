package com.apps.krishnakandula.dicerollerui.view

import com.apps.krishnakandula.diceroller.Dice
import io.reactivex.Flowable

interface DiceRollerView {

    fun setupActions()

    fun setupListeners()

    interface Actions {

        fun onClickDiceBtn(): Flowable<Dice>

        fun onClickEqualsBtn(): Flowable<List<Dice>>

        fun onClickSaveBtn(): Flowable<List<Dice>>

        fun onClickDeleteBtn(): Flowable<Unit>
    }
}