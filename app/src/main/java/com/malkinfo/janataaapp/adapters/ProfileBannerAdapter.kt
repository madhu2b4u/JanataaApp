package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import coil.load

import com.malkinfo.janataaapp.R

class ProfileBannerAdapter(
    context: Context?,
    private var profileBannerModel: ArrayList<String>
) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context).inflate(R.layout.pager_list, container, false)
        val slideIV: ImageView = view.findViewById(R.id.large_image)
        slideIV.load(profileBannerModel[position])
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return profileBannerModel.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}