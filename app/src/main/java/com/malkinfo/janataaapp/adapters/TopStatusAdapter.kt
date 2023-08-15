package com.malkinfo.janataaapp.adapters

import MyCommunityApp
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devlomi.circularstatusview.CircularStatusView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * ------------------------------------------
 * Created by Farida Shekh on 08-04-2023.
 * ------------------------------------------
 */

class TopStatusAdapter(
    var context: Context,
    var totalLimit: Int?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onWriteStatusClicked: ((Int, View) -> Unit)? = null

    var onItemClicked: ((String,String,Int) -> Unit)? = null

    var storyDelete: ((Int)-> Unit)? = null
    var onProfileClicked: ((Int) -> Unit)? = null
    var onOtherProfileClicked: ((Int) -> Unit)? = null
    var groupPostModel: ArrayList<HomePostModel> = ArrayList()
    var userStatusPostModel: ArrayList<HomePostModel> = ArrayList()
    var otherStatusPostModel: ArrayList<HomePostModel> = ArrayList()
    fun addStatus(statusList :ArrayList<HomePostModel>){
        for (status in statusList){
            if (postContains(groupPostModel,status._id)){
              Log.d("mTag","already add")
            }else{
                if (MyCommunityApp.getUser(context)!!._id == status.user_id!!._id){
                    if (postContains(userStatusPostModel,status._id)){
                        Log.d("mTag","userStatusPostModel already add")

                    }else{
                        userStatusPostModel.add(status)
                    }

                }else{
                    if (postContains(otherStatusPostModel,status._id)){
                        Log.d("mTag","otherStatusPostModel already add")

                    }else{
                        otherStatusPostModel.add(status)
                    }

                }
                Log.d("mTag","I am addStatus")
            }
        }
        groupPostModel.addAll(userStatusPostModel)
        groupPostModel.addAll(otherStatusPostModel)
        notifyDataSetChanged()
        Log.d("mTag","I am notifyDataSetChanged blow")
        Log.d("mTag","status list =${groupPostModel.size}")
    }
    fun postContains(list: ArrayList<HomePostModel>,name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == ITEM_HEADER) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.write_status_story, parent, false)
            HeaderViewHolder(view)
        } else if (viewType == ITEM_VIEW) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_status_view, parent, false)
            ItemViewHolder(view)
        } else if (viewType == ITEM_FOOTER) {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_more_progress_footer_item, parent, false)
            FooterViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_status_view, parent, false)
            ItemViewHolder(view)
        }
    }


    override fun getItemCount(): Int {
        return groupPostModel.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            Log.d("mTag","I am ItemViewHolder")
            Log.d("mTag","image_url =${groupPostModel[position-1].image_url!!.size} ")
               for (status in (groupPostModel[position-1].image_url!!)){
                   if (status.endsWith(".mp4")){
                       val requestOptions = RequestOptions()
                       Glide.with(context)
                           .load(status)
                           .apply(requestOptions)
                           .thumbnail(Glide.with(context)
                               .load(status))
                           .into(holder.image)

                   }else{
                       holder.image.load(status)
                   }
               }


            val time: Long = groupPostModel[position - 1].published_at!!.toLong()


            val currentDate = Date()

            val dateDiff = currentDate.time - time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            if (hour >= 24){
                storyDelete!!.invoke(position -1)
            }

        } else if (holder is HeaderViewHolder) {
            Log.d("mTag","HeaderViewHolder")

        } else if (holder is FooterViewHolder) {

            if (position == groupPostModel.size+1 && totalLimit != position-1) {
                holder.loadMoreProgressBar.visibility = View.VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = View.GONE
            }
        }

    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<CircleImageView>(R.id.image)
        val circularStatusView = itemView.findViewById<CircularStatusView>(R.id.circular_status_view)

        init {
            itemView.setOnClickListener {
                onItemClicked?.invoke(groupPostModel[adapterPosition -1]._id!!,
                    groupPostModel[adapterPosition-1].user_id!!._id!!,
                    adapterPosition - 1)
            }

        }
    }

    inner class HeaderViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView) {
        val writeStoryPostCV: CircleImageView = headerView.findViewById(R.id.writeStoryPostCV)

        init {
            writeStoryPostCV.setOnClickListener {
                onWriteStatusClicked?.invoke(adapterPosition,it)
            }
        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        val loadMoreProgressBar: ProgressBar = footerView.findViewById(R.id.loadMoreProgressBar)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                ITEM_HEADER
            }
            groupPostModel.size+1 -> {
                ITEM_FOOTER
            }
            else -> {
                ITEM_VIEW
            }
        }
    }

    companion object {
        private val ITEM_HEADER = 0
        private val ITEM_VIEW = 1
        private val ITEM_FOOTER = 2
    }
}