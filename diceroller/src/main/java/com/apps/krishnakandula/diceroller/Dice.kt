package com.apps.krishnakandula.diceroller

sealed class Dice {

    class D2 : Dice()

    class D4 : Dice()

    class D6 : Dice()

    class D8 : Dice()

    class D10 : Dice()

    class D20 : Dice()

}
