package com.apps.krishnakandula.dicerollerui.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.apps.krishnakandula.dicerollerui.LinearLayoutManagerWrapper
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.util.isLastItemCompletelyVisible
import com.apps.krishnakandula.dicerollerui.util.scrollToBeginning
import kotlin.math.roundToInt

class DiceRollerDragLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private lateinit var dragHelper: ViewDragHelper
    private lateinit var previousRollsView: View
    private lateinit var previousRollsRecyclerView: RecyclerView
    private var previousRollsViewLayoutManager: LinearLayoutManagerWrapper? = null
    private var initialPreviousRollsOffset = 0
    private var initialPreviousRollsLeft = 0
    private var isDown = false

    companion object {
        private val LOG_TAG = DiceRollerDragLayout::class.java.simpleName
    }

    override fun onFinishInflate() {
        dragHelper = ViewDragHelper.create(this, 1.0f, DragHelperCallback())
        previousRollsView = findViewById(R.id.fragment_dice_roller_history_card_view)
        previousRollsRecyclerView = findViewById(R.id.fragment_dice_roller_history_recycler_view)
        super.onFinishInflate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val totalHeight = MeasureSpec.getSize(heightMeasureSpec)
        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)

        val previousRollsChild = getChildAt(2)
        val equationEditChild = getChildAt(1)
        val dicePadChild = getChildAt(0)

        val equationEditHeightPercentage = 0.2
        val dicePadHeightPercentage = 0.55

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
            // Check if touch event was in previous rolls view
            if (dragHelper.shouldInterceptTouchEvent(ev)) {
                if (ev.action == MotionEvent.ACTION_MOVE && ev.historySize > 0) {
                    val deltaY = ev.y - ev.getHistoricalY(0)
                    val deltaX = ev.x - ev.getHistoricalX(0)
                    if (Math.abs(deltaY) > Math.abs(deltaX)
                            && getPrevRollsViewLayoutManager().isLastItemCompletelyVisible()) {
                        return (isDown && deltaY < 0) || (!isDown && deltaY > 0)
                    }

                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this)
    }


    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isDown = isDown
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            setIsDown(state.isDown)
        } else super.onRestoreInstanceState(state)

        if (isDown) scrollPreviousRollsViewDown() else getPrevRollsViewLayoutManager().disableVerticalScroll()
    }

    private fun getPrevRollsViewLayoutManager(): LinearLayoutManagerWrapper {
        if (this.previousRollsViewLayoutManager == null) {
            previousRollsViewLayoutManager = previousRollsRecyclerView.layoutManager as LinearLayoutManagerWrapper
        }
        return previousRollsViewLayoutManager!!
    }

    fun onBackPressed(superOnBackPressed: () -> Unit) {
        if (isDown) {
            if (getPrevRollsViewLayoutManager().isLastItemCompletelyVisible()) {
                scrollPreviousRollsViewUp()
            } else {
                getPrevRollsViewLayoutManager().scrollToBeginning()
            }
        } else superOnBackPressed()
    }

    private fun scrollPreviousRollsViewDown() {
        dragHelper.smoothSlideViewTo(
                previousRollsView,
                initialPreviousRollsLeft,
                1) // For some reason, having 0 here causes nothing to happen
        setIsDown(true)
        invalidate()
    }

    private fun scrollPreviousRollsViewUp() {
        dragHelper.smoothSlideViewTo(
                previousRollsView,
                initialPreviousRollsLeft,
                initialPreviousRollsOffset)
        setIsDown(false)
        invalidate()
    }

    private fun setIsDown(down: Boolean) {
        isDown = down
        if (isDown) {
            getPrevRollsViewLayoutManager().enableVerticalScroll()
        } else {
            getPrevRollsViewLayoutManager().disableVerticalScroll()
        }
    }

    internal class SavedState : BaseSavedState {

        var isDown: Boolean = false

        constructor(source: Parcel) : super(source) {
            isDown = source.readByte().toInt() == 1
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeByte((if (isDown) 1 else 0).toByte())
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean = child == previousRollsView

        override fun getViewVerticalDragRange(child: View): Int {
            return child.measuredHeight
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return if (dy > 0) {
                Math.min(getTop(), top)
            } else Math.max(initialPreviousRollsOffset, top)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            setIsDown(if (yvel > 0) {
                dragHelper.settleCapturedViewAt(releasedChild.left, 0)
                true
            } else {
                dragHelper.settleCapturedViewAt(releasedChild.left, initialPreviousRollsOffset)
                false
            })
            invalidate()
        }
    }
}
