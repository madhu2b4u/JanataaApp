package com.malkinfo.janataaapp.adapters

import MyCommunityApp
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel
import com.malkinfo.janataaapp.utitlis.SeeMoreTextView
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ------------------------------------------
 * Created by Farida Shekh on 08-04-2023.
 * ------------------------------------------
 */

class UserPostAdapter(
    var context: Context,
    var totalLimit: Int?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var strPostTime: String
    private lateinit var postImageAdapter: PostImageAdapter
    private var postImageModel: ArrayList<String> = ArrayList()
    var groupPostModel: ArrayList<HomePostModel> = ArrayList()
    var onShareClicked: ((HomePostModel) -> Unit)? = null
    var onMoreOptionClicked: ((String?, View) -> Unit)? = null
    var onItemClicked: ((String?) -> Unit)? = null
    var onProfileClicked: ((Int) -> Unit)? = null
    var onOtherProfileClicked: ((String?) -> Unit)? = null
    var onFavouriteClicked: ((HomePostModel) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == ITEM_VIEW) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.group_post_item, parent, false)
            ItemViewHolder(view)
        } else if (viewType == ITEM_FOOTER) {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_more_progress_footer_item, parent, false)
            FooterViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.group_post_item, parent, false)
            ItemViewHolder(view)
        }
    }
    fun addUserModelData(userPostModel:ArrayList<HomePostModel>){
        for (post_items in userPostModel){
            for (groupId in post_items.group_id!!){
                if (groupId == MyCommunityAppConstants.STATUS_STORY_GROUP_ID){
                   // Log.d("mTag","this igonner Id")
                }else{
                    if (!postContains(groupPostModel,post_items._id)){
                        this.groupPostModel.add(post_items)
                    }
                }
            }
        }
        notifyDataSetChanged()

    }

    fun postContains(list: ArrayList<HomePostModel>, name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }


    override fun getItemCount(): Int {
        return groupPostModel.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (groupPostModel[position].user_id!!.profile_url.isNullOrEmpty()) {
                holder.postUserIV.load(R.drawable.profile_iv)
            } else {
                holder.postUserIV.load(groupPostModel[position].user_id!!.profile_url)
            }
            if (MyCommunityApp.getUser(context)!!._id.equals(groupPostModel[position].user_id!!._id)) {
                holder.moreOptionsIV.visibility = View.GONE
            } else {
                holder.moreOptionsIV.visibility = View.VISIBLE
            }
            holder.postUserTV.text = groupPostModel[position].user_id!!.full_name
            val desp = groupPostModel[position].description
            holder.postTV.setContent(desp)
            holder.postTV.setTextMaxLength(3)


            holder.likesCountTV.text =
                groupPostModel[position].likes_count.toString() + "\t" + "Likes"
            holder.commentsCountTV.text =
                groupPostModel[position].comments_count.toString() + "\t" + "Comments"
            holder.viewsCountTV.text =
                groupPostModel[position].views_count.toString() + "\t" + "Views"
            holder.favouriteCB.isChecked = groupPostModel[position].is_liked!!

            val time: Long = groupPostModel[position].published_at!!.toLong()


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
                    holder.postTimeTV.text = strPostTime
                }
                minute < 60 -> {
                    strPostTime = if (minute == 1L) {
                        "$minute minute ago"
                    } else {
                        "$minute minutes ago"
                    }
                    holder.postTimeTV.text = strPostTime
                }
                hour < 24 -> {
                    strPostTime = if (hour == 1L) {
                        "$hour hour ago"
                    } else {
                        "$hour hours ago"
                    }
                    holder.postTimeTV.text = strPostTime
                }
                else -> {
                    holder.postTimeTV.text = DateHelper.getFullFormatDate(time)
                }
            }

            postImageModel = groupPostModel[position].image_url!!

            if (postImageModel.isNotEmpty()) {
                if (postImageModel.any())
                    holder.postImageRV.visibility = View.VISIBLE
            } else {
                holder.postImageRV.visibility = View.GONE
            }
            postImageAdapter = PostImageAdapter(context, postImageModel)
            holder.postImageRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
            holder.postImageRV.adapter = postImageAdapter
            postImageAdapter.notifyDataSetChanged()

        }else if (holder is FooterViewHolder) {

            if (position == groupPostModel.size && totalLimit != position) {
                holder.loadMoreProgressBar.visibility = View.VISIBLE
                Log.d("mTag"," holder totalLimit = $totalLimit")
            } else {
                holder.loadMoreProgressBar.visibility = View.GONE
            }
        }

    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postUserIV: ImageView = itemView.findViewById(R.id.postUserIV)
        val postUserTV: TextView = itemView.findViewById(R.id.postUserTV)
        val postTimeTV: TextView = itemView.findViewById(R.id.postTimeTV)
        val postTV: SeeMoreTextView = itemView.findViewById(R.id.postTV)
        val likesCountTV: TextView = itemView.findViewById(R.id.likesCountTV)
        val commentsCountTV: TextView = itemView.findViewById(R.id.commentsCountTV)
        val viewsCountTV: TextView = itemView.findViewById(R.id.viewsCountTV)
        val moreOptionsIV: ImageView = itemView.findViewById(R.id.moreOptionsIV)
        val ivShare: ImageView = itemView.findViewById(R.id.ivShare)
        val favouriteCB: CheckBox = itemView.findViewById(R.id.favouriteCB)
        val postImageRV: RecyclerView = itemView.findViewById(R.id.postImageRV)

        init {

            postUserIV.setOnClickListener {
                val _id = groupPostModel[adapterPosition]._id
                onOtherProfileClicked?.invoke(_id)
            }

            moreOptionsIV.setOnClickListener {
                val _id = groupPostModel[adapterPosition]._id
                onMoreOptionClicked?.invoke(_id, moreOptionsIV)
            }

            ivShare.setOnClickListener {
                onShareClicked?.invoke(groupPostModel[adapterPosition])
            }

            itemView.setOnClickListener {
                val id = groupPostModel[adapterPosition]._id
                onItemClicked?.invoke(id)
            }
            favouriteCB.setOnClickListener {
                onFavouriteClicked?.invoke(groupPostModel[adapterPosition])
            }

        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        val loadMoreProgressBar: ProgressBar = footerView.findViewById(R.id.loadMoreProgressBar)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            groupPostModel.size -> {
                ITEM_FOOTER
            }
            else -> {
                ITEM_VIEW
            }
        }
    }

    companion object {
        private val ITEM_VIEW = 1
        private val ITEM_FOOTER = 2
    }
}