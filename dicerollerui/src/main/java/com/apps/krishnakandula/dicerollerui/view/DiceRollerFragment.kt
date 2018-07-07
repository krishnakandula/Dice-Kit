package com.apps.krishnakandula.dicerollerui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.diceroller.Dice
import com.apps.krishnakandula.diceroller.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.di.DaggerDiceRollerUIComponent
import com.apps.krishnakandula.dicerollerui.di.DiceRollerUIModule
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DiceRollerFragment : Fragment(), DiceRollerView, DiceRollerView.Actions {

    @Inject lateinit var presenter: BasePresenter
    @Inject lateinit var viewModel: DiceRollerViewModel
    @Inject lateinit var backPressureStrategy: BackpressureStrategy
    private var disposable = CompositeDisposable()

    private val diceBtnClickRelay = PublishRelay.create<Dice>()
    private val equalsBtnClickRelay = PublishRelay.create<List<Dice>>()
    private val saveBtnClickRelay = PublishRelay.create<List<Dice>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        DaggerDiceRollerUIComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .diceRollerUIModule(DiceRollerUIModule(this))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dice_roller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setupListeners()
        disposable.add(presenter.bindActions())
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }

    override fun setupActions() {

    }

    override fun setupListeners() {
    }

    override fun onClickDiceBtn(): Flowable<Dice> = diceBtnClickRelay.toFlowable(backPressureStrategy)

    override fun onClickEqualsBtn(): Flowable<List<Dice>> = equalsBtnClickRelay.toFlowable(backPressureStrategy)

    override fun onClickSaveBtn(): Flowable<List<Dice>> = saveBtnClickRelay.toFlowable(backPressureStrategy)

}
