package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.base.CasteItem

class CasteAdapter(val context: Context?, var casteModel: ArrayList<CasteItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onCasteRadioButtonClicked: ((Int) -> Unit)? = null
    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View
        return if (viewType == ITEM_HEADER) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.caste_header_item, parent, false)
            HeaderViewHolder(view)
        } else if (viewType == ITEM_VIEW) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.caste_list_item, parent, false)
            ItemViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.caste_list_item, parent, false)
            ItemViewHolder(view)
        }
    }


    override fun getItemCount(): Int {
        return casteModel.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.casteIV.load(casteModel[position - 1].image_url!!)
            holder.casteTV.text = casteModel[position - 1].caste
            holder.casteRB.isChecked = casteModel[position - 1].isChecked!!
        } else if (holder is HeaderViewHolder) {
            holder.casteHeaderTV.text = context!!.getString(R.string.select_caste)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_HEADER else ITEM_VIEW
    }


    inner class ItemViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val casteIV: ImageView = itemview.findViewById(R.id.casteIV)
        val casteTV: TextView = itemview.findViewById(R.id.casteTV)
        val casteRB: RadioButton = itemview.findViewById(R.id.casteRB)

        init {

            casteRB.setOnClickListener {
                onCasteRadioButtonClicked?.invoke(
                    adapterPosition - 1
                )
                selectedPosition = adapterPosition - 1
                notifyDataSetChanged()
            }

        }
    }

    inner class HeaderViewHolder(headerview: View) : RecyclerView.ViewHolder(headerview) {
        val casteHeaderTV: TextView = headerview.findViewById(R.id.casteHeaderTV)
    }


    companion object {
        private val ITEM_HEADER = 1
        private val ITEM_VIEW = 2

    }
}