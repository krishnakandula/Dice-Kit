package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.view.drawable
import kotlinx.android.synthetic.main.dice_item.view.*

class PreviousRollsStackAdapter (context: Context,
                                 private val onClickStack: () -> Unit) : ArrayAdapter<Dice>(context, 0, mutableListOf()) {

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
        itemView?.dice_item_dice_imageview?.setImageDrawable(dice.drawable(this.context))
        return itemView!!
    }

}
