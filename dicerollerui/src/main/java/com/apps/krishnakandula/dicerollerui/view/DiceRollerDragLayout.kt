package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.apps.krishnakandula.dicerollerui.R
import kotlin.math.roundToInt

class DiceRollerDragLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private lateinit var dragHelper: ViewDragHelper
    private lateinit var previousRollsView: View
    private var initialPreviousRollsOffset = 0
    private var initialPreviousRollsLeft = 0
    private val velocityTracker = VelocityTracker.obtain()
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
            val gestureDetector = GestureDetector(context, gestureListener)
            val onEvent = gestureDetector.onTouchEvent(ev)
            Log.d(LOG_TAG, "onEvent: $onEvent")
            return onEvent
        }
        return false
    }

    private val gestureListener: GestureDetector.OnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            // Check if velocity is vertical
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                // If layout manager is fully down, accept scroll event
                val layoutManager = previousRollsView
                        .findViewById<RecyclerView>(R.id.fragment_dice_roller_history_recycler_view)
                        .layoutManager as LinearLayoutManager
                val lastCompletelyVis = layoutManager.findLastCompletelyVisibleItemPosition()
                if (layoutManager.findLastCompletelyVisibleItemPosition() <= 0) {
//                    Log.d(LOG_TAG, "IsDown: $isDown     DistanceY: $distanceY   lastCompletelyVis: $lastCompletelyVis")
                    return (isDown && distanceY > 0) || (!isDown && distanceY < 0)
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    private fun isPrevRollsViewTarget(event: MotionEvent, prevRollsView: View): Boolean {
        val previousRollsLocation = IntArray(2)
        prevRollsView.getLocationOnScreen(previousRollsLocation)
        val lowerLimit = previousRollsLocation[1]
        val upperLimit = previousRollsLocation[1] + prevRollsView.measuredHeight
        val y = event.rawY.roundToInt()

        Log.d(LOG_TAG, "y: $y   lowerLimit: $lowerLimit     upperLimit: $upperLimit")
        return y in lowerLimit..upperLimit
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            Log.d(LOG_TAG, "Processing touch event")
            if (isPrevRollsViewTarget(this, previousRollsView)) {
                dragHelper.processTouchEvent(this)
                return true
            }
        }
        return super.onTouchEvent(event)
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
            Log.v(LOG_TAG, "Child id: ${child.id}   history: ${R.id.fragment_dice_roller_history_recycler_view}")
            return child.id == R.id.fragment_dice_roller_history_recycler_view
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
