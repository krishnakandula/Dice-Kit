package com.apps.krishnakandula.dicerollerui.util

import android.support.v7.widget.LinearLayoutManager

fun LinearLayoutManager.isLastItemCompletelyVisible(): Boolean {
    if (itemCount == 0) return true
    val lastVisibleItem = findLastCompletelyVisibleItemPosition()
    return lastVisibleItem == itemCount - 1
}

fun LinearLayoutManager.scrollToBeginning() {
    if (itemCount > 0) {
        scrollToPosition(0)
    }
}
