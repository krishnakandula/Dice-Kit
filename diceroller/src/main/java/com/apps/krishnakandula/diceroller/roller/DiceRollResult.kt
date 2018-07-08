package com.apps.krishnakandula.diceroller.roller

import com.apps.krishnakandula.diceroller.Dice

data class DiceRollResult(val dice: List<Pair<Dice, Int>>,
                          val result: Int)
