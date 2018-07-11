package com.apps.krishnakandula.dicerollerui.view.roll

import android.arch.lifecycle.ViewModel
import com.apps.krishnakandula.common.view.BaseViewModel
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollercore.roller.DiceRollResult
import com.apps.krishnakandula.dicerollercore.roller.DiceRoller
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class DiceRollerViewModel(private val diceRoller: DiceRoller,
                          private val templateRepository: TemplateRepository) : ViewModel(), BaseViewModel {

    val diceInEquation: BehaviorRelay<List<List<Dice>>> = BehaviorRelay.createDefault(emptyList())
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
