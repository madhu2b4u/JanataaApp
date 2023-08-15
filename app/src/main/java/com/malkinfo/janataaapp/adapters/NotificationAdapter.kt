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
import com.malkinfo.janataaapp.models.community.NotificationModel

class NotificationAdapter (var context: Context,var notificationModel: ArrayList<NotificationModel>):
        RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return notificationModel.size
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.userProfileIV.load(notificationModel[position].image)
        holder.userNameTV.text=notificationModel[position].name
        holder.notificationContentTV.text=notificationModel[position].notificationType
        holder.notificationTimeTV.text=notificationModel[position].time
    }

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){

        var userProfileIV:ImageView=view.findViewById(R.id.userProfileIV)
        var userNameTV:TextView=view.findViewById(R.id.userNameTV)
        var notificationContentTV:TextView=view.findViewById(R.id.notificationContentTV)
        var notificationTimeTV:TextView=view.findViewById(R.id.notificationTimeTV)
    }

}