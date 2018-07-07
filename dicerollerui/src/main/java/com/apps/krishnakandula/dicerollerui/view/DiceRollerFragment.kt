package com.apps.krishnakandula.dicerollerui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.diceroller.Dice
import com.apps.krishnakandula.diceroller.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.di.DaggerDiceRollerUIComponent
import com.apps.krishnakandula.dicerollerui.di.DiceRollerUIModule
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

class DiceRollerFragment : Fragment(), DiceRollerView, DiceRollerView.Actions {

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        DaggerDiceRollerUIComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .diceRollerUIModule(DiceRollerUIModule())
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dice_roller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupActions() {
    }

    override fun setupListeners() {
    }

    override fun onClickDiceBtn(): Flowable<Dice> {
    }

    override fun onClickEqualsBtn(): Flowable<List<Dice>> {
    }

    override fun onClickSaveBtn(): Flowable<List<Dice>> {
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }
}
