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
import com.malkinfo.janataaapp.models.community.GroupMemberModel

class GroupMembersAdapter(
    var context: Context,
    var groupMembersModel: ArrayList<GroupMemberModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onViewAllClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val layoutView: View

        return if (viewType == ITEM_VIEW) {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.other_members_item, null)
            ItemViewHolder(layoutView)
        } else if (viewType == ITEM_FOOTER) {
            layoutView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_other_members_footer_item, null)
            FooterViewHolder(layoutView)
        } else {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.other_members_item, null)
            ItemViewHolder(layoutView)
        }
    }


    override fun getItemCount(): Int {

        return groupMembersModel.size + 1

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == groupMembersModel.size) ITEM_FOOTER else ITEM_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (groupMembersModel[position].profile_url.isNullOrEmpty()) {
                holder.groupMembersIV.load(R.drawable.profile_iv)
            } else {
                holder.groupMembersIV.load(groupMembersModel[position].profile_url)
            }
        } else if (holder is FooterViewHolder) {

        }

    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var groupMembersIV: ImageView = view.findViewById(R.id.groupMembersIV)
    }


    inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var viewAllGroupMembers: TextView = view.findViewById(R.id.viewAllGroupMembers)

        init {
            viewAllGroupMembers.setOnClickListener {
                onViewAllClicked?.invoke(adapterPosition)
            }
        }
    }

    companion object {
        private val ITEM_VIEW = 1
        private val ITEM_FOOTER = 2

    }
}
