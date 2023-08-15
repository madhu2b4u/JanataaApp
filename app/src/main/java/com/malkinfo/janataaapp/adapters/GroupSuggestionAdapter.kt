package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.community.GroupModel

class GroupSuggestionAdapter(
    var context: Context,var
    groupSuggestionModel: ArrayList<GroupModel>
) :
    RecyclerView.Adapter<GroupSuggestionAdapter.ViewHolder>() {

    var onGroupSuggestionClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupSuggestionAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.other_group_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return groupSuggestionModel.size
    }

    override fun onBindViewHolder(holder: GroupSuggestionAdapter.ViewHolder, position: Int) {
        if(groupSuggestionModel[position].profile_url.isNullOrEmpty()){
            holder.otherGroupIV.load(R.drawable.profile_iv)
        }
        else {
            holder.otherGroupIV.load(groupSuggestionModel[position].profile_url)
        }
        holder.otherGroupNameTV.text=groupSuggestionModel[position].group
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var otherGroupIV :ImageView =view.findViewById(R.id.otherGroupIV)
        var otherGroupNameTV :TextView =view.findViewById(R.id.otherGroupNameTV)
        var groupSuggestCL :ConstraintLayout =view.findViewById(R.id.groupSuggestCL)

        init {
            groupSuggestCL.setOnClickListener {
                onGroupSuggestionClicked?.invoke(adapterPosition)
            }
        }
    }

}