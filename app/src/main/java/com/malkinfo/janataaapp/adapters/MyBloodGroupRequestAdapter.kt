package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.models.bloodgroup.BloodGroupRequestItem

class MyBloodGroupRequestAdapter(
    var context: Context,
    var myBloodGroupRequestModel: ArrayList<BloodGroupRequestItem>
) :
    RecyclerView.Adapter<MyBloodGroupRequestAdapter.ViewHolder>() {

    var onremoveRequestClicked: ((Int) -> Unit)? = null
    var copyPasteItemClicked: ((View) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBloodGroupRequestAdapter.ViewHolder {
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.my_blood_group_request_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return myBloodGroupRequestModel.size
    }

    override fun onBindViewHolder(holder: MyBloodGroupRequestAdapter.ViewHolder, position: Int) {
        holder.usernameTV.text = myBloodGroupRequestModel[position].user_id!!.full_name
        holder.mobileNumberTV.text = myBloodGroupRequestModel[position].mobile.toString()
        val isLocaDesrib = myBloodGroupRequestModel[position].location
        holder.locationTV.text= HtmlCompat.fromHtml(isLocaDesrib!!,HtmlCompat.FROM_HTML_MODE_LEGACY)



        val requestTime = DateHelper.getTime( myBloodGroupRequestModel[position].requested_at!!.toLong())
        holder.timeTV.text = requestTime

        holder.bloodGroupTypeTV.text = myBloodGroupRequestModel[position].blood

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

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var bloodGroupTypeCL: CardView = view.findViewById(R.id.bloodGroupTypeCL)
        var bloodGroupTypeTV: TextView = view.findViewById(R.id.bloodGroupTypeTV)
        var usernameTV: TextView = view.findViewById(R.id.usernameTV)
        var mobileNumberTV: TextView = view.findViewById(R.id.mobileNumberTV)
        var locationTV: TextView = view.findViewById(R.id.locationTV)
        var timeTV: TextView = view.findViewById(R.id.timeTV)
        var removeRequestIV: ImageView = view.findViewById(R.id.removeRequestIV)

        init {
            removeRequestIV.setOnClickListener {
                onremoveRequestClicked?.invoke(adapterPosition)
            }
        }
    }

}