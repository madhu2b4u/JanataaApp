package com.malkinfo.janataaapp.adapters.homeadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.Matrimony.ShortlistProfileItem


/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class ShortListProfileAdapter (var context: Context, var shortListProfileModel: ArrayList<ShortlistProfileItem>):
    RecyclerView.Adapter<ShortListProfileAdapter.ViewHolder>() {

    var onRemoveShortlistClicked: ((Int) -> Unit)? = null
    var onChatNowClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.shortlist_profile_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return shortListProfileModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.partnerIV.load(shortListProfileModel[position].profile_url)
        holder.partnerNameTV.text = shortListProfileModel[position].full_name
        val age = shortListProfileModel[position].matrimony_registeration!!.age.toString()  +"yrs"+","
        val height= shortListProfileModel[position].matrimony_registeration!!.height.toString()+"cm"+","
        val caste = shortListProfileModel[position].caste!!.caste+","+"\n"
        val education =  shortListProfileModel[position].matrimony_registeration!!.education+","+"\n"
        val profession =  shortListProfileModel[position].matrimony_registeration!!.employed_in!!.employed_in+","+"\n"
        val state = shortListProfileModel[position].state!!.state
        holder.partnerDetailsTV.text=age+height+caste+education+profession+state

        holder.shortListCB.isChecked= shortListProfileModel[position].is_shortlisted!!
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var partnerIV: ImageView = view.findViewById(R.id.partnerIV)
        var partnerNameTV: TextView = view.findViewById(R.id.partnerNameTV)
        var partnerDetailsTV: TextView = view.findViewById(R.id.partnerDetailsTV)
        var chatNowTV :TextView =view.findViewById(R.id.chatNowTV)
        var shortListCB : CheckBox =view.findViewById(R.id.shortListCB)


        init {
            shortListCB.setOnClickListener {
                onRemoveShortlistClicked?.invoke(adapterPosition)
            }

            chatNowTV.setOnClickListener {
                onChatNowClicked?.invoke(adapterPosition)
            }

        }
    }
}