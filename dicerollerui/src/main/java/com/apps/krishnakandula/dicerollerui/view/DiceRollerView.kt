package com.apps.krishnakandula.dicerollerui.view

import com.apps.krishnakandula.diceroller.Dice
import com.apps.krishnakandula.diceroller.template.Template
import io.reactivex.Flowable

interface DiceRollerView {

    fun setupActions()

    fun setupListeners()

    interface Actions {

        fun onClickDiceBtn(): Flowable<Dice>

        fun onClickEqualsBtn(): Flowable<List<Dice>>

        fun onClickSaveBtn(): Flowable<Template>

        fun onClickDeleteBtn(): Flowable<Unit>
    }
}
