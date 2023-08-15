package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.community.FollowerModel

class FollowerAdapter(
    var context: Context,
    var followerModel: ArrayList<FollowerModel>,
    var totalLimit: Int?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onViewFollowersProfileClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View
        return when (viewType) {
            ITEM_VIEW -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.follower_item, null)
                ItemViewHolder(layoutView)
            }
            ITEM_FOOTER -> {
                layoutView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_more_progress_footer_item, null)
                FooterViewHolder(layoutView)
            }
            else -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.follower_item, null)
                ItemViewHolder(layoutView)
            }
        }
    }

    override fun getItemCount(): Int {
        return followerModel.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (followerModel[position].profile_url.isNullOrEmpty()) {
                holder.followerProfileIV.load(R.drawable.profile_iv)
            } else {
                holder.followerProfileIV.load(followerModel[position].profile_url)
            }
            holder.followerNameTV.text = followerModel[position].full_name
        } else if (holder is FooterViewHolder) {

            if (position == followerModel.size && totalLimit != position) {
                holder.loadMoreProgressBar.visibility = View.VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = View.GONE
            }
        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        val loadMoreProgressBar: ProgressBar = footerView.findViewById(R.id.loadMoreProgressBar)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var followerProfileIV: ImageView = view.findViewById(R.id.followerProfileIV)
        var followerNameTV: TextView = view.findViewById(R.id.followerNameTV)
        var viewFollowerProfile: TextView = view.findViewById(R.id.viewFollowerProfile)

        init {
            viewFollowerProfile.setOnClickListener {
                onViewFollowersProfileClicked?.invoke(adapterPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == followerModel.size) {
            ITEM_FOOTER
        } else {
            ITEM_VIEW
        }
    }

    companion object {
        private const val ITEM_VIEW = 1
        private const val ITEM_FOOTER = 2
    }
}