package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollerui.R
import com.jakewharton.rxrelay2.BehaviorRelay
import kotlinx.android.synthetic.main.dice_stack_itemview.view.*
import javax.inject.Inject

@Scopes.Fragment
class DiceEquationAdapter @Inject constructor(private val context: Context)
    : RecyclerView.Adapter<DiceEquationAdapter.DiceEquationViewHolder>() {

    private val dice = BehaviorRelay.createDefault<List<List<Dice>>>(emptyList())

    fun setData(result: List<List<Dice>>) {
        this.dice.accept(result)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceEquationViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.dice_stack_itemview, parent, false)
        return DiceEquationViewHolder(itemView)
    }

    override fun getItemCount(): Int = dice.value.size

    override fun onBindViewHolder(holder: DiceEquationViewHolder, position: Int) {
        if (position < itemCount) holder.bind(dice.value[position])
    }

    inner class DiceEquationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dice: List<Dice>) {
            val adapter = DiceEquationStackAdapter(context) { itemView.dice_stack_itemview_card_stack_view.reverse() }
            itemView.dice_stack_itemview_card_stack_view.setAdapter(adapter)
            itemView.dice_stack_itemview_count_text_view.text = "${dice.size}"
            adapter.setData(dice)
        }
    }
}
