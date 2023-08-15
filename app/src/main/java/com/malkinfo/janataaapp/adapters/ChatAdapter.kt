package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.Matrimony.ChatListItem

class ChatAdapter(var context: Context, var chatListItem: ArrayList<ChatListItem>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var onItemClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return chatListItem.size
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {

        val regularttf = Typeface.createFromAsset(context.assets, "font/lexend_regular.ttf")
        val semiboldttf = Typeface.createFromAsset(context.assets, "font/lexend_semibold.ttf")
        if (chatListItem[position].profile_url == null){
            holder.chatProfileIV.load(R.drawable.profile_iv)
        }else{
            holder.chatProfileIV.load(chatListItem[position].profile_url)
        }


        holder.chatUserNameTV.text = chatListItem[position].full_name
        holder.messageCountTV.text = chatListItem[position].new_message.toString()
        val messageTime =
            DateHelper.getTime(chatListItem[position].latest_message!!.sent_on!!.toLong())
        holder.chatTimeTV.text = messageTime


        when {
            chatListItem[position].latest_message!!.content_type.equals("message") -> {
                holder.messageTV.text = chatListItem[position].latest_message!!.message
            }
            chatListItem[position].latest_message!!.content_type.equals("message with media") -> {
                holder.messageTV.text = chatListItem[position].latest_message!!.message
            }
            chatListItem[position].latest_message!!.content_type.equals("message with audio") -> {
                holder.messageTV.text = chatListItem[position].latest_message!!.message
            }
            chatListItem[position].latest_message!!.content_type.equals("media") -> {
                holder.messageTV.text = context.getString(R.string.photo)
            }
            chatListItem[position].latest_message!!.content_type.equals("audio") -> {
                holder.messageTV.text = context.getString(R.string.audio)
            }
        }


        if (chatListItem[position].latest_message!!.my_message!! && chatListItem[position].latest_message!!.seen!!) {
            holder.doubleTickIV.visibility = VISIBLE
            holder.messageCountTV.visibility = GONE
            holder.messageTV.typeface = regularttf
        } else if (chatListItem[position].latest_message!!.my_message!! && !chatListItem[position].latest_message!!.seen!!) {
            holder.doubleTickIV.visibility = GONE
            holder.messageCountTV.visibility = GONE
            holder.messageTV.typeface = regularttf
        } else if (!chatListItem[position].latest_message!!.my_message!! && !chatListItem[position].latest_message!!.seen!!) {
            holder.doubleTickIV.visibility = GONE
            holder.messageCountTV.visibility = VISIBLE
            holder.messageTV.typeface = semiboldttf
        } else if (!chatListItem[position].latest_message!!.my_message!! && chatListItem[position].latest_message!!.seen!!) {
            holder.doubleTickIV.visibility = GONE
            holder.messageCountTV.visibility = GONE
            holder.messageTV.typeface = regularttf
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var chatUserNameTV: TextView = view.findViewById(R.id.chatUserNameTV)
        var messageTV: TextView = view.findViewById(R.id.messageTV)
        var chatTimeTV: TextView = view.findViewById(R.id.chatTimeTV)
        var messageCountTV: TextView = view.findViewById(R.id.messageCountTV)
        var doubleTickIV: ImageView = view.findViewById(R.id.doubleTickIV)
        var chatProfileIV: ImageView = view.findViewById(R.id.chatProfileIV)

        init {
            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition)
            }
        }
    }
}