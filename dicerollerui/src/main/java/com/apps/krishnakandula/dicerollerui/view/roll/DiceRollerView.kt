package com.apps.krishnakandula.dicerollerui.view.roll

import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollercore.template.Template
import io.reactivex.Flowable

interface DiceRollerView {

    fun setupActions()

    fun setupListeners()

    interface UserActions {

        fun onClickDiceBtn(): Flowable<Dice>

        fun onClickEqualsBtn(): Flowable<List<List<Dice>>>

        fun onClickDeleteBtn(): Flowable<Unit>

        fun onLongClickDeleteBtn(): Flowable<Unit>

        fun onClickTemplate(): Flowable<Template>

    }
}
