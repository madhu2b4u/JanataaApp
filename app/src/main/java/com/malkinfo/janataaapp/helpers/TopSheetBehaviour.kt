package com.malkinfo.janataaapp.helpers

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.ParcelableCompat
import androidx.core.os.ParcelableCompatCreatorCallbacks
import androidx.core.view.MotionEventCompat
import androidx.core.view.NestedScrollingChild
import androidx.core.view.VelocityTrackerCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.google.android.material.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.ref.WeakReference


/**
 * An interaction behavior plugin for a child view of [CoordinatorLayout] to make it work as
 * a bottom sheet.
 */
class TopSheetBehavior<V : View?> : CoordinatorLayout.Behavior<V> {
    /**
     * Callback for monitoring events about top sheets.
     */
    abstract class TopSheetCallback {
        /**
         * Called when the top sheet changes its state.
         *
         * @param topSheet The top sheet view.
         * @param newState The new state. This will be one of [.STATE_DRAGGING],
         * [.STATE_SETTLING], [.STATE_EXPANDED],
         * [.STATE_COLLAPSED], or [.STATE_HIDDEN].
         */
        abstract fun onStateChanged(
            topSheet: View,
            @State newState: Int
        )

        /**
         * Called when the top sheet is being dragged.
         *
         * @param topSheet    The top sheet view.
         * @param slideOffset The new offset of this top sheet within its range, from 0 to 1
         * when it is moving upward, and from 0 to -1 when it moving downward.
         * @param isOpening   detect showing
         */
        abstract fun onSlide(
            topSheet: View,
            slideOffset: Float,
            isOpening: Boolean?
        )
    }

    /**
     * @hide
     */
    @IntDef(
        STATE_EXPANDED,
        STATE_COLLAPSED,
        STATE_DRAGGING,
        STATE_SETTLING,
        STATE_HIDDEN
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class State

    private var hideable: Boolean=false
    private var mMaximumVelocity = 0f
    private var mPeekHeight = 0
    private var mMinOffset = 0
    private var mMaxOffset = 0
    /**
     * Gets whether this top sheet can hide when it is swiped down.
     *
     * @return `true` if this top sheet can hide.
     * @attr ref com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    /**
     * Sets whether this top sheet can hide when it is swiped down.
     *
     * @param hideable `true` to make this top sheet hideable.
     * @attr ref com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    var isHideable = false
    /**
     * Sets whether this top sheet should skip the collapsed state when it is being hidden
     * after it is expanded once.
     *
     * @return Whether the top sheet should skip the collapsed state.
     * @attr ref com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    /**
     * Sets whether this top sheet should skip the collapsed state when it is being hidden
     * after it is expanded once. Setting this to true has no effect unless the sheet is hideable.
     *
     * @param skipCollapsed True if the top sheet should skip the collapsed state.
     * @attr ref com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    var skipCollapsed = false

    @State
    private var mState = STATE_COLLAPSED
    private var mViewDragHelper: ViewDragHelper? = null
    private var mIgnoreEvents = false
    private var mLastNestedScrollDy = 0
    private var mNestedScrolled = false
    private var mParentHeight = 0
    private var mViewRef: WeakReference<V?>? = null
    private var mNestedScrollingChildRef: WeakReference<View?>? = null
    private var mCallback: TopSheetCallback? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mActivePointerId = 0
    private var mInitialY = 0
    private var mTouchingScrollingChild = false

    /**
     * Default constructor for instantiating TopSheetBehaviors.
     */
    constructor() {}

    /**
     * Default constructor for inflating TopSheetBehaviors from layout.
     *
     * @param context The [Context].
     * @param attrs   The [AttributeSet].
     */
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomSheetBehavior_Layout
        )
        peekHeight = a.getDimensionPixelSize(
            R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight,
            0
        )
        hideable = a.getBoolean(
            R.styleable.BottomSheetBehavior_Layout_behavior_hideable,
            false
        )
        skipCollapsed = a.getBoolean(
            R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed,
            false
        )
        a.recycle()
        val configuration = ViewConfiguration.get(context)
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity.toFloat()
    }

    override fun onSaveInstanceState(parent: CoordinatorLayout, child: V): Parcelable? {
        return SavedState(
            super.onSaveInstanceState(parent, child),
            mState
        )
    }

    override fun onRestoreInstanceState(
        parent: CoordinatorLayout,
        child: V,
        state: Parcelable
    ) {
        val ss = state as SavedState
        super.onRestoreInstanceState(parent, child, ss.superState)
        // Intermediate states are restored as collapsed state
        mState = if (ss.state == STATE_DRAGGING || ss.state == STATE_SETTLING) {
            STATE_COLLAPSED
        } else {
            ss.state
        }
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: V,
        layoutDirection: Int
    ): Boolean {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child!!)) {
            ViewCompat.setFitsSystemWindows(child, true)
        }
        val savedTop = child!!.top
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection)
        // Offset the top sheet
        mParentHeight = parent.height
        mMinOffset = Math.max(-child.height, -(child.height - mPeekHeight))
        mMaxOffset = 0
        if (mState == STATE_EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, mMaxOffset)
        } else if (isHideable && mState == STATE_HIDDEN) {
            ViewCompat.offsetTopAndBottom(child, -child.height)
        } else if (mState == STATE_COLLAPSED) {
            ViewCompat.offsetTopAndBottom(child, mMinOffset)
        } else if (mState == STATE_DRAGGING || mState == STATE_SETTLING) {
            ViewCompat.offsetTopAndBottom(child, savedTop - child.top)
        }
        if (mViewDragHelper == null) {
            mViewDragHelper = ViewDragHelper.create(parent, mDragCallback)
        }
        mViewRef = WeakReference(child)
        mNestedScrollingChildRef =
            WeakReference(findScrollingChild(child))
        return true
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        if (!child!!.isShown) {
            return false
        }
        val action = MotionEventCompat.getActionMasked(event)
        // Record the velocity
        if (action == MotionEvent.ACTION_DOWN) {
            reset()
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mTouchingScrollingChild = false
                mActivePointerId = MotionEvent.INVALID_POINTER_ID
                // Reset the ignore flag
                if (mIgnoreEvents) {
                    mIgnoreEvents = false
                    return false
                }
            }
            MotionEvent.ACTION_DOWN -> {
                val initialX = event.x.toInt()
                mInitialY = event.y.toInt()
                val scroll = mNestedScrollingChildRef!!.get()
                if (scroll != null && parent.isPointInChildBounds(scroll, initialX, mInitialY)) {
                    mActivePointerId = event.getPointerId(event.actionIndex)
                    mTouchingScrollingChild = true
                }
                mIgnoreEvents = mActivePointerId == MotionEvent.INVALID_POINTER_ID &&
                        !parent.isPointInChildBounds(child, initialX, mInitialY)
            }
        }
        if (!mIgnoreEvents && mViewDragHelper!!.shouldInterceptTouchEvent(event)) {
            return true
        }
        // We have to handle cases that the ViewDragHelper does not capture the top sheet because
        // it is not the top most view of its parent. This is not necessary when the touch event is
        // happening over the scrolling content as nested scrolling logic handles that case.
        val scroll = mNestedScrollingChildRef!!.get()
        return action == MotionEvent.ACTION_MOVE && scroll != null &&
                !mIgnoreEvents && mState != STATE_DRAGGING &&
                !parent.isPointInChildBounds(scroll, event.x.toInt(), event.y.toInt()) && Math.abs(
            mInitialY - event.y
        ) > mViewDragHelper!!.touchSlop
    }

    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        if (!child!!.isShown) {
            return false
        }
        val action = MotionEventCompat.getActionMasked(event)
        if (mState == STATE_DRAGGING && action == MotionEvent.ACTION_DOWN) {
            return true
        }
        if (mViewDragHelper != null) {
            //no crash
            mViewDragHelper!!.processTouchEvent(event)
            // Record the velocity
            if (action == MotionEvent.ACTION_DOWN) {
                reset()
            }
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain()
            }
            mVelocityTracker!!.addMovement(event)
            // The ViewDragHelper tries to capture only the top-most View. We have to explicitly tell it
            // to capture the top sheet in case it is not captured and the touch slop is passed.
            if (action == MotionEvent.ACTION_MOVE && !mIgnoreEvents) {
                if (Math.abs(mInitialY - event.y) > mViewDragHelper!!.touchSlop) {
                    mViewDragHelper!!.captureChildView(
                        child,
                        event.getPointerId(event.actionIndex)
                    )
                }
            }
        }
        return !mIgnoreEvents
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: V,
        directTargetChild: View, target: View, nestedScrollAxes: Int
    ): Boolean {
        mLastNestedScrollDy = 0
        mNestedScrolled = false
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int,
        dy: Int, consumed: IntArray
    ) {
        val scrollingChild = mNestedScrollingChildRef!!.get()
        if (target !== scrollingChild) {
            return
        }
        val currentTop = child!!.top
        val newTop = currentTop - dy
        if (dy > 0) { // Upward
            if (!ViewCompat.canScrollVertically(target, 1)) {
                if (newTop >= mMinOffset || isHideable) {
                    consumed[1] = dy
                    ViewCompat.offsetTopAndBottom(child, -dy)
                    setStateInternal(STATE_DRAGGING)
                } else {
                    consumed[1] = currentTop - mMinOffset
                    ViewCompat.offsetTopAndBottom(child, -consumed[1])
                    setStateInternal(STATE_COLLAPSED)
                }
            }
        } else if (dy < 0) { // Downward
            // Negative to check scrolling up, positive to check scrolling down
            if (newTop < mMaxOffset) {
                consumed[1] = dy
                ViewCompat.offsetTopAndBottom(child, -dy)
                setStateInternal(STATE_DRAGGING)
            } else {
                consumed[1] = currentTop - mMaxOffset
                ViewCompat.offsetTopAndBottom(child, -consumed[1])
                setStateInternal(STATE_EXPANDED)
            }
        }
        dispatchOnSlide(child.top)
        mLastNestedScrollDy = dy
        mNestedScrolled = true
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View
    ) {
        if (child!!.top == mMaxOffset) {
            setStateInternal(STATE_EXPANDED)
            return
        }
        if (target !== mNestedScrollingChildRef!!.get() || !mNestedScrolled) {
            return
        }
        val top: Int
        val targetState: Int
        if (mLastNestedScrollDy < 0) {
            top = mMaxOffset
            targetState = STATE_EXPANDED
        } else if (isHideable && shouldHide(child, yVelocity)) {
            top = -child.height
            targetState = STATE_HIDDEN
        } else if (mLastNestedScrollDy == 0) {
            val currentTop = child.top
            if (Math.abs(currentTop - mMinOffset) > Math.abs(currentTop - mMaxOffset)) {
                top = mMaxOffset
                targetState = STATE_EXPANDED
            } else {
                top = mMinOffset
                targetState = STATE_COLLAPSED
            }
        } else {
            top = mMinOffset
            targetState = STATE_COLLAPSED
        }
        if (mViewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
            setStateInternal(STATE_SETTLING)
            ViewCompat.postOnAnimation(
                child,
                SettleRunnable(child, targetState)
            )
        } else {
            setStateInternal(targetState)
        }
        mNestedScrolled = false
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout, child: V, target: View,
        velocityX: Float, velocityY: Float
    ): Boolean {
        return target === mNestedScrollingChildRef!!.get() &&
                (mState != STATE_EXPANDED ||
                        super.onNestedPreFling(
                            coordinatorLayout, child, target,
                            velocityX, velocityY
                        ))
    }

    /**
     * Gets the height of the top sheet when it is collapsed.
     *
     * @return The height of the collapsed top sheet.
     * @attr ref com.google.android.material.R.styleable#BottomSheetBehavior_Layout_behavior_peekHeight
     *///        mMaxOffset = mParentHeight - peekHeight;
    /**
     * Sets the height of the top sheet when it is collapsed.
     *
     * @param peekHeight The height of the collapsed top sheet in pixels.
     * @attr ref com.google.android.material.R.styleable#TopSheetBehavior_Params_behavior_peekHeight
     */
    var peekHeight: Int
        get() = mPeekHeight
        set(peekHeight) {
            mPeekHeight = Math.max(0, peekHeight)
            //        mMaxOffset = mParentHeight - peekHeight;
            if (mViewRef != null && mViewRef!!.get() != null) {
                mMinOffset = Math.max(
                    -mViewRef!!.get()!!.getHeight(),
                    -(mViewRef!!.get()!!.getHeight() - mPeekHeight)
                )
            }
        }

    /**
     * Sets a callback to be notified of top sheet events.
     *
     * @param callback The callback to notify when top sheet events occur.
     */
    fun setTopSheetCallback(callback: TopSheetCallback?) {
        mCallback = callback
    }

    /**
     * Gets the current state of the top sheet.
     *
     * @return One of [.STATE_EXPANDED], [.STATE_COLLAPSED], [.STATE_DRAGGING],
     * and [.STATE_SETTLING].
     */// The view is not laid out yet; modify mState and let onLayoutChild handle it later
    /**
     * Sets the state of the top sheet. The top sheet will transition to that state with
     * animation.
     *
     * @param state One of [.STATE_COLLAPSED], [.STATE_EXPANDED], or
     * [.STATE_HIDDEN].
     */
    @get:State
    var state: Int
        get() = mState
        set(state) {
            if (state == mState) {
                return
            }
            if (mViewRef == null) {
                // The view is not laid out yet; modify mState and let onLayoutChild handle it later
                if (state == STATE_COLLAPSED || state == STATE_EXPANDED ||
                    isHideable && state == STATE_HIDDEN
                ) {
                    mState = state
                }
                return
            }
            val child = mViewRef!!.get() ?: return
            val top: Int
            top = if (state == STATE_COLLAPSED) {
                mMinOffset
            } else if (state == STATE_EXPANDED) {
                mMaxOffset
            } else if (isHideable && state == STATE_HIDDEN) {
                -child.height
            } else if (state == STATE_DRAGGING) {
                mMaxOffset
            } else {
                throw IllegalArgumentException("Illegal state argument: $state")
            }
            setStateInternal(STATE_SETTLING)
            if (mViewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
                ViewCompat.postOnAnimation(
                    child,
                    SettleRunnable(child, state)
                )
            }
        }

    var oldState = mState
    private fun setStateInternal(@State state: Int) {
        if (state == STATE_COLLAPSED || state == STATE_EXPANDED) {
            oldState = state
        }
        if (mState == state) {
            return
        }
        mState = state
        val topSheet: View? = mViewRef!!.get()
        if (topSheet != null && mCallback != null) {
            mCallback!!.onStateChanged(topSheet, state)
        }
    }

    private fun reset() {
        mActivePointerId = ViewDragHelper.INVALID_POINTER
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    private fun shouldHide(child: View, yvel: Float): Boolean {
        if (child.top > mMinOffset) {
            // It should not hide, but collapse.
            return false
        }
        val newTop =
            child.top + yvel * HIDE_FRICTION
        return Math.abs(newTop - mMinOffset) / mPeekHeight.toFloat() > HIDE_THRESHOLD
    }

    private fun findScrollingChild(view: View): View? {
        if (view is NestedScrollingChild) {
            return view
        }
        if (view is ViewGroup) {
            val group = view
            var i = 0
            val count = group.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(group.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i++
            }
        }
        return null
    }

    private val yVelocity: Float
        private get() {
            mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity)
            return VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId)
        }

    private val mDragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(
            child: View,
            pointerId: Int
        ): Boolean {
            if (mState == STATE_DRAGGING) {
                return false
            }
            if (mTouchingScrollingChild) {
                return false
            }
            if (mState == STATE_EXPANDED && mActivePointerId == pointerId) {
                val scroll = mNestedScrollingChildRef!!.get()
                if (scroll != null && ViewCompat.canScrollVertically(scroll, -1)) {
                    // Let the content scroll up
                    return false
                }
            }
            return mViewRef != null && mViewRef!!.get() === child
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            dispatchOnSlide(top)
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == ViewDragHelper.STATE_DRAGGING) {
                setStateInternal(STATE_DRAGGING)
            }
        }

        override fun onViewReleased(
            releasedChild: View,
            xvel: Float,
            yvel: Float
        ) {
            val top: Int
            @State val targetState: Int
            if (yvel > 0) { // Moving up
                top = mMaxOffset
                targetState = STATE_EXPANDED
            } else if (isHideable && shouldHide(releasedChild, yvel)) {
                top = -mViewRef!!.get()!!.height
                targetState = STATE_HIDDEN
            } else if (yvel == 0f) {
                val currentTop = releasedChild.top
                if (Math.abs(currentTop - mMinOffset) > Math.abs(currentTop - mMaxOffset)) {
                    top = mMaxOffset
                    targetState = STATE_EXPANDED
                } else {
                    top = mMinOffset
                    targetState = STATE_COLLAPSED
                }
            } else {
                top = mMinOffset
                targetState = STATE_COLLAPSED
            }
            if (mViewDragHelper!!.settleCapturedViewAt(releasedChild.left, top)) {
                setStateInternal(STATE_SETTLING)
                ViewCompat.postOnAnimation(
                    releasedChild,
                    SettleRunnable(releasedChild, targetState)
                )
            } else {
                setStateInternal(targetState)
            }
        }

        override fun clampViewPositionVertical(
            child: View,
            top: Int,
            dy: Int
        ): Int {
            return constrain(
                top,
                if (isHideable) -child.height else mMinOffset,
                mMaxOffset
            )
        }

        override fun clampViewPositionHorizontal(
            child: View,
            left: Int,
            dx: Int
        ): Int {
            return child.left
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return if (isHideable) {
                child.height
            } else {
                mMaxOffset - mMinOffset
            }
        }
    }

    private fun dispatchOnSlide(top: Int) {
        val topSheet: View? = mViewRef!!.get()
        if (topSheet != null && mCallback != null) {
            val isOpening =
                oldState == STATE_COLLAPSED
            if (top < mMinOffset) {
                mCallback!!.onSlide(
                    topSheet,
                    (top - mMinOffset).toFloat() / mPeekHeight,
                    isOpening
                )
            } else {
                mCallback!!.onSlide(
                    topSheet,
                    (top - mMinOffset).toFloat() / (mMaxOffset - mMinOffset), isOpening
                )
            }
        }
    }

    private inner class SettleRunnable internal constructor(
        private val mView: View,
        @field:State @param:State private val mTargetState: Int
    ) :
        Runnable {

        override fun run() {
            if (mViewDragHelper != null && mViewDragHelper!!.continueSettling(true)) {
                ViewCompat.postOnAnimation(mView, this)
            } else {
                setStateInternal(mTargetState)
            }
        }

    }

    protected class SavedState : AbsSavedState {
        @State
        val state: Int

        @RequiresApi(api = Build.VERSION_CODES.N)
        constructor(source: Parcel) : this(source, null) {
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            state = source.readInt()
        }

        constructor(superState: Parcelable?, @State state: Int) : super(
            superState
        ) {
            this.state = state
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState?>? =
                ParcelableCompat.newCreator(
                    object :
                        ParcelableCompatCreatorCallbacks<SavedState?> {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        override fun createFromParcel(
                            `in`: Parcel,
                            loader: ClassLoader
                        ): SavedState? {
                            return SavedState(`in`, loader)
                        }

                        override fun newArray(size: Int): Array<SavedState?> {
                            return arrayOfNulls(size)
                        }
                    })
        }
    }

    companion object {
        /**
         * The top sheet is dragging.
         */
        const val STATE_DRAGGING = 1

        /**
         * The top sheet is settling.
         */
        const val STATE_SETTLING = 2

        /**
         * The top sheet is expanded.
         */
        const val STATE_EXPANDED = 3

        /**
         * The top sheet is collapsed.
         */
        const val STATE_COLLAPSED = 4

        /**
         * The top sheet is hidden.
         */
        const val STATE_HIDDEN = 5
        private const val HIDE_THRESHOLD = 0.5f
        private const val HIDE_FRICTION = 0.1f

        /**
         * A utility function to get the [JobDetailTopSheetBehavior] associated with the `view`.
         *
         * @param view The [View] with [JobDetailTopSheetBehavior].
         * @return The [JobDetailTopSheetBehavior] associated with the `view`.
         */
        fun <V : View?> from(view: V): TopSheetBehavior<V> {
            val params = view!!.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior =
                params
                    .behavior
            require(behavior is TopSheetBehavior<*>) { "The view is not associated with TopSheetBehavior" }
            return behavior as TopSheetBehavior<V>
        }

        fun constrain(amount: Int, low: Int, high: Int): Int {
            return if (amount < low) low else if (amount > high) high else amount
        }

        fun constrain(amount: Float, low: Float, high: Float): Float {
            return if (amount < low) low else if (amount > high) high else amount
        }
    }
}