package com.apps.krishnakandula.dicerollerui.view.deletetemplate

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollerui.view.roll.DiceEquationAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.BackpressureStrategy

@Module
class DeleteTemplateDialogModule(private val deleteTemplateDialogFragment: DeleteTemplateDialogFragment,
                                 private val templateId: Long) {

    @Provides
    @Scopes.Dialog
    fun provideContext(): Context = deleteTemplateDialogFragment.requireContext()

    @Provides
    @Scopes.Dialog
    fun provideDiceEquationAdapter(context: Context): DiceEquationAdapter = DiceEquationAdapter(context)

    @Provides
    @Scopes.Dialog
    fun provideBackPressureStrategy(): BackpressureStrategy = BackpressureStrategy.LATEST

    @Provides
    @Scopes.Dialog
    fun provideFragment(): DeleteTemplateDialogFragment = deleteTemplateDialogFragment

    @Provides
    @Scopes.Dialog
    fun provideUserActions(): DeleteTemplateDialogView.UserActions = deleteTemplateDialogFragment


    @Provides
    @Scopes.Dialog
    fun provideViewActions(): DeleteTemplateDialogView.ViewActions = deleteTemplateDialogFragment

    @Provides
    @Scopes.Dialog
    fun providePresenter(presenter: DeleteTemplateDialogPresenter): BasePresenter = presenter

    @Provides
    @Scopes.Dialog
    fun provideViewModelClazz(): Class<DeleteTemplateDialogViewModel> = DeleteTemplateDialogViewModel::class.java

    @Provides
    @Scopes.Dialog
    fun provideViewModel(deleteTemplateDialogFragment: DeleteTemplateDialogFragment,
                         viewModelFactory: DeleteTemplateDialogViewModel.ViewModelFactory,
                         clazz: Class<DeleteTemplateDialogViewModel>): DeleteTemplateDialogViewModel {
        return ViewModelProviders.of(deleteTemplateDialogFragment, viewModelFactory)[clazz]
    }

    @Provides
    @Scopes.Dialog
    fun provideTemplateId(): Long = templateId
}
