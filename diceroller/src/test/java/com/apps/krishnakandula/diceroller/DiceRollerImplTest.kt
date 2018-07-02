package com.apps.krishnakandula.diceroller

import org.junit.Assert.assertTrue
import org.junit.Test

internal class DiceRollerImplTest {

    private val diceRoller: DiceRollerImpl = DiceRollerImpl()

    @Test
    fun testRollD2() {
        (0..100).forEach { assertTrue((diceRoller.rollD2() as DiceRollResult.Result).value in 1..2) }
    }

    @Test
    fun testRollD4() {
        (0..100).forEach { assertTrue((diceRoller.rollD4() as DiceRollResult.Result).value in 1..4) }
    }

    @Test
    fun testRollD6() {
        (0..100).forEach { assertTrue((diceRoller.rollD6() as DiceRollResult.Result).value in 1..6) }
    }

    @Test
    fun testRollD8() {
        (0..100).forEach { assertTrue((diceRoller.rollD8() as DiceRollResult.Result).value in 1..8) }
    }

    @Test
    fun testRollD10() {
        (0..100).forEach { assertTrue((diceRoller.rollD10() as DiceRollResult.Result).value in 1..10) }
    }

    @Test
    fun testRollD20() {
        (0..100).forEach { assertTrue((diceRoller.rollD20() as DiceRollResult.Result).value in 1..20) }
    }
}
