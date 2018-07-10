package com.apps.krishnakandula.dicerollerui.view.roll

import android.support.v7.util.DiffUtil
import com.apps.krishnakandula.diceroller.roller.DiceRollResult

class PreviousRollsDiffCallback(private val oldItems: List<DiceRollResult>,
                                private val newItems: List<DiceRollResult>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        if (oldItem.result != newItem.result) return false
        if (oldItem.dice.size != newItem.dice.size) return false
        oldItem.dice.forEachIndexed { index, pair ->
            val newItemPair = newItem.dice[index]
            if (newItemPair.second != pair.second) return false
            if (newItemPair.first.javaClass != pair.first.javaClass) return false
        }

        return true
    }
}
