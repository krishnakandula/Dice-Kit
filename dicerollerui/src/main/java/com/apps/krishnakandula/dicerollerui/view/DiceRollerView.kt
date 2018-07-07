package com.apps.krishnakandula.dicerollerui.view

import com.apps.krishnakandula.common.view.BaseView
import com.apps.krishnakandula.diceroller.Dice
import io.reactivex.Flowable

interface DiceRollerView : BaseView {

    interface Actions {

        fun onClickDiceBtn(): Flowable<Dice>

        fun onClickEqualsBtn(): Flowable<List<Dice>>

        fun onClickSaveBtn(): Flowable<List<Dice>>
    }
}
