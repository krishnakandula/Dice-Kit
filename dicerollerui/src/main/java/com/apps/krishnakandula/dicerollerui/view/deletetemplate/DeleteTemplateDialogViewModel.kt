package com.apps.krishnakandula.dicerollerui.view.deletetemplate

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BaseViewModel
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DeleteTemplateDialogViewModel(private val templateId: Long,
                                    private val templateRepository: TemplateRepository) : ViewModel(), BaseViewModel {

    val template: BehaviorRelay<Template> = BehaviorRelay.create()

    override fun bindSources(): CompositeDisposable {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(templateRepository.getTemplate(templateId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    template.accept(it)
                }))

        return compositeDisposable
    }

    @Scopes.Dialog
    class ViewModelFactory @Inject constructor(private val templateId: Long,
                                               private val templateRepository: TemplateRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DeleteTemplateDialogViewModel::class.java)) {
                return DeleteTemplateDialogViewModel(templateId, templateRepository) as T
            }
            throw IllegalArgumentException("Unknown view model class $modelClass")
        }
    }
}
