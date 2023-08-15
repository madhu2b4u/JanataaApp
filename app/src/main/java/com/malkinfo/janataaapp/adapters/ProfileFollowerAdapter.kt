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
import com.malkinfo.janataaapp.models.community.FollowerModel

class ProfileFollowerAdapter (var context: Context,var profileFollowerModel:ArrayList<FollowerModel>):
        RecyclerView.Adapter<ProfileFollowerAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileFollowerAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.profile_follower_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return when {
            profileFollowerModel.size<5 -> {
                profileFollowerModel.size
            }
            else -> {
                5
            }
        }
    }

    override fun onBindViewHolder(holder: ProfileFollowerAdapter.ViewHolder, position: Int) {
        if(profileFollowerModel[position].profile_url.isNullOrEmpty()){
            holder.followerIV.load(R.drawable.profile_iv)
        }else {
            holder.followerIV.load(profileFollowerModel[position].profile_url)
        }
        holder.followerTV.text=profileFollowerModel[position].full_name
    }

    inner class ViewHolder (view: View):RecyclerView.ViewHolder(view){

        var followerIV:ImageView=view.findViewById(R.id.followerIV)
        var followerTV:TextView=view.findViewById(R.id.followerTV)

    }
}