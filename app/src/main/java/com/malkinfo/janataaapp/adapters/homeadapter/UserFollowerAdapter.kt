package com.malkinfo.janataaapp.adapters.homeadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.ProfileFollowerAdapter
import com.malkinfo.janataaapp.models.community.FollowerModel

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class UserFollowerAdapter(var context: Context,
                          var profileFollowerModel: ArrayList<FollowerModel>,
                          var followerCount: Int
):
RecyclerView.Adapter<UserFollowerAdapter.FollowerViewHolder>()
{

    var onAllFollowerClicked: ((Int) -> Unit)? = null
    private lateinit var profileFollowerAdapter: ProfileFollowerAdapter


    fun addFollowerList(followerModel:ArrayList<FollowerModel>){
        this.profileFollowerModel = followerModel
    }
    inner class FollowerViewHolder(var v:View) :RecyclerView.ViewHolder(v){
        var followersCountTV: TextView = itemView.findViewById(R.id.followersCountTV)
        var viewAllFollowers: TextView = itemView.findViewById(R.id.viewAllFollowers)
        var followerRV: RecyclerView = itemView.findViewById(R.id.followerRV)
        init {
            viewAllFollowers.setOnClickListener {
                onAllFollowerClicked?.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
       val v = LayoutInflater.from(parent.context)
           .inflate(R.layout.user_follower_layout,parent,false)
        return FollowerViewHolder(v)
    }

    override fun getItemCount(): Int = profileFollowerModel.size

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val followerCount = "$followerCount\tFollowers"
        holder.followersCountTV.text = followerCount

        holder.followerRV.layoutManager = GridLayoutManager(context, 5)
        profileFollowerAdapter = ProfileFollowerAdapter(context, profileFollowerModel)
        holder.followerRV.adapter = profileFollowerAdapter
        profileFollowerAdapter.notifyDataSetChanged()

        if (profileFollowerModel.any()) {
            holder.viewAllFollowers.visibility = View.VISIBLE
        } else {
            holder.viewAllFollowers.visibility = View.INVISIBLE
        }
    }
}