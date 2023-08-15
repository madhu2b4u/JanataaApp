package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.statusmodel.UserStatusData
import de.hdodenhof.circleimageview.CircleImageView
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ------------------------------------------
 * Created by Farida Shekh on 08-04-2023.
 * ------------------------------------------
 */

class UserStatusAdapter(
    val c: Context,
    val statusRV:RecyclerView):
RecyclerView.Adapter<UserStatusAdapter.UserStatusViewHolder>()
{
    var statusList:ArrayList<UserStatusData> = ArrayList()
    private lateinit var strPostTime: String
    inner class UserStatusViewHolder(val v:View) :RecyclerView.ViewHolder(v){
        val pubNameTV = v.findViewById<TextView>(R.id.pulishNameTV)
        val pulished_atTV = v.findViewById<TextView>(R.id.pulished_atTV)
        val pubImgCV = v.findViewById<CircleImageView>(R.id.pulisherImgCIV)
        val discpTV = v.findViewById<TextView>(R.id.discpTV)
        val statusImgIV = v.findViewById<ImageView>(R.id.statusImgIV)
        val storiesSV = v.findViewById<StoriesProgressView>(R.id.storiesSV)

    }

    fun addUserStatus(statusLists:ArrayList<UserStatusData>){
        for (statusp_post in statusLists){
            if (!postContains(statusList,statusp_post._id)){
                statusList.add(statusp_post)
            }
        }
        notifyDataSetChanged()
    }
    fun postContains(list: ArrayList<UserStatusData>, name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStatusViewHolder {
      val v= LayoutInflater.from(parent.context).inflate(R.layout.status_item_view,parent,false)
        return UserStatusViewHolder(v)
    }

    override fun getItemCount(): Int  = statusList.size

    override fun onBindViewHolder(holder: UserStatusViewHolder,position: Int) {
        val statusPoL = statusList[position]

        holder.pubNameTV.text = statusPoL.pubProfileName
        if (statusPoL.pubProfileImg != null){
            holder.pubImgCV.load(statusPoL.pubProfileImg)
        }else{
            holder.pubImgCV.load(R.drawable.profile_iv)
        }
        holder.discpTV.text = statusPoL.statusDesp
        holder.storiesSV.setStoryDuration(3000L);
        holder.statusImgIV.load(statusPoL.statusImg)
        holder.storiesSV.setStoriesCount(1);
        holder.storiesSV.setStoryDuration(3000L)
        holder.storiesSV.startStories();

        val time: Long = statusPoL.publish_at!!.toLong()
        val currentDate = Date()
        val dateDiff = currentDate.time - time
        val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)

        when {
            second < 60 -> {
                strPostTime = if (second == 1L) {
                    "$second second ago"
                } else {
                    "$second seconds ago"
                }
                holder.pulished_atTV.text = strPostTime
            }
            minute < 60 -> {
                strPostTime = if (minute == 1L) {
                    "$minute minute ago"
                } else {
                    "$minute minutes ago"
                }
                holder.pulished_atTV.text = strPostTime
            }
            hour < 24 -> {
                strPostTime = if (hour == 1L) {
                    "$hour hour ago"
                } else {
                    "$hour hours ago"
                }
                holder.pulished_atTV.text = strPostTime
            }
            else -> {
                holder.pulished_atTV.text = DateHelper.getFullFormatDate(time)
            }
        }

        holder.storiesSV.setStoriesListener(object : StoriesProgressView.StoriesListener {
            override fun onNext() {statusRV.post(Runnable { // Call smooth scroll
                if (position <= statusList.size){

                    statusRV.smoothScrollToPosition(position +1)
                }

            })}

            override fun onPrev() {}

            override fun onComplete() {
                statusRV.post(Runnable { // Call smooth scroll
                    if (position <= statusList.size){

                        statusRV.smoothScrollToPosition(position +1)
                    }
                })
            }
        })

    }

}