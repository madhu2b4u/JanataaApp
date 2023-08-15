package com.malkinfo.janataaapp.views.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.homeadapter.StatusSeeAdapter
import com.malkinfo.janataaapp.models.allpost.StatusSeenData
import jp.shts.android.storiesprogressview.StoriesProgressView

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */

class ActionBottomDialogFragment(var userSeeCount:String,
                                 var seeStatusModel: ArrayList<StatusSeenData>,
                                 var storyBar: StoriesProgressView,
                                 var userVideoStoryVV :VideoView,
                                 var current:Int
                                 )
    : BottomSheetDialogFragment(){



    private lateinit var viewerCount :TextView
    private lateinit var statusUserSeeRec:RecyclerView
    private lateinit var statusAdapter:StatusSeeAdapter
    private lateinit var cancelStViewIV:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stauts_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**now call all textView*/
        isCancelable = false
        invit(view)

    }

    private fun invit(v: View) {
        statusUserSeeRec = v.findViewById(R.id.statusUserSeeRec)
        viewerCount = v.findViewById(R.id.viewerCount)
        cancelStViewIV = v.findViewById(R.id.cancelStViewIV)
        setData()
    }

    private fun setData() {
        viewerCount.text = userSeeCount
        statusAdapter = StatusSeeAdapter()
        val layoutManger = LinearLayoutManager(requireActivity())
        statusUserSeeRec.layoutManager = layoutManger
        statusUserSeeRec.adapter = statusAdapter
        statusAdapter.addListSeeViewer(seeStatusModel)
        cancelStViewIV.setOnClickListener {
            storyBar.resume()
            userVideoStoryVV.resume()
            dismiss()
        }
    }

    companion object{
        const val TAG = "ActionBottomDialog"
        fun newInstance(userSeeCount:String,
                        seeStatusModel: ArrayList<StatusSeenData>,
                        storyBar: StoriesProgressView, userVideoStoryVV :VideoView,
                        current:Int
        ):ActionBottomDialogFragment =
            ActionBottomDialogFragment(userSeeCount,
                seeStatusModel,storyBar,
                userVideoStoryVV,current
            ).apply{

        }
    }










}