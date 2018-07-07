package com.apps.krishnakandula.dicerollerui.di

import android.arch.lifecycle.ViewModelProviders
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.dicerollerui.view.DiceRollerFragment
import com.apps.krishnakandula.dicerollerui.view.DiceRollerPresenter
import com.apps.krishnakandula.dicerollerui.view.DiceRollerView
import com.apps.krishnakandula.dicerollerui.view.DiceRollerViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.BackpressureStrategy

@Module
class DiceRollerUIModule(private val diceRollerFragment: DiceRollerFragment) {

    @Provides
    @Scopes.Feature
    fun provideDiceRollerFragment(): DiceRollerFragment = diceRollerFragment

    @Provides
    @Scopes.Feature
    fun provideDiceRollerViewActions(): DiceRollerView.Actions = diceRollerFragment

    @Provides
    @Scopes.Feature
    fun provideViewModelClass(): Class<DiceRollerViewModel> = DiceRollerViewModel::class.java

    @Provides
    @Scopes.Feature
    fun provideViewModel(diceRollerFragment: DiceRollerFragment,
                         clazz: Class<DiceRollerViewModel>): DiceRollerViewModel {
        return ViewModelProviders.of(diceRollerFragment)[clazz]
    }

    @Provides
    @Scopes.Feature
    fun providePresenter(presenter: DiceRollerPresenter): BasePresenter = presenter

    @Provides
    @Scopes.Feature
    fun provideBackPressureStrategy(): BackpressureStrategy = BackpressureStrategy.LATEST
}
