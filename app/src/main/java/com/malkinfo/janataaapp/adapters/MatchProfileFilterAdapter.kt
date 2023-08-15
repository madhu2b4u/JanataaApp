package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.Matrimony.MatchProfileItem

class MatchProfileFilterAdapter (var context: Context, var matchProfileFilterModel : ArrayList<MatchProfileItem>, var totalLimit: Int? ) :
RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var onItemClicked: ((Int) -> Unit)? = null
    var onChatNowClicked: ((Int) -> Unit)? = null
    var onDontShowClicked: ((Int) -> Unit)? = null
    var onShortlistClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View
        return when (viewType) {
            ITEM_VIEW -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.matrimony_item, null)
                ItemViewHolder(layoutView)
            }
            ITEM_FOOTER -> {
                layoutView = LayoutInflater.from(parent.context).inflate(R.layout.view_more_progress_footer_item, null)
                FooterViewHolder(layoutView)
            }
            else -> {
                layoutView = LayoutInflater.from(parent.context).inflate(R.layout.matrimony_item, null)
                ItemViewHolder(layoutView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            holder.partnerIV.load(matchProfileFilterModel[position].profile_url)
            holder.partnerNameTV.text = matchProfileFilterModel[position].full_name
            val age =
                matchProfileFilterModel[position].matrimony_registeration!!.age.toString() + "yrs" + ","
            val height =
                matchProfileFilterModel[position].matrimony_registeration!!.height.toString() + "cm" + ","
            val caste = matchProfileFilterModel[position].caste!!.caste + "," + "\n"
            val education =
                matchProfileFilterModel[position].matrimony_registeration!!.education + "," + "\n"
            val profession =
                matchProfileFilterModel[position].matrimony_registeration!!.employed_in!!.employed_in + "," + "\n"
            val state = matchProfileFilterModel[position].state!!.state
            holder.partnerDetailsTV.text = age + height + caste + education + profession + state

            holder.shortListCB.isChecked = matchProfileFilterModel[position].is_shortlisted!!
        }else if(holder is FooterViewHolder){
            if (position == matchProfileFilterModel.size && totalLimit != position) {
                holder.loadMoreProgressBar.visibility = View.VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = View.GONE
            }
        }
    }

   inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
       val loadMoreProgressBar: ProgressBar = footerView.findViewById(R.id.loadMoreProgressBar)
    }

   inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var partnerIV: ImageView = itemView.findViewById(R.id.partnerIV)
        var partnerNameTV: TextView = itemView.findViewById(R.id.partnerNameTV)
        var partnerDetailsTV: TextView = itemView.findViewById(R.id.partnerDetailsTV)
        var chatNowTV: TextView = itemView.findViewById(R.id.chatNowTV)
        var dontShowTV: TextView = itemView.findViewById(R.id.dontShowTV)
        var shortListCB: CheckBox = itemView.findViewById(R.id.shortListCB)

        init {
            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition)
            }

            chatNowTV.setOnClickListener {
                onChatNowClicked?.invoke(adapterPosition)
            }

            dontShowTV.setOnClickListener {
                onDontShowClicked?.invoke(adapterPosition)
            }
            shortListCB.setOnClickListener {
                onShortlistClicked?.invoke(adapterPosition)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == matchProfileFilterModel.size) {
            ITEM_FOOTER
        } else {
            ITEM_VIEW
        }
    }

    override fun getItemCount(): Int {
        return matchProfileFilterModel.size+1
    }


    companion object {
        private const val ITEM_VIEW = 0
        private const val ITEM_FOOTER = 1
    }
}