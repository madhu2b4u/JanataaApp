package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.GuideLineModel

class GuideLineAdapter(var context: Context, var guideLineModel: ArrayList<GuideLineModel>) :
    RecyclerView.Adapter<GuideLineAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideLineAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.guideline_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return guideLineModel.size
    }

    override fun onBindViewHolder(holder: GuideLineAdapter.ViewHolder, position: Int) {
        holder.guideLineTV.text=guideLineModel[position].guideline
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var guideLineTV:TextView=view.findViewById(R.id.guideLineTV)
    }
}