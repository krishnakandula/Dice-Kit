package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.dicerollerui.R
import kotlin.math.roundToInt

class DiceRollerDragLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private lateinit var dragHelper: ViewDragHelper
    private lateinit var previousRollsView: View
    private var initialPreviousRollsOffset = 0

    override fun onFinishInflate() {
        dragHelper = ViewDragHelper.create(this, 1.0f, DragHelperCallback())
        previousRollsView = findViewById(R.id.fragment_dice_roller_history_card_view)
        super.onFinishInflate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

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
                MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(previousRollsChildHeight, MeasureSpec.EXACTLY))
        equationEditChild.measure(
                MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(equationEditChildHeight, MeasureSpec.AT_MOST))
        dicePadChild.measure(
                MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(dicePadChildHeight, MeasureSpec.AT_MOST))
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
        prevRollsChild.layout(childLeft, childTop, childRight, currentBottom)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (ev != null) dragHelper.shouldInterceptTouchEvent(ev) else false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            dragHelper.processTouchEvent(it)
            return true
        }
        return false
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this)
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean = child == previousRollsView

        override fun getViewVerticalDragRange(child: View): Int {
            return (child.parent as View).measuredHeight - child.measuredHeight
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            val parent = releasedChild.parent as View
            if (yvel > 0) {
                dragHelper.settleCapturedViewAt(releasedChild.left, 0)
            } else {
                dragHelper.settleCapturedViewAt(releasedChild.left, initialPreviousRollsOffset)
            }
            invalidate()
        }
    }
}
