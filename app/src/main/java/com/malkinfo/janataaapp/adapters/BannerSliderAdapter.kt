package com.malkinfo.janataaapp.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel
import com.malkinfo.janataaapp.views.main.home.BannerViewDialog

/**
 * ------------------------------------------
 * Created by Farida Shekh on 08-04-2023.
 * ------------------------------------------
 */


class BannerSliderAdapter (val context:Context,
                           val viewPage: ViewPager2
):
    RecyclerView.Adapter<BannerSliderAdapter.SliderViewHolder>()
{
    private var imgList:ArrayList<HomePostModel> = ArrayList()

    inner class SliderViewHolder(var v: View): RecyclerView.ViewHolder(v){
        val imgVews = v.findViewById<AppCompatImageView>(R.id.imageSlider)

    }
    fun addSliderList(bannerImgL:ArrayList<HomePostModel>){
        this.imgList = bannerImgL
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val infalter = LayoutInflater.from(parent.context)
        val v = infalter.inflate(R.layout.bann_slider_item,parent,false)
        return SliderViewHolder(v)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val listImg = imgList[position]
        for (image in listImg.image_url!!){
            holder.imgVews.load(image)
        }


        if (position == imgList.size - 2){
            viewPage.post(run)
        }
        val bannerView = BannerViewDialog(context as Activity)
        holder.v.setOnClickListener {
            bannerView.showBannerAds(imgList,position)
        }
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    val run = object :Runnable{
        override fun run() {
            imgList.addAll(imgList)
            notifyDataSetChanged()
        }

    }

}