package com.malkinfo.janataaapp.utitlis

import android.content.Context
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class ProgressBarAnimation(
    var context: Context,
    var progressBar: ProgressBar,
    var storyStore: StoryStore,
    var from :Float,
    var to :Float
) : Animation()

{
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
        //textView.text = "Loading ${value.toInt()} %"
        if (value == to){
           storyStore.getNewStory()
        }
    }
}