package com.malkinfo.janataaapp.views.main.community

import android.app.Activity
import android.app.Dialog
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.ViewImageAdapters


class ViewImageDialogs (val mActivity: Activity) {
    private lateinit var isdialog: Dialog
    //var imageUrl : String ? = null
    private lateinit var imgRecV:RecyclerView
    private lateinit var imgViewAdapter:ViewImageAdapters
    private lateinit var mViewBackIV :ImageView
    /**set Id*/


    fun stowImageSide(imageUrlList :ArrayList<String>,position:Int) {
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.activity_view_image, null)
        imgRecV = dialogView.findViewById(R.id.imgRecV)
        mViewBackIV = dialogView.findViewById(R.id.mViewBackIV)

        imgViewAdapter = ViewImageAdapters(mActivity)

        /**set Dialog*/

        isdialog =  Dialog(mActivity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        isdialog.setContentView(dialogView)
        isdialog.create()
        val layoutManger = LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false)
        imgRecV.layoutManager = layoutManger
        imgViewAdapter.addViewImgList(imageUrlList)
        imgRecV.adapter = imgViewAdapter
        imgRecV.getLayoutManager()!!.scrollToPosition(position)
     //   imgRecV.smoothScrollToPosition(position);


        mViewBackIV.setOnClickListener { isDismiss() }
        isdialog.show()
    }


    fun isDismiss() {
        isdialog.dismiss()
    }


}