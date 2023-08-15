package com.malkinfo.janataaapp.utitlis


import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.malkinfo.janataaapp.R

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Read More Text view.
 * ------------------------------------------
 */

class SeeMoreTextView : AppCompatTextView {
    private var textMaxLength = 500
    private var seeMoreTextColor: Int = R.color.primaryVariant
    private var collapsedTextWithSeeMoreButton: String? = null
    private var expandedTextWithSeeMoreButton: String? = null
    private var orignalContent: String? = null
    private var collapsedTextSpannable: SpannableString? = null
    private var expandedTextSpannable: SpannableString? = null
    var isExpanded = false
        private set
    private var seeMore = "SeeMore"
    private var seeLess = "SeeLess"
    private var onTextClicked: onTextClicked? = null
    fun setOnTextClicked(onTextClicked: onTextClicked?) {
        this.onTextClicked = onTextClicked
    }

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    //set max length of the string text
    fun setTextMaxLength(maxLength: Int) {
        textMaxLength = maxLength
    }

    fun setSeeMoreTextColor(color: Int) {
        seeMoreTextColor = color
    }

    fun expandText(expand: Boolean) {
        if (expand) {
            isExpanded = true
            text = expandedTextSpannable
        } else {
            isExpanded = false
            text = collapsedTextSpannable
        }
    }

    fun setSeeMoreText(seeMoreText: String, seeLessText: String) {
        seeMore = seeMoreText
        seeLess = seeLessText
    }

    //toggle the state
    fun toggle() {
        if (isExpanded) {
            isExpanded = false
            text = collapsedTextSpannable
        } else {
            isExpanded = true
            text = expandedTextSpannable
        }
    }

    fun setContent(text: String?) {
        orignalContent = text
        this.movementMethod = LinkMovementMethod.getInstance()
        //show see more
        if (orignalContent!!.length >= textMaxLength) {
            collapsedTextWithSeeMoreButton =
                orignalContent!!.substring(0, textMaxLength) + "... " + seeMore
            expandedTextWithSeeMoreButton = "$orignalContent $seeLess"

            //creating spannable strings
            collapsedTextSpannable = SpannableString(collapsedTextWithSeeMoreButton)
            expandedTextSpannable = SpannableString(expandedTextWithSeeMoreButton)
            collapsedTextSpannable!!.setSpan(
                clickableSpan,
                textMaxLength + 4,
                collapsedTextWithSeeMoreButton!!.length,
                0
            )
            collapsedTextSpannable!!.setSpan(
                StyleSpan(Typeface.NORMAL),
                textMaxLength + 4,
                collapsedTextWithSeeMoreButton!!.length,
                0
            )
            collapsedTextSpannable!!.setSpan(
                RelativeSizeSpan(.9f),
                textMaxLength + 4,
                collapsedTextWithSeeMoreButton!!.length,
                0
            )
            expandedTextSpannable!!.setSpan(
                clickableSpan,
                orignalContent!!.length + 1,
                expandedTextWithSeeMoreButton!!.length,
                0
            )
            expandedTextSpannable!!.setSpan(
                StyleSpan(Typeface.NORMAL),
                orignalContent!!.length + 1,
                expandedTextWithSeeMoreButton!!.length,
                0
            )
            expandedTextSpannable!!.setSpan(
                RelativeSizeSpan(.9f),
                orignalContent!!.length + 1,
                expandedTextWithSeeMoreButton!!.length,
                0
            )
            if (isExpanded) setText(expandedTextSpannable) else setText(collapsedTextSpannable)
        } else {
            //to do: don't show see more
            setText(orignalContent)
        }
        setOnClickListener {
            if (onTextClicked != null) {
                if (tag == null || tag != "spanClicked") {
                    onTextClicked!!.onTextClicked()
                }
            }
            tag = "textClicked"
        }
        setOnLongClickListener {
            if (onTextClicked != null) {
                onTextClicked!!.onTextLongClicked()
            }
            tag = "textLongClicked"
            false
        }
    }

    var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            // to prevent toggle when long click on "show more/less"
            tag = if (tag == null || tag != "textLongClicked") {
                toggle()
                "spanClicked"
            } else {
                ""
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = resources.getColor(seeMoreTextColor)
        }
    }
}
