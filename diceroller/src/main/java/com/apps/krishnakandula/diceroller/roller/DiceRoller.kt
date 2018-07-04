package com.apps.krishnakandula.diceroller.roller

import com.apps.krishnakandula.diceroller.Dice
import io.reactivex.Observable
import io.reactivex.Single

interface DiceRoller {

    fun addToHistory(results: List<Pair<Dice, Int>>)

    fun roll(dice: List<Dice>): Single<List<Pair<Dice, Int>>>

    fun previousRolls(): Observable<List<List<Pair<Dice, Int>>>>

}
