package com.apps.krishnakandula.dicerollerui.view

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.apps.krishnakandula.common.util.Result
import com.apps.krishnakandula.common.view.BaseViewModel
import com.apps.krishnakandula.diceroller.Dice
import com.apps.krishnakandula.diceroller.roller.DiceRollResult
import com.apps.krishnakandula.diceroller.roller.DiceRoller
import com.apps.krishnakandula.diceroller.template.Template
import com.apps.krishnakandula.diceroller.template.TemplateRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class DiceRollerViewModel(private val diceRoller: DiceRoller,
                          private val templateRepository: TemplateRepository) : ViewModel(), BaseViewModel {

    val diceInEquation: BehaviorRelay<List<Dice>> = BehaviorRelay.createDefault(emptyList())
    val templates: BehaviorRelay<List<Template>> = BehaviorRelay.createDefault(emptyList())
    val previousRolls: BehaviorRelay<List<DiceRollResult>> = BehaviorRelay.createDefault(emptyList())

    companion object {
        private val LOG_TAG = DiceRollerViewModel::class.java.simpleName
    }

    override fun bindSources(): CompositeDisposable {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(diceRoller.previousRolls()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { previousRolls.accept(it) }))

        compositeDisposable.add(templateRepository.getTemplates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { templates.accept(it) }))

        return compositeDisposable
    }
}
