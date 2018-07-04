package com.apps.krishnakandula.diceroller.roller

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.diceroller.Dice
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@Scopes.Application
class DiceRollerImpl @Inject constructor() : DiceRoller {

    private val prevs: MutableList<List<Pair<Dice, Int>>> = mutableListOf()

    private val prevRollsRelay: BehaviorRelay<List<List<Pair<Dice, Int>>>> = BehaviorRelay.createDefault(emptyList())

    override fun roll(dice: List<Dice>): Single<List<Pair<Dice, Int>>> {
        return Single.just(dice.map { die ->
            when (die) {
                is Dice.D2 -> Pair(die, roll(0, 2 + 1))
                is Dice.D4 -> Pair(die, roll(0, 4 + 1))
                is Dice.D6 -> Pair(die, roll(0, 6 + 1))
                is Dice.D8 -> Pair(die, roll(0, 8 + 1))
                is Dice.D10 -> Pair(die, roll(0, 10 + 1))
                is Dice.D20 -> Pair(die, roll(0, 20 + 1))
            }
        })
    }

    override fun previousRolls(): Observable<List<List<Pair<Dice, Int>>>> = prevRollsRelay

    private fun roll(min: Int, max: Int): Int {
        val range = max - min
        return ((Math.random() * range) + min).toInt()
    }

    override fun addToHistory(results: List<Pair<Dice, Int>>) {
        prevs.add(results)
        prevRollsRelay.accept(prevs)
    }
}
