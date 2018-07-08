package com.apps.krishnakandula.dicerollerui.di

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.view.BasePresenter
import com.apps.krishnakandula.diceroller.roller.DiceRoller
import com.apps.krishnakandula.dicerollerui.view.*
import dagger.Module
import dagger.Provides
import io.reactivex.BackpressureStrategy

@Module
class DiceRollerUIModule(private val diceRollerFragment: DiceRollerFragment,
                         private val context: Context) {

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
    fun provideViewModelFactory(factory: DiceRollerViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @Scopes.Feature
    fun provideViewModel(diceRollerFragment: DiceRollerFragment,
                         clazz: Class<DiceRollerViewModel>,
                         factory: ViewModelProvider.Factory): DiceRollerViewModel {
        return ViewModelProviders.of(diceRollerFragment, factory)[clazz]
    }

    @Provides
    @Scopes.Feature
    fun providePresenter(presenter: DiceRollerPresenter): BasePresenter = presenter

    @Provides
    @Scopes.Feature
    fun provideBackPressureStrategy(): BackpressureStrategy = BackpressureStrategy.LATEST

    @Provides
    @Scopes.Feature
    fun provideContext(): Context = context

}
