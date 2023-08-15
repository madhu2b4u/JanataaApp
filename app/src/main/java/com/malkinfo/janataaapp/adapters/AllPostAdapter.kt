package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.ProfilePostModel
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AllPostAdapter(
    var context: Context,
    var allPostModel: ArrayList<ProfilePostModel>
) :
    RecyclerView.Adapter<AllPostAdapter.ViewHolder>() {
    private lateinit var postImageAdapter: PostImageAdapter
    private var postImageModel: ArrayList<String> = ArrayList()
    private lateinit var strPostTime: String
    var onShareClicked: ((Int) -> Unit)? = null
    var onMoreOptionClicked: ((Int, View) -> Unit)? = null
    var onItemClicked: ((Int) -> Unit)? = null
    var onFavouriteClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllPostAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.view_all_post_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return allPostModel.size
    }

    override fun onBindViewHolder(holder: AllPostAdapter.ViewHolder, position: Int) {
        if(allPostModel[position].user_id!!.profile_url.isNullOrEmpty()){
            holder.postUserIV.load(R.drawable.profile_iv)
        }
        else {
            holder.postUserIV.load(allPostModel[position].user_id!!.profile_url)
        }
        holder.postUserTV.text = allPostModel[position].user_id!!.full_name
        holder.postTV.text = allPostModel[position].description
        holder.likesCountTV.text = allPostModel[position].likes_count.toString() + "\t" + "Likes"
        holder.commentsCountTV.text = allPostModel[position].comments_count.toString() + "\t" + "Comments"
        holder.viewsCountTV.text =
            allPostModel[position].views_count.toString() + "\t" + "Views"
        holder.favouriteCB.isChecked = allPostModel[position].is_liked!!

        val time: Long = allPostModel[position].published_at!!.toLong()


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

        postImageModel = allPostModel[position].image_url!!

        if (postImageModel.isNotEmpty()) {
            if (postImageModel.any())
                holder.postImageRV.visibility = View.VISIBLE
        } else {
            holder.postImageRV.visibility = View.GONE
        }

        postImageAdapter = PostImageAdapter(context, postImageModel)
        holder.postImageRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.postImageRV.adapter = postImageAdapter
        postImageAdapter.notifyDataSetChanged()

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        init {

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
        }
    }
}