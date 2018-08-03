package com.apps.krishnakandula.dicerollerui.view.savetemplate

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollercore.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.di.DaggerSaveTemplateDialogComponent
import com.apps.krishnakandula.dicerollerui.di.SaveTemplateDialogModule
import com.apps.krishnakandula.dicerollerui.view.roll.DiceEquationAdapter
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.fragment_save_template.*
import javax.inject.Inject

class SaveTemplateDialogFragment :
        DialogFragment(),
        SaveTemplateDialogView,
        SaveTemplateDialogView.UserActions,
        SaveTemplateDialogView.ViewActions {

    @Parcelize
    data class Rolls(val rolls: @RawValue List<List<Dice>>) : Parcelable

    companion object {

        const val SAVE_TEMPLATE_DIALOG_FRAGMENT_ROLLS_KEY = "SAVE_TEMPLATE_DIALOG_FRAGMENT_ROLLS"
        private val LOG_TAG = SaveTemplateDialogFragment::class.java.simpleName

        fun init(rolls: Rolls): SaveTemplateDialogFragment {
            val fragment = SaveTemplateDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable(SAVE_TEMPLATE_DIALOG_FRAGMENT_ROLLS_KEY, rolls)
            fragment.arguments = bundle

            return fragment
        }
    }

    @Inject lateinit var diceEquationAdapter: DiceEquationAdapter
    @Inject lateinit var viewModel: SaveTemplateDialogViewModel
    @Inject lateinit var backpressureStrategy: BackpressureStrategy
    @Inject lateinit var presenter: BasePresenter

    private val confirmBtnClickRelay = PublishRelay.create<String>()
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        val rolls: Rolls = arguments?.get(SAVE_TEMPLATE_DIALOG_FRAGMENT_ROLLS_KEY) as Rolls
        if (rolls == null) {
            val e = NullPointerException("Bundle does not contain extra with key $SAVE_TEMPLATE_DIALOG_FRAGMENT_ROLLS_KEY")
            Log.e(LOG_TAG, "Unable to retrieve rolls from bundle", e)
            throw e
        }

        DaggerSaveTemplateDialogComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .saveTemplateUIModule(SaveTemplateDialogModule(this, requireContext()))
                .build()
                .inject(this)

        viewModel.rolls.accept(rolls.rolls)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_save_template_recycler_view.adapter = diceEquationAdapter
        fragment_save_template_recycler_view.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false)
        setupListeners()
        setupActions()
        disposable.addAll(
                presenter.bindActions(),
                viewModel.bindSources())
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        // Needed to force Dialog to take up more width
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun setupActions() {
        fragment_save_template_cancel_btn.setOnClickListener { dialog.cancel() }
        fragment_save_template_confirm_btn.setOnClickListener {
            confirmBtnClickRelay.accept(fragment_save_template_name_edit_text.text.toString())
        }
    }

    override fun setupListeners() {
        viewModel.rolls.subscribeBy(onNext = { diceEquationAdapter.setData(it, { }) })
    }

    override fun onClickConfirmBtn(): Flowable<String> = confirmBtnClickRelay.toFlowable(backpressureStrategy)

    override fun dismissDialog() {
        this.dismiss()
    }
}
