package com.apps.krishnakandula.dicerollerui.view

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apps.krishnakandula.common.util.Result
import com.apps.krishnakandula.common.view.BaseViewModel
import com.apps.krishnakandula.diceroller.Dice
import com.apps.krishnakandula.diceroller.roller.DiceRollResult
import com.apps.krishnakandula.diceroller.roller.DiceRoller
import com.apps.krishnakandula.diceroller.template.Template
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class DiceRollerViewModel(private val diceRoller: DiceRoller) : ViewModel(), BaseViewModel {

    val diceInEquation: BehaviorRelay<List<Dice>> = BehaviorRelay.createDefault(emptyList())
    val templates: BehaviorRelay<List<Template>> = BehaviorRelay.createDefault(emptyList())
    val previousRolls: BehaviorRelay<List<DiceRollResult>> = BehaviorRelay.createDefault(emptyList())

    var numCreated = 0

    companion object {
        private val LOG_TAG = DiceRollerViewModel::class.java.simpleName
    }

    override fun bindSources(): CompositeDisposable {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(diceRoller.previousRolls()
                .subscribeBy(onNext = { previousRolls.accept(it) }))
        Log.v(LOG_TAG, "Number of times created: ${numCreated++}")
        return compositeDisposable
    }
}
