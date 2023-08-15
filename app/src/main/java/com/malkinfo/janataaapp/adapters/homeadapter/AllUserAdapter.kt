package com.malkinfo.janataaapp.adapters.homeadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.firebaseuser.FireUser

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class AllUserAdapter(
    var context:Context,
    var userList:ArrayList<FireUser>
):
RecyclerView.Adapter<AllUserAdapter.AllUserViewHolder>()
{
    inner class AllUserViewHolder(v:View):RecyclerView.ViewHolder(v){
        var profileIV: ImageView = v.findViewById(R.id.userProfileIVs)
        var userNameTV: TextView = v.findViewById(R.id.userNameTVs)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUserViewHolder {
       val v = LayoutInflater.from(parent.context)
           .inflate(R.layout.user_item,parent,false)
        return AllUserViewHolder(v)
    }

    override fun getItemCount(): Int = userList.size
    override fun onBindViewHolder(holder: AllUserViewHolder, position: Int) {
        val users = userList[position]
        if (users.user_photo_url != null){
            holder.profileIV.load(users.user_photo_url)
        }else{
            holder.profileIV.load(R.drawable.profile_iv)
        }
        holder.userNameTV.text = users.name
    }
}