package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.community.FollowerModel
import com.malkinfo.janataaapp.models.community.ProfilePostModel
import com.malkinfo.janataaapp.models.launch.User


class ProfileAdapter(
    var context: Context,
    var profileDetails: User,
    var profileFollowerModel: ArrayList<FollowerModel>,
    var profilePostModel: ArrayList<ProfilePostModel>,
    var followerCount: Int,
    var totalLimit: Int,
    var listener: OnUserProfileItemClicked
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var profilePostAdapter: ProfilePostAdapter
    private lateinit var profileFollowerAdapter: ProfileFollowerAdapter
    var onBackClicked: ((Int) -> Unit)? = null
    var onFollowClicked: ((Int) -> Unit)? = null
    var onViewAllFollowerClicked: ((Int) -> Unit)? = null


    interface OnUserProfileItemClicked {
        fun onFavClicked(pos: Int)
        fun onItemClicked(pos: Int)
        fun onMoreOptionClicked(pos: Int, view: View)
        fun onOtherUserMoreOptionClicked(pos: Int, view: View)
        fun onShareClicked(pos: Any?)
        fun onListScroll()
        fun onViewMorePostClicked()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutView: View
        return when (viewType) {
            ITEM_HEADER -> {
                layoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_profile_post_header_layout, null)
                HeaderViewHolder(layoutView)
            }
            ITEM_VIEW -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.user_follower_layout, null)
                ItemViewHolder(layoutView)
            }
            ITEM_FOOTER -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.user_post_layout, null)
                FooterViewHolder(layoutView)
            }
            else -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.user_post_layout, null)
                ItemViewHolder(layoutView)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is HeaderViewHolder) {
            if (profileDetails.profile_url.isNullOrEmpty()) {
                holder.profileIV.load(R.drawable.profile_iv)
            } else {
                holder.profileIV.load(profileDetails.profile_url)
            }
            holder.userNameTV.text = profileDetails.full_name
            holder.casteNameTV.text = profileDetails.caste!!.caste
            val address =
                profileDetails.district!!.district + "," + profileDetails.state!!.state
            holder.locationTV.text = address
            holder.bloodGroupTV.text = profileDetails.blood_group



            if (MyCommunityApp.getUser(context)!!._id.equals(profileDetails._id)) {
                holder.followCB.visibility = GONE
            } else {
                holder.followCB.visibility = VISIBLE

                if (profileDetails.is_following == true) {
                    holder.followCB.text = context.getString(R.string.following)
                    holder.followCB.setTextColor(context.getColor(R.color.textGrey))
                    holder.followCB.setBackgroundResource(R.drawable.grey_stroke_bg)

                } else if (profileDetails.is_following == false) {
                    holder.followCB.text = context.getString(R.string.follow)
                    holder.followCB.setTextColor(context.getColor(R.color.white))
                    holder.followCB.setBackgroundResource(R.drawable.corner_fill_bg)
                }
            }

        } else if (holder is ItemViewHolder) {

            val followerCount = "$followerCount\tFollowers"
            holder.followersCountTV.text = followerCount

            holder.followerRV.layoutManager = GridLayoutManager(context, 5)
            profileFollowerAdapter = ProfileFollowerAdapter(context, profileFollowerModel)
            holder.followerRV.adapter = profileFollowerAdapter
            profileFollowerAdapter.notifyDataSetChanged()

            if (profileFollowerModel.any()) {
                holder.viewAllFollowers.visibility = VISIBLE
            } else {
                holder.viewAllFollowers.visibility = INVISIBLE
            }


        } else if (holder is FooterViewHolder) {

            if(profilePostModel.any()){
                holder.noPostFoundTV.visibility= GONE
            }else{
                holder.noPostFoundTV.visibility= VISIBLE
                holder.postRV.visibility= GONE
            }

            holder.postRV.layoutManager = LinearLayoutManager(context)

            profilePostAdapter = ProfilePostAdapter(context, profilePostModel, totalLimit)
            holder.postRV.adapter = profilePostAdapter
            profilePostAdapter.notifyDataSetChanged()

            profilePostAdapter.onFavouriteClicked = { pos ->
                listener.onFavClicked(pos)
            }
            profilePostAdapter.onItemClicked = { pos ->
                listener.onItemClicked(pos)
            }
            profilePostAdapter.onShareClicked = { pos ->
                listener.onShareClicked(pos)
            }
            profilePostAdapter.onMoreOptionClicked = { pos, view ->
                listener.onMoreOptionClicked(pos, view)
            }

            profilePostAdapter.onOtherUserMoreOptionClicked = { pos, view ->
                listener.onOtherUserMoreOptionClicked(pos, view)
            }

            /*holder.postRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!holder.postRV.canScrollVertically(1)) {
                        listener.onListScroll()
                    }
                }
            })*/

            profilePostAdapter.onViewMorePostClicked={
                listener.onViewMorePostClicked()
            }
        }

    }


    inner class HeaderViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView) {
        var profileIV: ImageView = headerView.findViewById(R.id.profileIV)
        var followCB: CheckBox = headerView.findViewById(R.id.followCB)
        var userNameTV: TextView = headerView.findViewById(R.id.userNameTV)
        var casteNameTV: TextView = headerView.findViewById(R.id.casteNameTV)
        var locationTV: TextView = headerView.findViewById(R.id.locationTV)
        var bloodGroupTV: TextView = headerView.findViewById(R.id.bloodGroupTV)
        var backIV: ImageView = headerView.findViewById(R.id.backIV)

        init {
            followCB.setOnClickListener {
                onFollowClicked?.invoke(adapterPosition)
            }

            backIV.setOnClickListener {
                onBackClicked?.invoke(adapterPosition)
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var followersCountTV: TextView = itemView.findViewById(R.id.followersCountTV)
        var viewAllFollowers: TextView = itemView.findViewById(R.id.viewAllFollowers)
        var followerRV: RecyclerView = itemView.findViewById(R.id.followerRV)

        init {
            viewAllFollowers.setOnClickListener {
                onViewAllFollowerClicked?.invoke(adapterPosition)
            }
        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        var postRV: RecyclerView = footerView.findViewById(R.id.postRV)
        var noPostFoundTV: TextView = footerView.findViewById(R.id.noPostFoundTV)
    }


    override fun getItemCount(): Int {
        return 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                ITEM_HEADER
            }
            1 -> {
                ITEM_VIEW
            }
            else -> {
                ITEM_FOOTER
            }
        }

    }

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_VIEW = 1
        private const val ITEM_FOOTER = 2
    }


}