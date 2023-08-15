package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.base.FileUploadItem

class ChatImageAdapter(var context: Context, var chatImageList: ArrayList<FileUploadItem>) :
    RecyclerView.Adapter<ChatImageAdapter.ViewHolder>() {

    var onDeleteMediaClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatImageAdapter.ViewHolder {
        val layoutView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_image_item, null)
        return ViewHolder(layoutView)

    }

    override fun getItemCount(): Int {
        return chatImageList.size
    }


    override fun onBindViewHolder(holder: ChatImageAdapter.ViewHolder, position: Int) {
        holder.mediaIV.load(chatImageList[position].fileurl)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mediaIV: ImageView = view.findViewById(R.id.mediaIV)
        var deleteProfileImageCV: CardView = view.findViewById(R.id.deleteProfileImageCV)

        init {
            deleteProfileImageCV.setOnClickListener {
                onDeleteMediaClicked?.invoke(adapterPosition)
            }
        }
    }


}