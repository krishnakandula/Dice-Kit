package com.apps.krishnakandula.dicerollerui.view.roll

import android.util.Log
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.util.Result
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollercore.roller.DiceRoller
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@Scopes.Feature
class DiceRollerPresenter @Inject constructor(private val actions: DiceRollerView.Actions,
                                              private val viewModel: DiceRollerViewModel,
                                              private val diceRoller: DiceRoller) : BasePresenter {

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
                    val equation = viewModel.diceInEquation.value.toMutableList()
                    if (equation.isEmpty()) equation.add(listOf(die))
                    else {
                        if (equation.last().first()::class == die::class) {
                            val sameDice = equation.last().toMutableList()
                            sameDice.add(die)
                            equation.removeAt(equation.lastIndex)
                            equation.add(sameDice)
                        } else {
                            equation.add(listOf(die))
                        }
                    }
                    viewModel.diceInEquation.accept(equation)
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
