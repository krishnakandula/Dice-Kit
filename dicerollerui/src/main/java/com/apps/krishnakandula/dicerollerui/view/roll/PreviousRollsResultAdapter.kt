package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollerui.R
import kotlinx.android.synthetic.main.previous_rolls_dice_itemview.view.*

class PreviousRollsResultAdapter(private val context: Context)
    : RecyclerView.Adapter<PreviousRollsResultAdapter.PreviousRollsResultViewHolder>() {

    private val results: MutableList<List<Pair<Dice, Int>>> = mutableListOf()

    fun setData(data: List<List<Pair<Dice, Int>>>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousRollsResultViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.previous_rolls_dice_itemview, parent, false)
        return PreviousRollsResultViewHolder(itemView)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: PreviousRollsResultViewHolder, position: Int) {
        if (position <= results.lastIndex) holder.bind(results[position])
    }

    inner class PreviousRollsResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(roll: List<Pair<Dice, Int>>) {
            val adapter = DiceEquationStackAdapter(context)
            itemView.dice_roll_stack_itemview_card_stack_view.setAdapter(adapter)
            itemView.dice_roll_stack_itemview_count_text_view.text = "${roll.size}"
            itemView.dice_roll_stack_itemview_total_text_view.text = " = ${roll.sumBy { it.second }}"
            adapter.setData(roll.map { it.first })
        }
    }
}
