package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.UpcomingMeetingModel
import java.util.*
import kotlin.collections.ArrayList

class UpcomingMeetingAdapter (var context: Context,var upcomingMeetingModel: ArrayList<UpcomingMeetingModel>):
        RecyclerView.Adapter<UpcomingMeetingAdapter.ViewHolder>(){

    var onJoinMeetingClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingMeetingAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.upcoming_meeting_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return upcomingMeetingModel.size
    }

    override fun onBindViewHolder(holder: UpcomingMeetingAdapter.ViewHolder, position: Int) {
        holder.meetingTitleTV.text=upcomingMeetingModel[position].event_title

        var meetingDate = upcomingMeetingModel[position].meeting_on!!.toLong()
        val meetingStartTime = DateHelper.getTime(upcomingMeetingModel[position].meeting_on!!.toLong())
        val meetingEndTime =DateHelper.getTime( upcomingMeetingModel[position].meeting_ends!!.toLong())

        holder.meetingDateTV.text= DateHelper.getOnlyDate(meetingDate)
        holder.meetingMonthTV.text= DateHelper.getOnlyMonth(meetingDate)

        holder.meetingStartTimeTV.text= meetingStartTime
        holder.meetingEndTimeTV.text=meetingEndTime


        if(DateHelper.getDate(DateHelper.getDateFromString(holder.meetingStartTimeTV.text.toString()))!!.before(Date()))
        {

            holder.meetingCL.setBackgroundColor(context.getColor(R.color.textBlue))
            holder.joinedMeetingHintTV.visibility=VISIBLE
            holder.joinMeetingTV.visibility=GONE
            holder.streamIV.visibility=GONE
        }
        else{
            holder.meetingCL.setBackgroundColor(context.getColor(R.color.red))
            holder.joinedMeetingHintTV.visibility= GONE
            holder.joinMeetingTV.visibility= VISIBLE
            holder.streamIV.visibility= VISIBLE
        }

       /* if(upcomingMeetingModel[position].isToday){
            holder.meetingCL.setBackgroundColor(context.getColor(R.color.red))
            holder.joinedMeetingHintTV.visibility= GONE
            holder.joinMeetingTV.visibility= VISIBLE
            holder.streamIV.visibility= VISIBLE
        }else{
            holder.meetingCL.setBackgroundColor(context.getColor(R.color.textBlue))
            holder.joinedMeetingHintTV.visibility=VISIBLE
            holder.joinMeetingTV.visibility=GONE
            holder.streamIV.visibility=GONE
        }*/
    }

    inner class ViewHolder (view: View):RecyclerView.ViewHolder(view){

        var streamIV:ImageView=view.findViewById(R.id.streamIV)
        var meetingDateTV:TextView=view.findViewById(R.id.meetingDateTV)
        var meetingMonthTV:TextView=view.findViewById(R.id.meetingMonthTV)
        var meetingTitleTV:TextView=view.findViewById(R.id.meetingTitleTV)
        var meetingStartTimeTV:TextView=view.findViewById(R.id.meetingStartTimeTV)
        var meetingEndTimeTV:TextView=view.findViewById(R.id.meetingEndTimeTV)
        var joinMeetingTV:TextView=view.findViewById(R.id.joinMeetingTV)
        var joinedMeetingHintTV:TextView=view.findViewById(R.id.joinedMeetingHintTV)
        var meetingCL:ConstraintLayout=view.findViewById(R.id.meetingCL)

        init {
            joinMeetingTV.setOnClickListener {
                onJoinMeetingClicked?.invoke(adapterPosition)
            }
        }
    }

}