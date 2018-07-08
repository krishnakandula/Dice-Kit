package com.apps.krishnakandula.diceroller.roller

import com.apps.krishnakandula.common.util.Result
import com.apps.krishnakandula.diceroller.Dice
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface DiceRoller {

    fun addToHistory(result: DiceRollResult): Completable

    fun roll(dice: List<Dice>): Single<out Result<DiceRollResult>>

    fun previousRolls(): Observable<List<DiceRollResult>>

}
