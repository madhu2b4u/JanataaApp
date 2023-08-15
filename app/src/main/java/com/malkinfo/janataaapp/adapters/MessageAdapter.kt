package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.Matrimony.MessageListItem

class MessageAdapter(
    var context: Context,
    var messageListItem: ArrayList<MessageListItem>,
   var totalLimit: Int?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var chatImageItemAdapter: ChatImageItemAdapter
    private var chatImageList : ArrayList< String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View
        return when (viewType) {
            ITEM_HEADER -> {
                layoutView =
                    LayoutInflater.from(parent.context).inflate(R.layout.view_more_progress_footer_item, parent, false)
                HeaderViewHolder(layoutView)
            }
            ITEM_VIEW -> {
                 layoutView = LayoutInflater.from(parent.context).inflate(R.layout.message_item, null)
                ItemViewHolder(layoutView)
            }
            else -> {
                 layoutView = LayoutInflater.from(parent.context).inflate(R.layout.message_item, null)
                ItemViewHolder(layoutView)
            }
        }

    }

    override fun getItemCount(): Int {
         return messageListItem.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            if (position == chatImageList.size&& totalLimit != position) {
                holder.loadMoreProgressBar.visibility = VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = GONE
            }
        }
        else if(holder is ItemViewHolder) {
            if (messageListItem[position].my_message!!) {
                holder.userIV.load(messageListItem[position].user_id!!.profile_url)
                val messageTime =
                    DateHelper.getDateAndTime(messageListItem[position].sent_on!!.toLong())
                holder.sendTimeTV.text = messageTime
                holder.receiveMessageCL.visibility = GONE
                holder.sendMessageCL.visibility = VISIBLE

                if (messageListItem[position].content_type.equals("message")) {
                    holder.sendMsgTV.visibility = VISIBLE
                    holder.sendImageCV.visibility = GONE
                    holder.sendMsgTV.text = messageListItem[position].message
                } else if (messageListItem[position].content_type.equals("message with media")) {
                    holder.sendMsgTV.visibility = VISIBLE
                    holder.sendImageCV.visibility = VISIBLE
                    holder.sendMsgTV.text = messageListItem[position].message
                } else if (messageListItem[position].content_type.equals("message with audio")) {
                    holder.sendMsgTV.visibility = VISIBLE
                    holder.sendImageCV.visibility = GONE
                    holder.sendMsgTV.text = messageListItem[position].message
                } else if (messageListItem[position].content_type.equals("media")) {
                    holder.sendMsgTV.visibility = GONE
                    holder.sendImageCV.visibility = VISIBLE
                } else if (messageListItem[position].content_type.equals("audio")) {
                    holder.sendMsgTV.visibility = GONE
                    holder.sendImageCV.visibility = GONE

                }

                chatImageList = messageListItem[position].media!!
                chatImageItemAdapter = ChatImageItemAdapter(context, chatImageList)
                holder.sendImageRV.layoutManager = GridLayoutManager(context, 2)
                holder.sendImageRV.adapter = chatImageItemAdapter
                chatImageItemAdapter.notifyDataSetChanged()
            } else {
                val messageTime =
                    DateHelper.getDateAndTime(messageListItem[position].sent_on!!.toLong())
                holder.receivedTimeTV.text = messageTime
                holder.receivedMsgTV.text = messageListItem[position].message
                holder.partnerIV.load(messageListItem[position].message_to!!.profile_url)
                holder.receiveMessageCL.visibility = VISIBLE
                holder.sendMessageCL.visibility = GONE
                if (messageListItem[position].content_type.equals("message")) {
                    holder.receivedMsgTV.visibility = VISIBLE
                    holder.receivedImageCV.visibility = GONE
                    holder.receivedMsgTV.text = messageListItem[position].message
                } else if (messageListItem[position].content_type.equals("message with media")) {
                    holder.receivedMsgTV.visibility = VISIBLE
                    holder.receivedImageCV.visibility = VISIBLE
                    holder.receivedMsgTV.text = messageListItem[position].message
                } else if (messageListItem[position].content_type.equals("message with audio")) {
                    holder.receivedMsgTV.visibility = VISIBLE
                    holder.receivedImageCV.visibility = GONE
                    holder.receivedMsgTV.text = messageListItem[position].message
                } else if (messageListItem[position].content_type.equals("media")) {
                    holder.receivedMsgTV.visibility = GONE
                    holder.receivedImageCV.visibility = VISIBLE
                } else if (messageListItem[position].content_type.equals("audio")) {
                    holder.receivedMsgTV.visibility = GONE
                    holder.receivedImageCV.visibility = GONE
                }

                chatImageList = messageListItem[position].media!!
                chatImageItemAdapter = ChatImageItemAdapter(context, chatImageList)
                holder.receivedImageRV.layoutManager = GridLayoutManager(context, 2)
                holder.receivedImageRV.adapter = chatImageItemAdapter
                chatImageItemAdapter.notifyDataSetChanged()
            }
        }
    }

    inner class HeaderViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView) {
        val loadMoreProgressBar: ProgressBar = headerView.findViewById(R.id.loadMoreProgressBar)
    }


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var partnerIV: ImageView = view.findViewById(R.id.partnerIV)
        var userIV: ImageView = view.findViewById(R.id.userIV)
        var receivedTimeTV: TextView = view.findViewById(R.id.receivedTimeTV)
        var receivedMsgTV: TextView = view.findViewById(R.id.receivedMsgTV)
        var sendTimeTV: TextView = view.findViewById(R.id.sendTimeTV)
        var sendMsgTV: TextView = view.findViewById(R.id.sendMsgTV)
        var receiveMessageCL: ConstraintLayout = view.findViewById(R.id.receiveMessageCL)
        var sendMessageCL: ConstraintLayout = view.findViewById(R.id.sendMessageCL)
        var receivedImageCV: CardView = view.findViewById(R.id.receivedImageCV)
        var sendImageCV: CardView = view.findViewById(R.id.sendImageCV)
        var receivedImageRV: RecyclerView = view.findViewById(R.id.receivedImageRV)
        var sendImageRV: RecyclerView = view.findViewById(R.id.sendImageRV)
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == messageListItem.size){
            ITEM_HEADER
        }else{
            ITEM_VIEW
        }
    }

    companion object {
        private val ITEM_HEADER = 0
        private val ITEM_VIEW = 1
    }
}