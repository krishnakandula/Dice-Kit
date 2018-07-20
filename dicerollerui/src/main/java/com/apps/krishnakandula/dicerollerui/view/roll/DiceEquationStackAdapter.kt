package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.view.color
import kotlinx.android.synthetic.main.dice_item.view.*


class DiceEquationStackAdapter(context: Context) : ArrayAdapter<Dice>(context, 0, mutableListOf()) {

    fun setData(data: List<Dice>) {
        this.clear()
        this.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val dice = getItem(position)
        var itemView: View? = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.dice_item, parent, false)
        }
        when (dice) {
            is Dice.D2 -> itemView?.dice_item_dice_type_textview?.text = "D2"
            is Dice.D4 -> itemView?.dice_item_dice_type_textview?.text = "D4"
            is Dice.D6 -> itemView?.dice_item_dice_type_textview?.text = "D6"
            is Dice.D8 -> itemView?.dice_item_dice_type_textview?.text = "D8"
            is Dice.D10 -> itemView?.dice_item_dice_type_textview?.text = "D10"
            is Dice.D20 -> itemView?.dice_item_dice_type_textview?.text = "D20"
        }
        itemView?.dice_item_dice_roll_textview?.visibility = View.GONE
        itemView?.dice_item_card_view?.setCardBackgroundColor(dice.color(context))
        itemView?.dice_item_dice_type_textview?.setTextColor(Color.WHITE)
        return itemView!!
    }

}
