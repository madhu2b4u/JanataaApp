package com.malkinfo.janataaapp.adapters

import MyCommunityApp
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.ProfilePostModel
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ProfilePostAdapter(
    var context: Context,
    var userProfilePostModel: ArrayList<ProfilePostModel>,
    var totalLimit: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var strPostTime: String
    private lateinit var postImageAdapter: PostImageAdapter
    private var postImageModel: ArrayList<String> = ArrayList()
    var onViewMorePostClicked: ((Int) -> Unit)? = null
    var onShareClicked: ((Int) -> Unit)? = null
    var onMoreOptionClicked: ((Int, View) -> Unit)? = null
    var onItemClicked: ((Int) -> Unit)? = null
    var onOtherProfileClicked: ((Int) -> Unit)? = null
    var onFavouriteClicked: ((Int) -> Unit)? = null
    var onOtherUserMoreOptionClicked: ((Int, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == ITEM_VIEW) {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.profile_post_item, parent, false)
            ItemViewHolder(view)
        } else if (viewType == ITEM_FOOTER) {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_more_post_footer_item, null)
            FooterViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.profile_post_item, parent, false)
            ItemViewHolder(view)
        }
    }


    override fun getItemCount(): Int {
        return userProfilePostModel.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (userProfilePostModel[position].user_id!!.profile_url.isNullOrEmpty()) {
                holder.postUserIV.load(R.drawable.profile_iv)
            } else {
                holder.postUserIV.load(userProfilePostModel[position].user_id!!.profile_url)
            }
            holder.postUserTV.text = userProfilePostModel[position].user_id!!.full_name
            holder.postTV.text = userProfilePostModel[position].description
            val likecount = userProfilePostModel[position].likes_count.toString() + "\t" + "Likes"
            holder.likesCountTV.text = likecount
            val commentsCount =
                userProfilePostModel[position].comments_count.toString() + "\t" + "Comments"
            holder.commentsCountTV.text = commentsCount
            val viewsCount = userProfilePostModel[position].views_count.toString() + "\t" + "Views"
            holder.viewsCountTV.text = viewsCount

            holder.favouriteCB.isChecked = userProfilePostModel[position].is_liked!!

            val time: Long = userProfilePostModel[position].published_at!!.toLong()


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

            postImageModel = userProfilePostModel[position].image_url!!

            if (postImageModel.isNotEmpty()) {
                if (postImageModel.any())
                    holder.postImageRV.visibility = VISIBLE
            } else {
                holder.postImageRV.visibility = GONE
            }

            postImageAdapter = PostImageAdapter(context, postImageModel)
            holder.postImageRV.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            holder.postImageRV.adapter = postImageAdapter
            postImageAdapter.notifyDataSetChanged()


            if (MyCommunityApp.getUser(context)!!._id.equals(userProfilePostModel[position].user_id!!._id)) {
                holder.moreOptionsIV.visibility = VISIBLE
                holder.otherUserMoreOptionsIV.visibility = GONE
            } else {
                holder.moreOptionsIV.visibility = GONE
                holder.otherUserMoreOptionsIV.visibility = VISIBLE

            }

        } else if (holder is FooterViewHolder) {

            if (position == userProfilePostModel.size && totalLimit != position) {
                holder.viewMoreBT.visibility = VISIBLE
            } else {
                holder.viewMoreBT.visibility = GONE
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postUserIV: ImageView = itemView.findViewById(R.id.postUserIV)
        val postUserTV: TextView = itemView.findViewById(R.id.postUserTV)
        val postTimeTV: TextView = itemView.findViewById(R.id.postTimeTV)
        val postTV: TextView = itemView.findViewById(R.id.postTV)
        val likesCountTV: TextView = itemView.findViewById(R.id.likesCountTV)
        val commentsCountTV: TextView = itemView.findViewById(R.id.commentsCountTV)
        val viewsCountTV: TextView = itemView.findViewById(R.id.viewsCountTV)
        val moreOptionsIV: ImageView = itemView.findViewById(R.id.moreOptionsIV)
        val ivShare: ImageView = itemView.findViewById(R.id.ivShare)
        val favouriteCB: CheckBox = itemView.findViewById(R.id.favouriteCB)
        val postImageRV: RecyclerView = itemView.findViewById(R.id.postImageRV)
        val otherUserMoreOptionsIV: ImageView = itemView.findViewById(R.id.otherUserMoreOptionsIV)

        init {

            postUserIV.setOnClickListener {
                onOtherProfileClicked?.invoke(adapterPosition)
            }

            moreOptionsIV.setOnClickListener {
                onMoreOptionClicked?.invoke(adapterPosition, moreOptionsIV)
            }

            ivShare.setOnClickListener {
                onShareClicked?.invoke(adapterPosition)
            }

            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition)
            }
            favouriteCB.setOnClickListener {
                onFavouriteClicked?.invoke(adapterPosition)
            }
            otherUserMoreOptionsIV.setOnClickListener {
                onOtherUserMoreOptionClicked?.invoke(adapterPosition, otherUserMoreOptionsIV)
            }
        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        val viewMoreBT: Button = footerView.findViewById(R.id.viewMoreBT)

        init {
            viewMoreBT.setOnClickListener {
                onViewMorePostClicked?.invoke(adapterPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == userProfilePostModel.size) {
            ITEM_FOOTER
        } else {
            ITEM_VIEW
        }
    }

    companion object {
        private val ITEM_VIEW = 2
        private val ITEM_FOOTER = 3
    }
}