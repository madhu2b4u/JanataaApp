package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.base.FileUploadItem

class MediaAdapter(var context: Context, var profileImageModel: ArrayList<FileUploadItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onAddMoreMediaClicked: ((Int) -> Unit)? = null
    var onDeleteMediaClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View

        return if (viewType == ITEM_VIEW) {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.media_item, null)
            ItemViewHolder(layoutView)
        } else if (viewType == ITEM_FOOTER) {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.media_footer_item, null)
            FooterViewHolder(layoutView)
        } else {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.media_item, null)
            ItemViewHolder(layoutView)
        }
    }

    override fun getItemCount(): Int {
        return profileImageModel.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == profileImageModel.size) ITEM_FOOTER else ITEM_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder) {
            holder.mediaIV.load(profileImageModel[position].fileurl)
        }

        else if(holder is FooterViewHolder){
            if(position<=4){
                holder.addMoreMediaCV.visibility= VISIBLE
            }else{
                holder.addMoreMediaCV.visibility= GONE
            }
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mediaIV :ImageView=view.findViewById(R.id.mediaIV)
        var deleteProfileImageCV :CardView=view.findViewById(R.id.deleteProfileImageCV)

        init {
            deleteProfileImageCV.setOnClickListener {
                onDeleteMediaClicked?.invoke(adapterPosition)
            }
        }
    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        var addMoreMediaCV: CardView =footerView.findViewById(R.id.addMoreMediaCV)

        init {
            addMoreMediaCV.setOnClickListener {
                onAddMoreMediaClicked?.invoke(adapterPosition)
            }
        }
    }

    companion object {
        private val ITEM_VIEW = 1
        private val ITEM_FOOTER = 2

    }
}