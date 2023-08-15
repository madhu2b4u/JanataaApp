package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.RecentMeetingModel

class RecentMeetingAdapter (var context: Context,var recentMeetingModel: ArrayList<RecentMeetingModel>):
        RecyclerView.Adapter<RecentMeetingAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentMeetingAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.recent_meeting_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return recentMeetingModel.size
    }

    override fun onBindViewHolder(holder: RecentMeetingAdapter.ViewHolder, position: Int) {
        holder.meetingAboutTV.text=recentMeetingModel[position].event_title

        val meetingDate = DateHelper.getMonthAndDate(recentMeetingModel[position].meeting_on!!.toLong())
        val meetingStartTime = DateHelper.getTime(recentMeetingModel[position].meeting_on!!.toLong())
        val meetingEndTime =DateHelper.getTime( recentMeetingModel[position].meeting_ends!!.toLong())
        holder.meetingDateTV.text=meetingDate
        holder.meetingStartTimeTV.text=meetingStartTime
        holder.meetingEndTimeTV.text=meetingEndTime

    }

    inner class ViewHolder (view: View):RecyclerView.ViewHolder(view){
        var meetingAboutTV:TextView =view.findViewById(R.id.eventTV)
        var meetingDateTV:TextView =view.findViewById(R.id.meetingDateTV)
        var meetingStartTimeTV:TextView =view.findViewById(R.id.meetingStartTimeTV)
        var meetingEndTimeTV:TextView =view.findViewById(R.id.meetingEndTimeTV)
    }
}