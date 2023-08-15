package com.malkinfo.janataaapp.views.main.home

import android.app.Activity
import android.app.Dialog
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.homeadapter.BannerAdsAdapter
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class BannerViewDialog(val mActivity: Activity) {
    private lateinit var isdialog: Dialog
    private lateinit var bannerViewBackIV:ImageView
    private lateinit var bannerImgRecV:RecyclerView
    //private lateinit var bannerViewCount:TextView
    /**set adapter*/
    private lateinit var bannerAdAdapter :BannerAdsAdapter

    fun showBannerAds(bannerImgList:ArrayList<HomePostModel>,pos:Int) {
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.banner_view_layout, null)
        bannerImgRecV = dialogView.findViewById(R.id.bannerImgRecV)
        bannerViewBackIV  = dialogView.findViewById(R.id.bannerViewBackIV)
      //  bannerViewCount = dialogView.findViewById(R.id.bannerViewCount)
        bannerAdAdapter = BannerAdsAdapter(mActivity)
        val layoutManger = LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false)
        bannerImgRecV.layoutManager = layoutManger
        bannerImgRecV.adapter = bannerAdAdapter
       // bannerViewCount.text = "$count Viewers..."

        /**set Dialog*/

        isdialog =  Dialog(mActivity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        isdialog.setContentView(dialogView)
        isdialog.create()
        bannerAdAdapter.addViewImgList(bannerImgList)
        bannerImgRecV.getLayoutManager()!!.scrollToPosition(pos)
        bannerViewBackIV.setOnClickListener { isDismiss() }
        showViewerCount()
        isdialog.show()
    }

    private fun showViewerCount() {

    }

    fun isDismiss() {
        isdialog.dismiss()
    }

}