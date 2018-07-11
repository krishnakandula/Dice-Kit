package com.apps.krishnakandula.dicerollercore.roller

import com.apps.krishnakandula.dicerollercore.Dice


data class DiceRollResult(val dice: List<Pair<Dice, Int>>,
                          val result: Int)
