package com.malkinfo.janataaapp.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.views.main.community.ViewImageDialogs


class ChatImageItemAdapter (var context: Context, var chatImageItem: ArrayList<String>):
    RecyclerView.Adapter<ChatImageItemAdapter.ViewHolder>(){

    var onPostImageClicked : ((Int)-> Unit)? = null
    private lateinit var photoDialog: Dialog

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatImageItemAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.chat_image_uploaded_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return chatImageItem.size
    }

    override fun onBindViewHolder(holder: ChatImageItemAdapter.ViewHolder, position: Int) {
        holder.chatImageItemIV.load(chatImageItem[position])


    }

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){

        var chatImageItemIV: ImageView =view.findViewById(R.id.chatImageItemIV)


        init {
            chatImageItemIV.setOnClickListener {
                //onPostImageClicked?.invoke(adapterPosition)
                val viewImgDialog = ViewImageDialogs(context as Activity)
                viewImgDialog.stowImageSide(chatImageItem,adapterPosition)

//                val photoSetImgView = LayoutInflater.from(context).inflate(R.layout.post_image_layout,null)
//                val postZoomIV = photoSetImgView.findViewById<PhotoView>(R.id.postZoomIV)
//
//                photoDialog = Dialog(context)
//                photoDialog.setContentView(R.layout.post_image_layout)
//                photoDialog.setCanceledOnTouchOutside(true)
//                photoDialog.show()
//                postZoomIV.load(chatImageItem[adapterPosition])
            }

        }
    }
}