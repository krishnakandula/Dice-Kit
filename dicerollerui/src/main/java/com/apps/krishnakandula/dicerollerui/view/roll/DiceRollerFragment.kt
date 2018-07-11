package com.apps.krishnakandula.dicerollerui.view.roll

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollercore.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.di.*
import com.apps.krishnakandula.dicerollerui.view.savetemplate.SaveTemplateDialogFragment
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.dice_pad.*
import kotlinx.android.synthetic.main.fragment_dice_roller.*
import javax.inject.Inject

class DiceRollerFragment : Fragment(), DiceRollerView, DiceRollerView.Actions, DiceRollerUIComponentProvider {

    @Inject lateinit var presenter: BasePresenter
    @Inject lateinit var viewModel: DiceRollerViewModel
    @Inject lateinit var backPressureStrategy: BackpressureStrategy
    @Inject lateinit var diceEquationAdapter: DiceEquationAdapter
    @Inject lateinit var previousRollsAdapter: PreviousRollsAdapter
    @Inject lateinit var templatesAdapter: TemplateAdapter
    private lateinit var diceRollerUIComponent: DiceRollerUIComponent
    private var disposable = CompositeDisposable()

    private val diceBtnClickRelay = PublishRelay.create<Dice>()
    private val deleteBtnClickRelay = PublishRelay.create<Unit>()
    private val equalsBtnClickRelay = PublishRelay.create<List<Dice>>()

    companion object {
        private val LOG_TAG = DiceRollerFragment::class.java.simpleName
        private const val SAVE_TEMPLATE_DIALOG_FRAGMENT_TAG = "SAVE_TEMPLATE_DIALOG_FRAGMENT_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        diceRollerUIComponent = DaggerDiceRollerUIComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .diceRollerUIModule(DiceRollerUIModule(this, this.requireContext()))
                .diceRollerDataModule(DiceRollerDataModule(this.requireContext()))
                .build()
        diceRollerUIComponent.inject(this)
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
        dice_pad_template_recyclerview.adapter = templatesAdapter
        dice_pad_template_recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        dice_pad_save_btn.setOnClickListener {
            var dialogFragment = childFragmentManager.findFragmentByTag(SAVE_TEMPLATE_DIALOG_FRAGMENT_TAG)
            if (dialogFragment == null) dialogFragment = SaveTemplateDialogFragment()
            if (!dialogFragment.isAdded) (dialogFragment as SaveTemplateDialogFragment).show(childFragmentManager, SAVE_TEMPLATE_DIALOG_FRAGMENT_TAG)
        }
        dice_pad_eq_btn.setOnClickListener { equalsBtnClickRelay.accept(viewModel.diceInEquation.value) }
        dice_pad_delete_btn.setOnClickListener { deleteBtnClickRelay.accept(Unit) }
    }

    override fun setupListeners() {
        viewModel.diceInEquation.subscribeBy(onNext = { diceEquationAdapter.setData(it.map { die -> Pair(die, null) }) })

        viewModel.templates.subscribeBy(onNext = { templates ->
            if (templates.isEmpty()) dice_pad_template_recyclerview.visibility = View.GONE
            else {
                dice_pad_template_recyclerview.visibility = View.VISIBLE
                templatesAdapter.setData(templates)
            }
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

    override fun onClickDeleteBtn(): Flowable<Unit> = deleteBtnClickRelay.toFlowable(backPressureStrategy)

    override fun diceRollerUIComponent(): DiceRollerUIComponent = diceRollerUIComponent
}
