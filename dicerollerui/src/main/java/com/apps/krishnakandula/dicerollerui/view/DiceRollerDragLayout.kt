package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.dicerollerui.R
import kotlin.math.roundToInt

class DiceRollerDragLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private lateinit var dragHelper: ViewDragHelper
    private lateinit var previousRollsView: View
    private var initialPreviousRollsOffset = 0
    private var initialPreviousRollsLeft = 0
    private var previousScrollEvent: MotionEvent? = null
    var isDown = false

    companion object {
        private val LOG_TAG = DiceRollerDragLayout::class.java.simpleName
    }

    override fun onFinishInflate() {
        dragHelper = ViewDragHelper.create(this, 1.0f, DragHelperCallback())
        previousRollsView = findViewById(R.id.fragment_dice_roller_history_card_view)
        super.onFinishInflate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val totalHeight = MeasureSpec.getSize(heightMeasureSpec)
        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)

        val previousRollsChild = getChildAt(2)
        val equationEditChild = getChildAt(1)
        val dicePadChild = getChildAt(0)

        val equationEditHeightPercentage = 0.2
        val dicePadHeightPercentage = 0.4

        val previousRollsChildHeight = totalHeight
        val equationEditChildHeight = (totalHeight * equationEditHeightPercentage).roundToInt()
        val dicePadChildHeight = (totalHeight * dicePadHeightPercentage).roundToInt()
        previousRollsChild.measure(
                MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(previousRollsChildHeight, MeasureSpec.EXACTLY))
        equationEditChild.measure(
                MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(equationEditChildHeight, MeasureSpec.AT_MOST))
        dicePadChild.measure(
                MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(dicePadChildHeight, MeasureSpec.EXACTLY))
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = childCount

        // Draw dice pad -> equation edit -> previous rolls
        var currentBottom = bottom
        (0 until count - 1).forEach { i ->
            val child: View = getChildAt(i)
            if (child.visibility != View.GONE) {
                val childBottom = currentBottom
                val childTop = childBottom - child.measuredHeight
                currentBottom = childTop

                val childLeft = left + child.paddingStart
                val childRight = if (child.layoutParams.width == LayoutParams.WRAP_CONTENT) {
                     childLeft + child.measuredWidth
                } else {
                    right - child.paddingEnd
                }
                child.layout(childLeft, childTop, childRight, childBottom)
            }
        }

        // Previous rolls
        val prevRollsChild = getChildAt(2)
        val childLeft = left + prevRollsChild.paddingStart
        val childTop = currentBottom - prevRollsChild.measuredHeight
        val childRight = if (prevRollsChild.layoutParams.width == LayoutParams.WRAP_CONTENT) {
            childLeft + prevRollsChild.measuredWidth
        } else {
            right - prevRollsChild.paddingEnd
        }
        initialPreviousRollsOffset = childTop
        initialPreviousRollsLeft = childLeft
        prevRollsChild.layout(childLeft, childTop, childRight, currentBottom)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                // Save event as prev
                previousScrollEvent = ev
            }
            if (ev.action == MotionEvent.ACTION_UP && previousScrollEvent != null) {
                if (ev.y > previousScrollEvent!!.y && !isDown || ev.y < previousScrollEvent!!.y && isDown) {
                    onTouchEvent(ev)
                    return true
                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            dragHelper.processTouchEvent(this)
            return true
        }
        return false
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this)
    }

    fun resetPreviousRollsView() {
        dragHelper.smoothSlideViewTo(
                previousRollsView,
                initialPreviousRollsLeft,
                initialPreviousRollsOffset)
        isDown = false
        invalidate()
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == previousRollsView
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return (child.parent as View).measuredHeight - child.measuredHeight
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            if (yvel > 0) {
                dragHelper.settleCapturedViewAt(releasedChild.left, 0)
                isDown = true
            } else {
                dragHelper.settleCapturedViewAt(releasedChild.left, initialPreviousRollsOffset)
                isDown = false
            }
            invalidate()
        }


    }
}
