package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.diceroller.Dice
import com.apps.krishnakandula.dicerollerui.R
import com.jakewharton.rxrelay2.BehaviorRelay
import kotlinx.android.synthetic.main.dice_item.view.*
import javax.inject.Inject

@Scopes.Feature
class DiceEquationAdapter @Inject constructor(private val context: Context)
    : RecyclerView.Adapter<DiceEquationAdapter.DiceEquationViewHolder>() {

    private val dice = BehaviorRelay.createDefault<List<Pair<Dice, Int?>>>(emptyList())

    fun setData(result: List<Pair<Dice, Int?>>) {
        this.dice.accept(result)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceEquationViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.dice_item, parent, false)
        return DiceEquationViewHolder(itemView)
    }

    override fun getItemCount(): Int = dice.value.size

    override fun onBindViewHolder(holder: DiceEquationViewHolder, position: Int) {
        if (position < itemCount) holder.bind(dice.value[position])
    }

    inner class DiceEquationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(result: Pair<Dice, Int?>) {
            when (result.first) {
                is Dice.D2 -> itemView.dice_item_dice_type_textview.text = "D2"
                is Dice.D4 -> itemView.dice_item_dice_type_textview.text = "D4"
                is Dice.D6 -> itemView.dice_item_dice_type_textview.text = "D6"
                is Dice.D8 -> itemView.dice_item_dice_type_textview.text = "D8"
                is Dice.D10 -> itemView.dice_item_dice_type_textview.text = "D10"
                is Dice.D20 -> itemView.dice_item_dice_type_textview.text = "D20"
            }

            if (result.second != null) {
                itemView.dice_item_dice_roll_textview.visibility = View.VISIBLE
                itemView.dice_item_dice_roll_textview.text = "${result.second}"
                itemView.dice_item_dice_roll_textview.setTextColor(context.getColor(R.color.white))
                itemView.dice_item_dice_type_textview.setTextColor(context.getColor(R.color.white))
                itemView.dice_item_card_view.setCardBackgroundColor(context.getColor(R.color.primary))
            } else {
                itemView.dice_item_dice_roll_textview.visibility = View.GONE
            }
        }
    }
}