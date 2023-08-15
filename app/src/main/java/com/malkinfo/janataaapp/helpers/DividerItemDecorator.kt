package com.malkinfo.janataaapp.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator(context: Context, val showFirstDivider: Boolean, val showLastDivider: Boolean) : RecyclerView.ItemDecoration() {

    private val divider: Drawable

    var mOrientation = -1


    init {
        val a = context.obtainStyledAttributes(ATTRS)
//        mShowFirstDivider = showFirstDivider
//        mShowLastDivider = showLastDivider
        divider = a.getDrawable(0)!!
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
    }

    fun drawVertical(c: Canvas, parent: RecyclerView) {
        var left = parent.paddingLeft
        var right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }

//        for (i in (if (mShowFirstDivider) 0 else 1) until childCount) {
//            val child = parent.getChildAt(i)
//            val params = child.layoutParams as RecyclerView.LayoutParams
//
//            val size = divider.getIntrinsicHeight()
//
//            val top = child.top - params.topMargin - size
//            val bottom = top + size
//            left = child.left - params.leftMargin
//            right = left + size
//
//            divider.setBounds(left, top, right, bottom)
//            divider.draw(c)
//        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION || position == 0 && !showFirstDivider) {
            return
        }

        if (mOrientation == -1) getOrientation(parent)

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = divider.getIntrinsicHeight()
            if (showLastDivider && position == state.itemCount - 1) {
                outRect.bottom = outRect.top
            }
        } else {
            outRect.left = divider.getIntrinsicWidth()
            if (showLastDivider && position == state.itemCount - 1) {
                outRect.right = outRect.left
            }
        }
//        outRect.set(0, 0, 0, divider.intrinsicHeight)
    }

    companion object {

        private val ATTRS = intArrayOf(android.R.attr.listDivider)
//        private var showFirstDivider : Boolean = false
//        private var showLastDivider : Boolean = false


    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (mOrientation == -1) {
            mOrientation = if (parent.layoutManager is LinearLayoutManager) {
                val layoutManager =
                    parent.layoutManager as LinearLayoutManager?
                layoutManager!!.orientation
            } else {
                throw IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager."
                )
            }
        }
        return mOrientation
    }
}