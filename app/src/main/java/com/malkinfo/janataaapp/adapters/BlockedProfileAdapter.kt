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
import com.malkinfo.janataaapp.models.Matrimony.BlockedProfileItem

class BlockedProfileAdapter (var context: Context ,var blockedProfileModel: ArrayList<BlockedProfileItem>):
        RecyclerView.Adapter<BlockedProfileAdapter.ViewHolder>(){

    var onUnBlockClicked: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.blocked_profile_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return blockedProfileModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.partnerIV.load(blockedProfileModel[position].profile_url)
        holder.partnerNameTV.text=blockedProfileModel[position].full_name

        val age = blockedProfileModel[position].matrimony_registeration!!.age.toString()  +"yrs"+","
        val height= blockedProfileModel[position].matrimony_registeration!!.height.toString()+"cm"+","
        val caste = blockedProfileModel[position].caste!!.caste+","+"\n"
        val education =  blockedProfileModel[position].matrimony_registeration!!.education+","+"\n"
        val profession =  blockedProfileModel[position].matrimony_registeration!!.employed_in!!.employed_in+","+"\n"
        val state = blockedProfileModel[position].state!!.state
        holder.partnerDetailsTV.text=age+height+caste+education+profession+state

    }


    inner class ViewHolder (view : View): RecyclerView.ViewHolder(view){
        var partnerIV : ImageView = view.findViewById(R.id.partnerIV)
        var partnerNameTV : TextView =view.findViewById(R.id.partnerNameTV)
        var partnerDetailsTV : TextView =view.findViewById(R.id.partnerDetailsTV)
        var unBlockTV : TextView =view.findViewById(R.id.unBlockTV)


        init {
            unBlockTV.setOnClickListener {
                onUnBlockClicked?.invoke(adapterPosition)
            }


        }
    }
}