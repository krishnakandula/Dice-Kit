package com.apps.krishnakandula.dicerollerui.view.roll

import android.support.v7.util.DiffUtil
import com.apps.krishnakandula.dicerollercore.Dice


class DiceEquationDiffCallback(private val oldRolls: List<List<Dice>>,
                               private val newRolls: List<List<Dice>>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRolls[oldItemPosition] == newRolls[newItemPosition]
    }

    override fun getOldListSize(): Int = oldRolls.size

    override fun getNewListSize(): Int = newRolls.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRoll = oldRolls[oldItemPosition]
        val newRoll = newRolls[newItemPosition]

        if (oldRoll.size != newRoll.size) return false
        newRoll.forEachIndexed { index, newRollDice ->
            if (newRollDice != oldRoll[index]) return false
        }
        return true
    }
}
