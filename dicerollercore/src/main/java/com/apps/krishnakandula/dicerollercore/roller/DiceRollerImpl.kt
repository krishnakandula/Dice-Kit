package com.apps.krishnakandula.dicerollercore.roller

import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.common.util.Result
import com.apps.krishnakandula.dicerollercore.Dice
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@Scopes.Application
class DiceRollerImpl @Inject constructor() : DiceRoller {

    private val prevs: MutableList<DiceRollResult> = mutableListOf()

    private val prevRollsRelay: BehaviorRelay<List<DiceRollResult>> = BehaviorRelay.createDefault(emptyList())

    override fun roll(dice: List<Dice>): Single<out Result<DiceRollResult>> {
        return Single.create { emitter ->
            val rolls = dice.map { die ->
                when (die) {
                    is Dice.D2 -> Pair(die, roll(1, 2 + 1))
                    is Dice.D4 -> Pair(die, roll(1, 4 + 1))
                    is Dice.D6 -> Pair(die, roll(1, 6 + 1))
                    is Dice.D8 -> Pair(die, roll(1, 8 + 1))
                    is Dice.D10 -> Pair(die, roll(1, 10 + 1))
                    is Dice.D20 -> Pair(die, roll(1, 20 + 1))
                }
            }
            emitter.onSuccess(Result.Success(DiceRollResult(rolls, rolls.sumBy { it.second })))
        }
    }

    override fun previousRolls(): Observable<List<DiceRollResult>> = prevRollsRelay

    private fun roll(min: Int, max: Int): Int {
        val range = max - min
        return ((Math.random() * range) + min).toInt()
    }

    override fun addToHistory(result: DiceRollResult): Completable = Completable.create {
        prevs.add(result)
        prevRollsRelay.accept(prevs)
        it.onComplete()
    }
}
