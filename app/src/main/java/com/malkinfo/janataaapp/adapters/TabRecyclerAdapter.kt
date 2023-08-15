package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.BottomTabItem

class TabRecyclerAdapter(private val mContext: Context, private val mTabList: List<BottomTabItem>, private val mListener: OnTabButtonSelector, selectedPos: Int) : RecyclerView.Adapter<TabRecyclerAdapter.TabSelectorViewHolder>() {
    private var selectedPos = 0

    init {
        this.selectedPos = selectedPos
    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabSelectorViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_tab, null)
        return TabSelectorViewHolder(layoutView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TabSelectorViewHolder, position: Int) {

        /*holder.mTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        if(mTabList[position].name==""){
            holder.mTabName.visibility = GONE
        }else {
            holder.mTabName.visibility = VISIBLE
        }*/

        if (selectedPos == position) {
            holder.mTabIcon.setImageResource(mTabList[position].activeIcon)
//            holder.mTabName.setTextColor(ContextCompat.getColor(mContext, R.color.secondary))
//            holder.mTabRL.setBackgroundColor(ContextCompat.getColor(mContext, R.color.secondary))
        } else {
            holder.mTabIcon.setImageResource(mTabList[position].inactiveIcon)
//            holder.mTabName.setTextColor(ContextCompat.getColor(mContext, R.color.green))
//            holder.mTabRL.setBackgroundColor(ContextCompat.getColor(mContext, R.color))
        }

//        holder.mTabName.text = mTabList[position].name
    }


    // Return the size of your dataset
    override fun getItemCount(): Int {
        return this.mTabList.size
    }

    fun setSelectedPos(i: Int) {
        notifyItemChanged(selectedPos)
        selectedPos = i
        notifyItemChanged(selectedPos)
        mListener.onTabSelected(i)
    }

    //creating interface for item click
    interface OnTabButtonSelector {
        fun onTabSelected(position: Int)
    }

    inner class TabSelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init the item view's
        internal var mTabIcon: ImageView
        //internal var mTabName: TextView
        internal var mTabRL: ConstraintLayout

        init {

            // get the reference of item view's
            mTabIcon = itemView.findViewById(R.id.tab_icon)
            //mTabName = itemView.findViewById(R.id.tab_text)
            mTabRL = itemView.findViewById(R.id.tab_layout)


            itemView.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = adapterPosition
                notifyItemChanged(selectedPos)
                mListener.onTabSelected(adapterPosition)

            }

            itemView.isClickable = true
        }

    }

}

