package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
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
        is Dice.Plus1 -> context.getColor(R.color.light_blue)
        is Dice.Minus1 -> context.getColor(R.color.light_red)
    }
}

fun List<Dice>.color(context: Context): Int {
    if (this.isEmpty()) return Color.BLACK
    return this.first().color(context)
}

fun Array<Dice>.color(context: Context): Int = this.toList().color(context)

fun Dice.drawable(context: Context): Drawable {
    return when (this) {
        is Dice.D2 -> context.getDrawable(R.drawable.dice_d2)
        is Dice.D4 -> context.getDrawable(R.drawable.dice_d4)
        is Dice.D6 -> context.getDrawable(R.drawable.dice_d6)
        is Dice.D8 -> context.getDrawable(R.drawable.dice_d8)
        is Dice.D10 -> context.getDrawable(R.drawable.dice_d10)
        is Dice.D20 -> context.getDrawable(R.drawable.dice_d20)
        is Dice.Minus1 -> context.getDrawable(R.drawable.dice_minus1)
        is Dice.Plus1 -> context.getDrawable(R.drawable.dice_plus1)
    }
}
