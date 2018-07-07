package com.apps.krishnakandula.dicerollerui.view

import android.util.Log
import com.apps.krishnakandula.common.view.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class DiceRollerPresenter @Inject constructor(private val actions: DiceRollerView.Actions,
                                              private val viewModel: DiceRollerViewModel) : BasePresenter {

    companion object {
        private val LOG_TAG = DiceRollerPresenter::class.simpleName
    }

    override fun bindActions(): CompositeDisposable {
        val disposable = CompositeDisposable()
        disposable.add(actions.onClickDiceBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Dice Button pressed") }
                .subscribeBy(onNext = { }))

        disposable.add(actions.onClickEqualsBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Equals Button pressed") }
                .subscribeBy(onNext = { }))

        disposable.add(actions.onClickSaveBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .doOnNext { Log.v(LOG_TAG, "Save Button pressed") }
                .subscribeBy(onNext = { }))

        return disposable
    }
}
