package com.apps.krishnakandula.dicerollerui.view.deletetemplate

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.dicerollercore.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.view.roll.DiceEquationAdapter
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_delete_template.*
import kotlinx.android.synthetic.main.template_item.*
import javax.inject.Inject

class DeleteTemplateDialogFragment :
        DialogFragment(),
        DeleteTemplateDialogView,
        DeleteTemplateDialogView.UserActions,
        DeleteTemplateDialogView.ViewActions {

    companion object {
        const val TEMPLATE_ID_KEY = "DeleteTemplateDialogFragment_TEMPLATE_ID_KEY"

        fun newInstance(templateId: Long): DeleteTemplateDialogFragment {
            val bundle = Bundle()
            bundle.putLong(TEMPLATE_ID_KEY, templateId)
            val fragment = DeleteTemplateDialogFragment()
            fragment.arguments = bundle

            return fragment
        }

        private val LOG_TAG = DeleteTemplateDialogFragment::class.java.simpleName
    }

    @Inject lateinit var viewModel: DeleteTemplateDialogViewModel
    @Inject lateinit var presenter: DeleteTemplateDialogPresenter
    @Inject lateinit var diceEquationAdapter: DiceEquationAdapter
    @Inject lateinit var backPressureStrategy: BackpressureStrategy

    private val confirmBtnClickRelay = PublishRelay.create<Template>()
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        var templateId = arguments?.getLong(TEMPLATE_ID_KEY)
        if (templateId == null) {
            val e = NullPointerException("Bundle does not contain extra with key $TEMPLATE_ID_KEY")
            Log.e(LOG_TAG, "Unable to retrieve template from bundle", e)
            throw e
        }

        DaggerDeleteTemplateDialogComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .deleteTemplateDialogModule(DeleteTemplateDialogModule(this, templateId))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delete_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_delete_template_recycler_view.adapter = diceEquationAdapter
        fragment_delete_template_recycler_view.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false)
        setupListeners()
        setupActions()
        disposable.addAll(
                presenter.bindActions(),
                viewModel.bindSources())
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }

    override fun setupActions() {
        fragment_delete_template_confirm_btn.setOnClickListener {
            if (viewModel.template.hasValue()) {
                confirmBtnClickRelay.accept(viewModel.template.value)
            }
        }
        fragment_delete_template_cancel_btn.setOnClickListener { dialog.cancel() }
    }

    override fun setupListeners() {
        viewModel.template.subscribeBy(onNext = {
            template_item_template_name.text = it.name
            diceEquationAdapter.setData(it.rolls.map { it.toList() })
        })
    }

    override fun dismissDialog() {
        this.dismiss()
    }

    override fun onClickConfirmDeleteBtn(): Flowable<Template> = confirmBtnClickRelay.toFlowable(backPressureStrategy)
}
