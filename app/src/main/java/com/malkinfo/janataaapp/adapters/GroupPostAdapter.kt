package com.malkinfo.janataaapp.adapters

import MyCommunityApp
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.GroupPostModel
import com.malkinfo.janataaapp.utitlis.SeeMoreTextView
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class GroupPostAdapter(
    var context: Context,
    var groupPostModel: ArrayList<GroupPostModel>,
    var totalLimit: Int?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var strPostTime: String
    private lateinit var postImageAdapter: PostImageAdapter
    private var postImageModel: ArrayList<String> = ArrayList()
    var onWritePostClicked: ((Int) -> Unit)? = null
    var onShareClicked: ((Int) -> Unit)? = null
    var onMoreOptionClicked: ((Int, View) -> Unit)? = null
    var onMoreOptionUserClicked: ((Int, View) -> Unit)? = null
    var onItemClicked: ((Int) -> Unit)? = null
    var onProfileClicked: ((Int) -> Unit)? = null
    var onOtherProfileClicked: ((Int) -> Unit)? = null
    var onFavouriteClicked: ((Int) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == ITEM_HEADER) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.write_post_item, parent, false)
            HeaderViewHolder(view)
        } else if (viewType == ITEM_VIEW) {
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


    override fun getItemCount(): Int {
        return groupPostModel.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (groupPostModel[position - 1].user_id != null){
                if (groupPostModel[position - 1].user_id!!.profile_url.isNullOrEmpty()) {
                    holder.postUserIV.load(R.drawable.profile_iv)
                } else {
                    holder.postUserIV.load(groupPostModel[position - 1].user_id!!.profile_url)
                }
                holder.postUserTV.text = groupPostModel[position - 1].user_id!!.full_name
            }

//            if (MyCommunityApp.getUser(context)!!._id.equals(groupPostModel[position - 1].user_id!!._id)) {
//                holder.moreOptionsIV.visibility = GONE
//            } else {
//                holder.moreOptionsIV.visibility = VISIBLE
//            }


            val desp = groupPostModel[position-1].description
            holder.postTV.setContent(desp)
            holder.postTV.setTextMaxLength(3)



            holder.likesCountTV.text =
                groupPostModel[position - 1].likes_count.toString() + "\t" + "Likes"
            holder.commentsCountTV.text =
                groupPostModel[position - 1].comments_count.toString() + "\t" + "Comments"
            holder.viewsCountTV.text =
                groupPostModel[position - 1].views_count.toString() + "\t" + "Views"
            holder.favouriteCB.isChecked = groupPostModel[position - 1].is_liked!!

            val time: Long = groupPostModel[position - 1].published_at!!.toLong()


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

            postImageModel = groupPostModel[position - 1].image_url!!

            if (postImageModel.isNotEmpty()) {
                if (postImageModel.any())
                    holder.postImageRV.visibility = VISIBLE
            } else {
                holder.postImageRV.visibility = GONE
            }

            postImageAdapter = PostImageAdapter(context, postImageModel)
            holder.postImageRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
            holder.postImageRV.adapter = postImageAdapter
            postImageAdapter.notifyDataSetChanged()

        } else if (holder is HeaderViewHolder) {
            if (MyCommunityApp.getUser(context)!!.profile_url.isNullOrEmpty()) {

                holder.profileIV.load(R.drawable.profile_iv)
            } else {
                holder.profileIV.load(MyCommunityApp.getUser(context)!!.profile_url)
            }
        } else if (holder is FooterViewHolder) {

            if (position == groupPostModel.size+1 && totalLimit != position-1) {
                holder.loadMoreProgressBar.visibility = VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = GONE
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
        val ivSharePostItemIV: ImageView = itemView.findViewById(R.id.ivSharePostItemIV)
        val favouriteCB: CheckBox = itemView.findViewById(R.id.favouriteCB)
        val postImageRV: RecyclerView = itemView.findViewById(R.id.myPostImageRV)

        init {

            postUserIV.setOnClickListener {
                onOtherProfileClicked?.invoke(adapterPosition - 1)
            }

            moreOptionsIV.setOnClickListener {
                if (groupPostModel[adapterPosition -1].user_id != null){
                    if (MyCommunityApp.getUser(context)!!._id == groupPostModel[adapterPosition -1].user_id!!._id){
                        onMoreOptionUserClicked!!.invoke(adapterPosition-1,moreOptionsIV)
                    }else{
                        onMoreOptionClicked?.invoke(adapterPosition - 1, moreOptionsIV)
                    }
                }


            }

            ivSharePostItemIV.setOnClickListener {

                onShareClicked?.invoke(adapterPosition - 1)
            }

            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition - 1)
            }
            favouriteCB.setOnClickListener {
                onFavouriteClicked?.invoke(adapterPosition - 1)
            }

        }
    }

    inner class HeaderViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView) {
        val profileIV: ImageView = headerView.findViewById(R.id.profileIV)
        val writePostED: TextView = headerView.findViewById(R.id.writePostED)

        init {
            profileIV.setOnClickListener {
                onProfileClicked?.invoke(adapterPosition)
            }
            writePostED.setOnClickListener {
                onWritePostClicked?.invoke(adapterPosition)
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