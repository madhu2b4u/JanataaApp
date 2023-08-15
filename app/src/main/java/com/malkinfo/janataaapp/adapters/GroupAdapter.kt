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
import com.malkinfo.janataaapp.models.community.GroupModel

class GroupAdapter(var context: Context?, var groupModel: ArrayList<GroupModel>,var totalLimit: Int?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onJoinClicked: ((Int, Boolean, View) -> Unit)? = null
    var onMuteClicked: ((Int, Boolean) -> Unit)? = null
    var onItemClicked: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View
        return when (viewType) {
            ITEM_VIEW -> {
                layoutView = LayoutInflater.from(parent.context).inflate(R.layout.group_item, null)
                ItemViewHolder(layoutView)
            }
            ITEM_FOOTER -> {
                layoutView = LayoutInflater.from(parent.context).inflate(R.layout.view_more_progress_footer_item, null)
                FooterViewHolder(layoutView)
            }
            else -> {
                layoutView = LayoutInflater.from(parent.context).inflate(R.layout.group_item, null)
                ItemViewHolder(layoutView)
            }
        }

    }

    override fun getItemCount(): Int {
        return groupModel.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder) {
            holder.groupNameTV.text = groupModel[position].group
            holder.groupMembersTV.text = groupModel[position].total_members.toString() + "members"
            holder.aboutGroupTV.text = groupModel[position].about
            if (groupModel[position].profile_url.isNullOrEmpty()) {
                holder.groupIV.load(R.drawable.profile_iv)
            } else {
                holder.groupIV.load(groupModel[position].profile_url!!)
            }
            holder.muteCB.isChecked = groupModel[position].isMute!!
            holder.joinCB.isChecked = groupModel[position].is_joined!!



            if (holder.joinCB.isChecked) {
                holder.joinCB.text = context!!.getString(R.string.joined)
                holder.joinCB.setTextColor(context!!.getColor(R.color.white))

            } else {
                holder.joinCB.text = context!!.getString(R.string.join)
                holder.joinCB.setTextColor(context!!.getColor(R.color.lightBlue))
            }
        }
        else if(holder is FooterViewHolder){

            if (position == groupModel.size && totalLimit != position) {
                holder.loadMoreProgressBar.visibility = View.VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = View.GONE
            }
        }
    }

    inner class FooterViewHolder(footerView : View) : RecyclerView.ViewHolder(footerView){
        val loadMoreProgressBar: ProgressBar = footerView.findViewById(R.id.loadMoreProgressBar)
    }


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupMembersTV: TextView = view.findViewById(R.id.groupMembersTV)
        var groupNameTV: TextView = view.findViewById(R.id.groupNameTV)
        var aboutGroupTV: TextView = view.findViewById(R.id.aboutGroupTV)
        var groupIV: ImageView = view.findViewById(R.id.groupIV)
        var joinCB: CheckBox = view.findViewById(R.id.joinCB)
        var muteCB: CheckBox = view.findViewById(R.id.muteCB)

        init {

            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition)
            }

            joinCB.setOnClickListener {
                onJoinClicked?.invoke(adapterPosition, joinCB.isChecked, joinCB)
            }

            muteCB.setOnClickListener {
                onMuteClicked?.invoke(adapterPosition, muteCB.isChecked)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == groupModel.size) {
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