package com.apps.krishnakandula.diceroller

import com.apps.krishnakandula.common.Scopes
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import javax.inject.Inject

@Scopes.Application
class DiceRollerImpl @Inject constructor(): DiceRoller {

    private val prevRolls: MutableList<DiceRollResult> = mutableListOf()
    private val prevRollsRelay: BehaviorRelay<List<DiceRollResult>> = BehaviorRelay.createDefault(emptyList())

    override fun rollD2(addHistory: Boolean): DiceRollResult {
        val result = DiceRollResult.D2Result(roll(1, 2 + 1))
        if (addHistory) addToHistory(result)
        return result
    }

    override fun rollD4(addHistory: Boolean): DiceRollResult {
        val result = DiceRollResult.D4Result(roll(1, 4 + 1))
        if (addHistory) addToHistory(result)
        return result
    }

    override fun rollD6(addHistory: Boolean): DiceRollResult {
        val result = DiceRollResult.D6Result(roll(1, 6 + 1))
        if (addHistory) addToHistory(result)
        return result
    }

    override fun rollD8(addHistory: Boolean): DiceRollResult {
        val result = DiceRollResult.D8Result(roll(1, 8 + 1))
        if (addHistory) addToHistory(result)
        return result
    }

    override fun rollD10(addHistory: Boolean): DiceRollResult {
        val result = DiceRollResult.D10Result(roll(1, 10 + 1))
        if (addHistory) addToHistory(result)
        return result
    }

    override fun rollD20(addHistory: Boolean): DiceRollResult {
        val result = DiceRollResult.D20Result(roll(1, 20 + 1))
        if (addHistory) addToHistory(result)
        return result
    }

    override fun previousRolls(): Observable<List<DiceRollResult>> = prevRollsRelay

    private fun roll(min: Int, max: Int): Int {
        val range = max - min
        return ((Math.random() * range) + min).toInt()
    }

    private fun addToHistory(result: DiceRollResult) {
        prevRolls.add(result)
        prevRollsRelay.accept(prevRolls)
    }
}
