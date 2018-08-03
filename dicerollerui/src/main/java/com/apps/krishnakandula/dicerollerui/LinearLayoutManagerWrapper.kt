package com.apps.krishnakandula.dicerollerui

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

class LinearLayoutManagerWrapper : LinearLayoutManager {

    private var verticalScrollEnabled = true
    private var horizontalScrollEnabled = true

    constructor(context: Context) : super(context)

    constructor(context: Context,
                orientation: Int,
                reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    constructor(context: Context,
                attrs: AttributeSet,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun supportsPredictiveItemAnimations(): Boolean = false

    override fun canScrollVertically(): Boolean = verticalScrollEnabled && super.canScrollVertically()

    override fun canScrollHorizontally(): Boolean = horizontalScrollEnabled && super.canScrollHorizontally()

    fun enableVerticalScroll() { verticalScrollEnabled = true }

    fun disableVerticalScroll() { verticalScrollEnabled = false }

    fun enableHorizontalScroll() { horizontalScrollEnabled = true }

    fun disableHorizontalScroll() { horizontalScrollEnabled = false }
}
