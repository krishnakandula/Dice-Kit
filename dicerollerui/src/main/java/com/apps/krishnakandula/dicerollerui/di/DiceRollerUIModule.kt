package com.apps.krishnakandula.dicerollerui.di

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollerui.view.roll.*
import dagger.Module
import dagger.Provides
import io.reactivex.BackpressureStrategy

@Module
class DiceRollerUIModule(private val diceRollerFragment: DiceRollerFragment,
                         private val context: Context) {

    @Provides
    @Scopes.Fragment
    fun provideDiceRollerFragment(): DiceRollerFragment = diceRollerFragment

    @Provides
    @Scopes.Fragment
    fun provideDiceRollerUserActions(): DiceRollerView.UserActions = diceRollerFragment

    @Provides
    @Scopes.Fragment
    fun provideViewModelClass(): Class<DiceRollerViewModel> = DiceRollerViewModel::class.java

    @Provides
    @Scopes.Fragment
    fun provideViewModelFactory(factory: DiceRollerViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @Scopes.Fragment
    fun provideViewModel(diceRollerFragment: DiceRollerFragment,
                         clazz: Class<DiceRollerViewModel>,
                         factory: ViewModelProvider.Factory): DiceRollerViewModel {
        return ViewModelProviders.of(diceRollerFragment, factory)[clazz]
    }

    @Provides
    @Scopes.Fragment
    fun providePresenter(presenter: DiceRollerPresenter): BasePresenter = presenter

    @Provides
    @Scopes.Fragment
    fun provideBackPressureStrategy(): BackpressureStrategy = BackpressureStrategy.LATEST

    @Provides
    @Scopes.Fragment
    fun provideContext(): Context = context

}
