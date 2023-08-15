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
import com.malkinfo.janataaapp.models.ProfileRecommendationItem

class ProfileRecommendationAdapter (var context: Context , var profileRecommendationItem: ArrayList<ProfileRecommendationItem>):
        RecyclerView.Adapter<ProfileRecommendationAdapter.ViewHolder>(){

    var onItemClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileRecommendationAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.profile_recommendation_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
    return profileRecommendationItem.size
    }

    override fun onBindViewHolder(holder: ProfileRecommendationAdapter.ViewHolder, position: Int) {
        holder.partnerNameTV.text=profileRecommendationItem[position].full_name
        holder.partnerAgeTV.text= profileRecommendationItem[position].matrimony_registeration!!.age.toString()
        if(profileRecommendationItem[position].profile_url.isNullOrEmpty()){
            holder.profileIV.load(R.drawable.profile_iv)
        }
        holder.profileIV.load(profileRecommendationItem[position].profile_url)
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        var profileIV: ImageView =itemView.findViewById(R.id.profileIV)
        var partnerNameTV: TextView =itemView.findViewById(R.id.partnerNameTV)
        var partnerAgeTV: TextView =itemView.findViewById(R.id.partnerAgeTV)
        init {
            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition)
            }

        }
    }
}