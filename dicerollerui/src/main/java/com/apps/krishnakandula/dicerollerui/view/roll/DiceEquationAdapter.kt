package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollerui.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dice_stack_itemview.view.*
import javax.inject.Inject

@Scopes.Fragment
class DiceEquationAdapter @Inject constructor(private val context: Context)
    : RecyclerView.Adapter<DiceEquationAdapter.DiceEquationViewHolder>() {

    private val roll: MutableList<List<Dice>> = emptyList<List<Dice>>().toMutableList()

    fun setData(newRoll: List<List<Dice>>,
                scrollCallback: (lastIndex: Int) -> Unit) {
        updateRollObservable(newRoll)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { diffResult ->
                    diffResult.dispatchUpdatesTo(this)
                    roll.clear()
                    roll.addAll(newRoll)
                    scrollCallback(roll.lastIndex)
                })
    }

    private fun updateRollObservable(newRoll: List<List<Dice>>) = Observable.just(DiffUtil.calculateDiff(DiceEquationDiffCallback(roll, newRoll)))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceEquationViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.dice_stack_itemview, parent, false)
        return DiceEquationViewHolder(itemView)
    }

    override fun getItemCount(): Int = roll.size

    override fun onBindViewHolder(holder: DiceEquationViewHolder, position: Int) {
        if (position < itemCount) holder.bind(roll[position])
    }

    inner class DiceEquationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dice: List<Dice>) {
            val adapter = DiceEquationStackAdapter(context)
            itemView.dice_roll_stack_itemview_card_stack_view.setAdapter(adapter)
            itemView.dice_roll_stack_itemview_count_text_view.text = "${dice.size}"
            adapter.setData(dice)
        }
    }
}
