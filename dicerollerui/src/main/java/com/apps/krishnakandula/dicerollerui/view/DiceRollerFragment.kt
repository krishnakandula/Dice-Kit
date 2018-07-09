package com.apps.krishnakandula.dicerollerui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.dice_pad.*
import kotlinx.android.synthetic.main.fragment_dice_roller.*
import javax.inject.Inject

class DiceRollerFragment : Fragment(), DiceRollerView, DiceRollerView.Actions {

    @Inject lateinit var presenter: BasePresenter
    @Inject lateinit var viewModel: DiceRollerViewModel
    @Inject lateinit var backPressureStrategy: BackpressureStrategy
    @Inject lateinit var diceEquationAdapter: DiceEquationAdapter
    @Inject lateinit var previousRollsAdapter: PreviousRollsAdapter
    private var disposable = CompositeDisposable()

    private val diceBtnClickRelay = PublishRelay.create<Dice>()
    private val deleteBtnClickRelay = PublishRelay.create<Unit>()
    private val equalsBtnClickRelay = PublishRelay.create<List<Dice>>()
    private val saveBtnClickRelay = PublishRelay.create<List<Dice>>()

    companion object {
        private val LOG_TAG = DiceRollerFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        DaggerDiceRollerUIComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .diceRollerUIModule(DiceRollerUIModule(this, this.requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dice_roller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_dice_roller_equation_edit_recycler_view.adapter = diceEquationAdapter
        fragment_dice_roller_equation_edit_recycler_view.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        fragment_dice_roller_history_recycler_view.adapter = previousRollsAdapter
        fragment_dice_roller_history_recycler_view.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,
                false)
        setupActions()
        setupListeners()
        disposable.addAll(presenter.bindActions(), viewModel.bindSources())
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }

    override fun setupActions() {
        dice_pad_d2_btn.setOnClickListener { diceBtnClickRelay.accept(Dice.D2()) }
        dice_pad_d4_btn.setOnClickListener { diceBtnClickRelay.accept(Dice.D4()) }
        dice_pad_d6_btn.setOnClickListener { diceBtnClickRelay.accept(Dice.D6()) }
        dice_pad_d8_btn.setOnClickListener { diceBtnClickRelay.accept(Dice.D8()) }
        dice_pad_d10_btn.setOnClickListener { diceBtnClickRelay.accept(Dice.D10()) }
        dice_pad_d20_btn.setOnClickListener { diceBtnClickRelay.accept(Dice.D20()) }
        dice_pad_save_btn.setOnClickListener { saveBtnClickRelay.accept(viewModel.diceInEquation.value) }
        dice_pad_eq_btn.setOnClickListener { equalsBtnClickRelay.accept(viewModel.diceInEquation.value) }
        dice_pad_delete_btn.setOnClickListener { deleteBtnClickRelay.accept(Unit) }
    }

    override fun setupListeners() {
        viewModel.diceInEquation.subscribeBy(onNext = { diceEquationAdapter.setData(it.map { die -> Pair(die, null) }) })

        viewModel.templates.subscribeBy(onNext = {
            dice_pad_template_recyclerview.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        })

        viewModel.previousRolls.subscribeBy(onNext = {
            previousRollsAdapter.setData(it)
            if (previousRollsAdapter.itemCount > 0) {
                fragment_dice_roller_history_recycler_view.smoothScrollToPosition(previousRollsAdapter.itemCount - 1)
            }

        })
    }

    override fun onClickDiceBtn(): Flowable<Dice> = diceBtnClickRelay.toFlowable(backPressureStrategy)

    override fun onClickEqualsBtn(): Flowable<List<Dice>> = equalsBtnClickRelay.toFlowable(backPressureStrategy)

    override fun onClickSaveBtn(): Flowable<List<Dice>> = saveBtnClickRelay.toFlowable(backPressureStrategy)

    override fun onClickDeleteBtn(): Flowable<Unit> = deleteBtnClickRelay.toFlowable(backPressureStrategy)
}
