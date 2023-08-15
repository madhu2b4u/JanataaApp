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
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.community.CommentModel
import com.malkinfo.janataaapp.utitlis.SeeMoreTextView
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CommentAdapter(
    var context: Context,
    var commentModel: ArrayList<CommentModel>,
    var totalLimit: Int?
):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var strPostTime: String
    var onDeleteClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View
        return if (viewType == ITEM_VIEW) {
            layoutView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, null)
            return ItemViewHolder(layoutView)
        } else  if (viewType == ITEM_FOOTER) {
            layoutView = LayoutInflater.from(parent.context).inflate(R.layout.view_more_progress_footer_item, null)
            return FooterViewHolder(layoutView)
        }else{
            layoutView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, null)
            return ItemViewHolder(layoutView)
        }
    }

    override fun getItemCount(): Int {
        return commentModel.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            if(MyCommunityApp.getUser(context)!!._id.equals(commentModel[position].user!!._id)) {
                holder.deleteCommentTV.visibility = View.VISIBLE
            }else{
                holder.deleteCommentTV.visibility = View.GONE
            }

            if(commentModel[position].user!!.profile_url.isNullOrEmpty()){
                holder.commentUserIV.load(R.drawable.profile_iv)
            }
            else {
                holder.commentUserIV.load(commentModel[position].user!!.profile_url!!)
            }

            val commentText=commentModel[position].comment
            holder.commentTV.setContent(commentText)
            holder.commentTV.setTextMaxLength(3)
            holder.commentUserTV.text=commentModel[position].user!!.full_name

            val time: Long = commentModel[position].commented_at!!.toLong()


            val currentDate = Date()

            val dateDiff = currentDate.time - time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)


            when {
                second < 60 -> {
                    strPostTime="$second s"
                    holder.commentTimeTV.text = strPostTime
                }
                minute < 60 -> {
                    strPostTime="$minute m"
                    holder.commentTimeTV.text =  strPostTime
                }
                hour < 24 -> {
                    strPostTime = "$hour h"
                    holder.commentTimeTV.text = strPostTime
                }
                else -> {
                    holder.commentTimeTV.text = DateHelper.getCurrentDate(time)
                }
            }
        }else if( holder is FooterViewHolder){
            if (position == commentModel.size && totalLimit != position) {
                holder.loadMoreProgressBar.visibility = VISIBLE
            } else {
                holder.loadMoreProgressBar.visibility = GONE
            }
        }

    }


    inner class ItemViewHolder (view: View):RecyclerView.ViewHolder(view){

        var commentUserIV:ImageView = view.findViewById(R.id.commentUserIV)
        var commentUserTV:TextView = view.findViewById(R.id.commentUserTV)
        var commentTV: SeeMoreTextView = view.findViewById(R.id.commentTV)
        var deleteCommentTV:TextView = view.findViewById(R.id.deleteCommentTV)
        var commentTimeTV:TextView = view.findViewById(R.id.commentTimeTV)

        init {
            deleteCommentTV.setOnClickListener {
                onDeleteClicked?.invoke(adapterPosition)
            }
        }
    }

    inner class FooterViewHolder (footerView: View) : RecyclerView.ViewHolder(footerView){
        val loadMoreProgressBar: ProgressBar = footerView.findViewById(R.id.loadMoreProgressBar)
    }

    override fun getItemViewType(position: Int): Int {
       return if(position==commentModel.size){
            ITEM_FOOTER
        }else{
            ITEM_VIEW
        }
    }
    companion object {
        private val ITEM_VIEW = 2
        private val ITEM_FOOTER = 3
    }
}