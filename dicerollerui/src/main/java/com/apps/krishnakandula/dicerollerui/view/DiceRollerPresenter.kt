package com.apps.krishnakandula.dicerollerui.view

import android.util.Log
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.util.Result
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.diceroller.roller.DiceRoller
import com.apps.krishnakandula.diceroller.template.Template
import com.apps.krishnakandula.diceroller.template.TemplateRepository
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.data.DiceRollerRoomDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import javax.inject.Inject

@Scopes.Feature
class DiceRollerPresenter @Inject constructor(private val actions: DiceRollerView.Actions,
                                              private val viewModel: DiceRollerViewModel,
                                              private val diceRoller: DiceRoller,
                                              private val templateRepository: TemplateRepository) : BasePresenter {

    companion object {
        private val LOG_TAG = DiceRollerPresenter::class.simpleName
    }

    override fun bindActions(): CompositeDisposable {
        val disposable = CompositeDisposable()
        disposable.add(actions.onClickDiceBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Dice Button pressed") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { die ->
                    val dice = viewModel.diceInEquation.value.toMutableList()
                    dice.add(die)
                    viewModel.diceInEquation.accept(dice)
                }))

        disposable.add(actions.onClickEqualsBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Equals Button pressed") }
                .filter { it.isNotEmpty() }
                .flatMap { diceRoller.roll(it).toFlowable() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    when (it) {
                        is Result.Error -> {
                            Log.e(LOG_TAG, "Unable to retrieve result", it.error)
                        }
                        is Result.Success -> {
                            // Update previous rolls to include calculated result
                            diceRoller.addToHistory(it.data).subscribe()
                            // Clear view model list of dice in equation
                            viewModel.diceInEquation.accept(emptyList())
                        }
                    }
                }))

        disposable.add(actions.onClickSaveBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Save Button pressed") }
                .observeOn(Schedulers.io())
                .subscribeBy(onNext = { templateRepository.addTemplate(it) }))

        disposable.add(actions.onClickDeleteBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Delete Button pressed") }
                .filter { viewModel.diceInEquation.value.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    val dice = viewModel.diceInEquation.value.toMutableList()
                    dice.removeAt(dice.lastIndex)
                    viewModel.diceInEquation.accept(dice)
                }))

        return disposable
    }

}
