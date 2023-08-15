package com.malkinfo.janataaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.CommunityData

/**
 * ------------------------------------------
 * Created by Farida Shekh on 08-04-2023.
 * ------------------------------------------
 */

class CommunityAdapter():
RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>()
{
    var communityList:ArrayList<CommunityData> = ArrayList()
    var onItemClicked: ((Int) -> Unit)? = null
    inner class CommunityViewHolder(val v:View):RecyclerView.ViewHolder(v){
        //val comTitleTV = v.findViewById<TextView>(R.id.commTitleTV)
        val commIconCV = v.findViewById<ImageView>(R.id.comIconCV)


    }
    fun addCommunityData(isCommunityList:ArrayList<CommunityData>){
        this.communityList = isCommunityList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.community_item,parent,false)
        return CommunityViewHolder(v)
    }

    override fun getItemCount(): Int = communityList.size

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val communityDatas = communityList[position]
      //  holder.comTitleTV.text = communityDatas.comTitles
        holder.commIconCV.setImageResource(communityDatas.comIcon)
        holder.v.setOnClickListener {
            onItemClicked!!.invoke(position)
        }

    }


}