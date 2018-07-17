package com.apps.krishnakandula.dicerollerui.view.savetemplate

import android.arch.lifecycle.ViewModel
import com.apps.krishnakandula.common.view.BaseViewModel
import com.apps.krishnakandula.dicerollercore.Dice
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable

class SaveTemplateDialogViewModel : ViewModel(), BaseViewModel {

    val rolls: BehaviorRelay<List<List<Dice>>> = BehaviorRelay.createDefault(emptyList())

    override fun bindSources(): CompositeDisposable {
        // Do nothing
        return CompositeDisposable()
    }
}
