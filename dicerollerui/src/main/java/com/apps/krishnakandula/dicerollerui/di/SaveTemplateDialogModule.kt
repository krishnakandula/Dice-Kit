package com.apps.krishnakandula.dicerollerui.di

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollerui.view.roll.DiceEquationAdapter
import com.apps.krishnakandula.dicerollerui.view.savetemplate.SaveTemplateDialogFragment
import com.apps.krishnakandula.dicerollerui.view.savetemplate.SaveTemplateDialogPresenter
import com.apps.krishnakandula.dicerollerui.view.savetemplate.SaveTemplateDialogView
import com.apps.krishnakandula.dicerollerui.view.savetemplate.SaveTemplateDialogViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.BackpressureStrategy

@Module
class SaveTemplateDialogModule(private val saveTemplateDialogFragment: SaveTemplateDialogFragment,
                               private val context: Context) {

    @Provides
    @Scopes.Dialog
    fun provideContext(): Context = context

    @Provides
    @Scopes.Dialog
    fun provideFragment(): SaveTemplateDialogFragment = saveTemplateDialogFragment

    @Provides
    @Scopes.Dialog
    fun provideUserActions(): SaveTemplateDialogView.UserActions = saveTemplateDialogFragment

    @Provides
    @Scopes.Dialog
    fun provideViewActions(): SaveTemplateDialogView.ViewActions = saveTemplateDialogFragment

    @Provides
    @Scopes.Dialog
    fun provideViewModel(saveTemplateDialogFragment: SaveTemplateDialogFragment): SaveTemplateDialogViewModel {
        return ViewModelProviders.of(saveTemplateDialogFragment)[SaveTemplateDialogViewModel::class.java]
    }

    @Provides
    @Scopes.Dialog
    fun provideDiceEquationAdapter(context: Context): DiceEquationAdapter = DiceEquationAdapter(context)

    @Provides
    @Scopes.Dialog
    fun provideBackPressureStrategy(): BackpressureStrategy = BackpressureStrategy.LATEST

    @Provides
    @Scopes.Dialog
    fun providePresenter(presenter: SaveTemplateDialogPresenter): BasePresenter = presenter

}
