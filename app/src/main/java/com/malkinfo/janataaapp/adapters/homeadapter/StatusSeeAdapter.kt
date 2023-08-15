package com.malkinfo.janataaapp.adapters.homeadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.allpost.StatusSeenData
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class StatusSeeAdapter():
RecyclerView.Adapter<StatusSeeAdapter.StatusSeeViewHolder>()
{
    private var seeStatusModel: ArrayList<StatusSeenData> = ArrayList()
    private  var strPostTime: String = ""
    fun addListSeeViewer(listSeeViewer :ArrayList<StatusSeenData>){
        this.seeStatusModel = listSeeViewer
        notifyDataSetChanged()

    }
    inner class StatusSeeViewHolder(var v: View):RecyclerView.ViewHolder(v){
        val userSeeImgCIV = v.findViewById<CircleImageView>(R.id.userSeeImgCIV)
        val userStatNameTV = v.findViewById<TextView>(R.id.userStatNameTV)
        val userseeTimeTV = v.findViewById<TextView>(R.id.userseeTimeTV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusSeeViewHolder {
       val v = LayoutInflater.from(parent.context)
           .inflate(R.layout.status_user_list_see,parent,false)
        return StatusSeeViewHolder(v)
    }

    override fun getItemCount(): Int = seeStatusModel.size

    override fun onBindViewHolder(holder: StatusSeeViewHolder, position: Int) {
        val seeUserItem = seeStatusModel[position]
        holder.userStatNameTV.text = seeUserItem.userName
        if (seeUserItem.userProfileImg == null){
            holder.userSeeImgCIV.load(R.drawable.profile_iv)
        }else{
            holder.userSeeImgCIV.load(seeUserItem.userProfileImg)
        }

        //holder.userseeTimeTV.text = seeUserItem.seentime
        /**set time*/
        val time: Long = seeUserItem.seentime!!
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
                holder.userseeTimeTV.text = strPostTime
            }
            minute < 60 -> {
                strPostTime = if (minute == 1L) {
                    "$minute minute ago"
                } else {
                    "$minute minutes ago"
                }
                holder.userseeTimeTV.text = strPostTime
            }
            hour < 24 -> {
                strPostTime = if (hour == 1L) {
                    "$hour hour ago"
                } else {
                    "$hour hours ago"
                }
                holder.userseeTimeTV.text = strPostTime
            }
            else -> {
                holder.userseeTimeTV.text = DateHelper.getFullFormatDate(time)
            }
        }


    }
}