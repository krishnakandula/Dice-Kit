package com.apps.krishnakandula.diceroller

sealed class DiceRollResult {

    open class Result(val value: Int) : DiceRollResult()

    data class D2Result(val d2Roll: Int) : Result(d2Roll)

    data class D4Result(val d4Roll: Int): Result(d4Roll)

    data class D6Result(val d6Roll: Int): Result(d6Roll)

    data class D8Result(val d8Roll: Int): Result(d8Roll)

    data class D10Result(val d10Roll: Int): Result(d10Roll)

    data class D20Result(val d20Roll: Int): Result(d20Roll)
}