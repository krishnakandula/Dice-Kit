package com.apps.krishnakandula.dicerollerui.view.savetemplate

import android.util.Log
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Scopes.Dialog
class SaveTemplateDialogPresenter @Inject constructor(private val userActions: SaveTemplateDialogView.UserActions,
                                                      private val viewActions: SaveTemplateDialogView.ViewActions,
                                                      private val templateRepository: TemplateRepository,
                                                      private val viewModel: SaveTemplateDialogViewModel) : BasePresenter {

    companion object {
        private val LOG_TAG = SaveTemplateDialogPresenter::class.simpleName
    }

    override fun bindActions(): CompositeDisposable {
        val disposable = CompositeDisposable()

        disposable.add(userActions.onClickConfirmBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .filter { it.isNotBlank() }
                .observeOn(Schedulers.io())
                .doOnNext {
                    Log.v(LOG_TAG, "Confirm button pressed")
                    templateRepository.addTemplate(Template(
                            null,
                            it,
                            viewModel.rolls.value.map { it.toTypedArray() }.toTypedArray()))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    viewActions.dismissDialog()
                }))

        return disposable
    }
}
