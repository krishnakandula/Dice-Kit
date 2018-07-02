package com.apps.krishnakandula.diceroller

import io.reactivex.Observable

interface DiceRoller {

    fun rollD2(addHistory: Boolean): DiceRollResult

    fun rollD4(addHistory: Boolean): DiceRollResult

    fun rollD6(addHistory: Boolean): DiceRollResult

    fun rollD8(addHistory: Boolean): DiceRollResult

    fun rollD10(addHistory: Boolean): DiceRollResult

    fun rollD20(addHistory: Boolean): DiceRollResult

    fun previousRolls(): Observable<List<DiceRollResult>>

}