package com.malkinfo.janataaapp.views.main.community

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.GuideLineAdapter
import com.malkinfo.janataaapp.adapters.RecentMeetingAdapter
import com.malkinfo.janataaapp.adapters.UpcomingMeetingAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.DividerItemDecorator
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.models.GuideLineModel
import com.malkinfo.janataaapp.models.community.RecentMeetingModel
import com.malkinfo.janataaapp.models.community.UpcomingMeetingModel
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class GroupMeetingFragment : MyBaseFragment() {

    private var upcomingMeetingModel: ArrayList<UpcomingMeetingModel> = ArrayList()
    private lateinit var upcomingMeetingAdapter: UpcomingMeetingAdapter
    private var recentMeetingModel: ArrayList<RecentMeetingModel> = ArrayList()
    private lateinit var recentMeetingAdapter: RecentMeetingAdapter
    var groupId: String? = null
    /**set Id*/
    private lateinit var noRecentMeetingTV: TextView
    private lateinit var noUpComingMeetingTV:TextView
    private lateinit var recentMeetingRV:RecyclerView
    private lateinit var upcomingMeetingRV:RecyclerView
    private lateinit var backIV:ImageView
    private lateinit var meetingCLS:ConstraintLayout
    /**====================================================*/
    /**meeting Enable */
    private lateinit var meetingEnableLayout :View
    private lateinit var enableIV:ImageView
    private lateinit var enableThisFeatureHintTV:TextView
    private lateinit var enableFeatureBT: MaterialButton

    private var totalLimit: Int? = null
    var page = 1
    var strPageLimit = 10

    private lateinit var meetingGuideLineDialog: Dialog
    private var guideLineModel: ArrayList<GuideLineModel> = ArrayList()
    private lateinit var meetingGuideLineAdapter: GuideLineAdapter

    private var featureSelectionModel: ArrayList<String> = ArrayList()
    /**======================*/

    private val groupMeetingViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupId = requireArguments().getString(MyCommunityAppConstants.GROUP_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_meeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inisId(view)
        setUpLoader(groupMeetingViewModel)
        doGetGroupList(view)
    }
    private fun inisId(v:View){
        noRecentMeetingTV  = v.findViewById(R.id.noRecentMeetingTV)
        noUpComingMeetingTV = v.findViewById(R.id.noUpComingMeetingTV)
        recentMeetingRV = v.findViewById(R.id.recentMeetingRV)
        upcomingMeetingRV = v.findViewById(R.id.upcomingMeetingRV)
        backIV = v.findViewById(R.id.backIV)
        /**meet enable */
        meetingEnableLayout = v.findViewById(R.id.meetingEnableLayout)
        enableIV = v.findViewById(R.id.enableIV)
        enableThisFeatureHintTV = v.findViewById(R.id.enableThisFeatureHintTV)
        enableFeatureBT = v.findViewById(R.id.enableFeatureBT)
        meetingCLS = v.findViewById(R.id.meetingCLS)
        /**================================================*/
    }
    private fun doGetGroupList(view: View) {
        if (SharedPrefManager.getInstance(view.context)
                .getBooleanPreference(MyCommunityAppConstants.IS_MEET_GROUP_ENABLE)
        ) {
            meetingEnableLayout.visibility = GONE
            meetingCLS.visibility = VISIBLE
           // meetingEnableLayout.getGroupList(page.toString(), strPageLimit.toString())
            groupMeetingViewModel.doGetMeeting(groupId!!)
        } else {
            meetingEnableLayout.visibility = VISIBLE
            meetingCLS.visibility = GONE
        }

        enableIV.load(R.drawable.group_meeting)
        enableThisFeatureHintTV.text =
            getString(R.string.to_enable_meeting_feature)
        enableFeatureBT.setOnClickListener {
            openGuideLinesDialog()
        }
    }
    override fun onPause() {
        super.onPause()
        requireActivity().viewModelStore.clear()
    }
    private fun openGuideLinesDialog() {
        val guidelines_layout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.guidelines_dailog_layout,null)
        val guideLineTitleTV = guidelines_layout.findViewById<TextView>(R.id.guideLineTitleTV)
        val guideLinesRV = guidelines_layout.findViewById<RecyclerView>(R.id.guideLinesRV)
        val closeIV = guidelines_layout.findViewById<ImageView>(R.id.closeIV)
        val disAgreeCL = guidelines_layout.findViewById<ConstraintLayout>(R.id.disAgreeCL)
        val agreeAndContinueBT = guidelines_layout.findViewById<MaterialButton>(R.id.agreeAndContinueBT)
        meetingGuideLineDialog = Dialog(requireActivity())
        meetingGuideLineDialog.setContentView(guidelines_layout)
        meetingGuideLineDialog.setCanceledOnTouchOutside(false)
        meetingGuideLineDialog.show()


        guideLineTitleTV.text =
            getString(R.string.meeting_guide_lines)
        addGuidelines()

        meetingGuideLineAdapter = GuideLineAdapter(requireActivity(), guideLineModel)
        guideLinesRV.layoutManager = LinearLayoutManager(context)
        guideLinesRV.adapter = meetingGuideLineAdapter
        meetingGuideLineAdapter.notifyDataSetChanged()

        closeIV.setOnClickListener {
            meetingGuideLineDialog.dismiss()
        }

        disAgreeCL.setOnClickListener {
            meetingGuideLineDialog.dismiss()
        }
        agreeAndContinueBT.setOnClickListener {
            featureSelectionModel.add(MyCommunityAppConstants.MEETING_ID)
            //Do FeatureEnable
            val main = JSONObject()
            val array = JSONArray()
            for (i in featureSelectionModel) {
                array.put(i)
            }
            try {
                main.put("features_enabled", array)
                val jsonParser = JsonParser()
                val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                viewModel.doMEETINGFeatureEnable(gsonObject)
                meetingEnableLayout.visibility = GONE
                meetingGuideLineDialog.dismiss()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
    private fun addGuidelines() {
        guideLineModel.clear()
        guideLineModel.add(GuideLineModel(getString(R.string.no_abuse_texts_should_be_posted_shared)))
        guideLineModel.add(GuideLineModel(getString(R.string.no_abuse_videos_should_be_posted_shared)))
        guideLineModel.add(GuideLineModel(getString(R.string.if_any_abuse_text_or_video_is_found_with_user_s_details_that_user_will_bewarned_for_first_time_if_it_repeats_user_will_be_disabled_from_communitychat_facility_or_respective_group)))
        guideLineModel.add(GuideLineModel(getString(R.string.if_any_reports_found_on_users_posts_shared_content_adminauthorized_person_will_take_disciplinary_action_on_user)))
    }


    override fun initObservers() {
        groupMeetingViewModel.meetingLiveData.observe(this, Observer {
            if (it != null) {
                initViews()
                if (it.recent_meetings!!.any()) {
                    recentMeetingModel.clear()
                    recentMeetingModel.addAll(it.recent_meetings!!)
                    recentMeetingAdapter.notifyDataSetChanged()
                    noRecentMeetingTV.visibility = GONE

                } else {
                    noRecentMeetingTV.visibility = VISIBLE
                }

                if (it.upcoming_meetings!!.any()) {
                    upcomingMeetingModel.clear()
                    upcomingMeetingModel.addAll(it.upcoming_meetings!!)
                    upcomingMeetingAdapter.notifyDataSetChanged()
                    noUpComingMeetingTV.visibility = GONE

                } else {
                    noUpComingMeetingTV.visibility = VISIBLE
                }
            }
        })
        viewModel.meetingFeatureEnableLiveData.observe(this,Observer{
            if (isAdded) {
                if (it != null) {
                    if (it.featureEnable!![0] == MyCommunityAppConstants.MEETING_ID) {
                        groupMeetingViewModel.doGetMeeting(groupId!!)
                    }
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initViews() {
        meetingCLS.visibility = VISIBLE
        recentMeetingRV.layoutManager = LinearLayoutManager(requireActivity())
        recentMeetingAdapter = RecentMeetingAdapter(requireActivity(), recentMeetingModel)
        recentMeetingRV.adapter = recentMeetingAdapter
        recentMeetingRV.addItemDecoration(
            DividerItemDecorator(
                activity?.applicationContext!!,
                showFirstDivider = true,
                showLastDivider = false
            )
        )
        recentMeetingAdapter.notifyDataSetChanged()

        upcomingMeetingRV.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        upcomingMeetingAdapter = UpcomingMeetingAdapter(requireActivity(), upcomingMeetingModel)
        upcomingMeetingRV.adapter = upcomingMeetingAdapter
        upcomingMeetingAdapter.notifyDataSetChanged()


        upcomingMeetingAdapter.onJoinMeetingClicked = { pos ->
            val uri = Uri.parse(upcomingMeetingModel[pos].meeting_link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        backIV.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }


    companion object {

        @JvmStatic
        fun newInstance(groupId: String?) =
            GroupMeetingFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                }
            }
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

}