package com.apps.krishnakandula.diceroller.template

import com.apps.krishnakandula.diceroller.Dice

data class Template(val id: String,
                    val name: String,
                    val rolls: List<Dice>)
