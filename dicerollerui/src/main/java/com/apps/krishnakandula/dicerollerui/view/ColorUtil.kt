package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.graphics.Color
import com.apps.krishnakandula.dicerollercore.Dice
import com.apps.krishnakandula.dicerollerui.R

fun Dice.color(context: Context): Int {
    return when (this) {
        is Dice.D2 -> context.getColor(R.color.teal)
        is Dice.D4 -> context.getColor(R.color.fuschia)
        is Dice.D6 -> context.getColor(R.color.deep_blue)
        is Dice.D8 -> context.getColor(R.color.sea_foam)
        is Dice.D10 -> context.getColor(R.color.orange)
        is Dice.D20 -> context.getColor(R.color.pink)
    }
}

fun List<Dice>.color(context: Context): Int {
    if (this.isEmpty()) return Color.BLACK
    return this.first().color(context)
}

fun Array<Dice>.color(context: Context): Int = this.toList().color(context)
