package com.apps.krishnakandula.dicerollerui.view.deletetemplate

import android.util.Log
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Scopes.Dialog
class DeleteTemplateDialogPresenter @Inject constructor(private val userActions: DeleteTemplateDialogView.UserActions,
                                                        private val viewActions: DeleteTemplateDialogView.ViewActions,
                                                        private val templateRepository: TemplateRepository): BasePresenter {

    companion object {
        private val LOG_TAG = DeleteTemplateDialogPresenter::class.java.simpleName
    }

    override fun bindActions(): CompositeDisposable {
        val disposable = CompositeDisposable()

        disposable.add(userActions.onClickConfirmDeleteBtn()
                .debounce(BasePresenter.DEFAULT_ACTIONS_TIMEOUT, BasePresenter.DEFAULT_TIME_UNIT)
                .observeOn(Schedulers.io())
                .doOnNext {
                    Log.v(LOG_TAG, "Confirm delete button pressed")
                    templateRepository.deleteTemplate(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    viewActions.dismissDialog()
                }))

        return disposable
    }
}
