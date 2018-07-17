package com.apps.krishnakandula.dicerollercore

sealed class Dice {

    class D2 : Dice()

    class D4 : Dice()

    class D6 : Dice()

    class D8 : Dice()

    class D10 : Dice()

    class D20 : Dice()

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Dice) return false
        return other::class.java == this::class.java
    }
}
