package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.bloodgroup.BloodGroupRequestItem

class BloodGroupRequestAdapter(
    var context: Context,
    var bloodGroupRequestModel: ArrayList<BloodGroupRequestItem>,
    var totalLimit: Int?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onCallClicked: ((Int) -> Unit)? = null
    var onViewMoreClicked: ((Int) -> Unit)? = null
    var copyPasteItemClicked: ((View) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val layoutView: View
        return when (viewType) {
            ITEM_VIEW -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.blood_group_request_item, null)
                ItemViewHolder(layoutView)

            }
            ITEM_FOOTER -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.view_more_post_footer_item, null)
                FooterViewHolder(layoutView)

            }
            else -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.blood_group_request_item, null)
                ItemViewHolder(layoutView)

            }
        }
    }

    override fun getItemCount(): Int {
        return bloodGroupRequestModel.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder) {
            if (bloodGroupRequestModel[position].user_id != null){
                holder.usernameTV.text = bloodGroupRequestModel[position].user_id!!.full_name
            }

            holder.mobileNumberTV.text = bloodGroupRequestModel[position].mobile
            val isLocaDesrib = bloodGroupRequestModel[position].location
            holder.locationTV.text =  HtmlCompat.fromHtml(isLocaDesrib!!,HtmlCompat.FROM_HTML_MODE_LEGACY)

            val requestTime =
                DateHelper.getTime(bloodGroupRequestModel[position].requested_at!!.toLong())
            holder.timeTV.text = requestTime
            holder.bloodGroupTypeTV.text = bloodGroupRequestModel[position].blood

            holder.view.setOnClickListener {
                copyPasteItemClicked?.invoke(holder.locationTV)
            }

            when (holder.bloodGroupTypeTV.text) {
                "A+" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.semiDarkGreen))
                }
                "B+" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.midYellow))
                }
                "O+" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.orange))
                }
                "AB+" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.lightGreen))
                }
                "A-" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.pink))
                }
                "AB-" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.darkPurple))
                }
                "B-" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.semiBackgroundBlue))
                }
                "O-" -> {
                    holder.bloodGroupTypeCL.setCardBackgroundColor(context.resources.getColor(R.color.semiDarkBlue))
                }

            }
        }
        else if (holder is FooterViewHolder) {
            if (position == bloodGroupRequestModel.size && totalLimit != position) {
                holder.viewMoreBT.visibility = VISIBLE
            } else {
                holder.viewMoreBT.visibility = GONE
            }
        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        val viewMoreBT: Button = footerView.findViewById(R.id.viewMoreBT)
        init {
            viewMoreBT.setOnClickListener {
                onViewMoreClicked?.invoke(adapterPosition)
            }
        }
    }

    inner class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var bloodGroupTypeCL: CardView = view.findViewById(R.id.bloodGroupTypeCL)
        var bloodGroupTypeTV: TextView = view.findViewById(R.id.bloodGroupTypeTV)
        var usernameTV: TextView = view.findViewById(R.id.usernameTV)
        var mobileNumberTV: TextView = view.findViewById(R.id.mobileNumberTV)
        var locationTV: TextView = view.findViewById(R.id.locationTV)
        var timeTV: TextView = view.findViewById(R.id.timeTV)
        var callIV: ImageView = view.findViewById(R.id.callIV)

        init {
            callIV.setOnClickListener {
                onCallClicked?.invoke(adapterPosition)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == bloodGroupRequestModel.size) {
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