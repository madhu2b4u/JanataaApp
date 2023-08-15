package com.malkinfo.janataaapp.views.main

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import com.malkinfo.janataaapp.adapters.GroupAdapter
import com.malkinfo.janataaapp.adapters.GuideLineAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.GuideLineModel
import com.malkinfo.janataaapp.models.community.GroupModel
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class CommunityFragment : MyBaseFragment() {
    private var tempPage: Int = 0
    private var featureSelectionModel: ArrayList<String> = ArrayList()
    private lateinit var communityInterface: CommunityStore
    private lateinit var communityChatGuideLineDialog: Dialog
    private lateinit var communityChatGuideLineAdapter: GuideLineAdapter
    private var guideLineModel: ArrayList<GuideLineModel> = ArrayList()
    private var groupAdapter: GroupAdapter? = null
    private var groupModel: ArrayList<GroupModel> = ArrayList()
    var currentPosition: Int? = null
    private var totalLimit: Int? = null
    var page = 1
    var strPageLimit = 9
    /**set id*/
    private lateinit var communityEnableLayout:View
    private lateinit var communityGroupCL:ConstraintLayout
    private lateinit var enableIV:ImageView
    private lateinit var enableThisFeatureHintTV:TextView
    private lateinit var enableFeatureBT: MaterialButton
    private lateinit var communityGroupRV:RecyclerView

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setUpLoader(viewModel)
        setUpLoader(communityViewModel)
        doGetGroupList(view)

    }
    private fun init(v:View){
        communityEnableLayout = v.findViewById(R.id.communityEnableLayout)
        communityGroupCL = v.findViewById(R.id.communityGroupCL)
        enableIV = v.findViewById(R.id.enableIV)
        enableThisFeatureHintTV = v.findViewById(R.id.enableThisFeatureHintTV)
        enableFeatureBT = v.findViewById(R.id.enableFeatureBT)
        communityGroupRV = v.findViewById(R.id.communityGroupRV)
    }



    override fun onPause() {
        super.onPause()
        requireActivity().viewModelStore.clear()
    }


    override fun onErrorCalled(it: String?) {
        //showSnackbar(it!!)
    }

    private fun doGetGroupList(view: View) {
        if (SharedPrefManager.getInstance(view.context)
                .getBooleanPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE)
        ) {
            page = 1
            communityEnableLayout.visibility = GONE
            communityGroupCL.visibility = VISIBLE
            communityViewModel.getGroupList(page.toString(), strPageLimit.toString())
        } else {
            communityEnableLayout.visibility = VISIBLE
            communityGroupCL.visibility = GONE
        }

        enableIV.load(R.drawable.chat)
        enableThisFeatureHintTV.text =
            getString(R.string.to_join_the_community_chat_tap_the_button_below_to_enable_the_community_chat_feature)
        enableFeatureBT.setOnClickListener {
            openGuideLinesDialog()
        }
    }

    override fun initObservers() {
        viewModel.communityFeatureEnableLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                    if (it.featureEnable!![0] == MyCommunityAppConstants.COMMUNITY_CHAT_ID) {
                        communityViewModel.getGroupList(page.toString(), strPageLimit.toString())
                    }
                }
            }
        })

        communityViewModel.groupLiveData.observe(this, Observer {
            if (isAdded) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                }
                if (it.groups!!.any()) {
                    if (page == 1) {
                      //  groupModel.clear()
                        groupAddView(it.groups!!)
                        println("---model size---" + groupModel.size)
                        println("---group size---" + it.groups!!.size)
                        initViews()
                        groupAdapter?.notifyDataSetChanged()
                    } else {
                        if (tempPage != page) {
                            groupAddView(it.groups!!)
                            groupAdapter?.notifyDataSetChanged()
                            tempPage = page
                        }
                    }

                }
            }
        })

        /*communityViewModel.joinGroupLiveData.observe(this, Observer {
            if (it != null) {
                //showSnackbar(it.message!!)
            }
        })*/
    }

    private fun groupAddView(groups: ArrayList<GroupModel>) {
        // Define a custom comparator
//        val timeComparator = Comparator<GroupModel> { item1, item2 ->
//            val isTime1 = convertLongToTime(item1.created_at!!.toLong())
//            val isTime2 = convertLongToTime(item2.created_at!!.toLong())
//            Log.d("mTag","time = ${isTime1}")
//            Log.d("mTag","time = ${isTime2}")
//            isTime2.compareTo(isTime1)
//        }
//        Collections.sort(groups, timeComparator)

        for(groups_item in groups){

            when(groups_item._id){
                MyCommunityAppConstants.STATUS_STORY_GROUP_ID->{}
                MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP->{}
                else->{
               //   val timeLog = convertLongToTime(groups_item.created_at!!.toLong())
                 //   Log.d("mTag","time Log = $timeLog")
                    if(groupContains(groupModel,groups_item._id)){
                        Log.d("mTag","group already add")

                    }else{
                        groupModel.add(groups_item)
                    }

                    Log.d("mTag","group list = $groupModel")
                }
            }

        }

    }

    fun groupContains(list: ArrayList<GroupModel>, name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLongToTime(longValue: Long): String {
        // Assuming the long value represents time in milliseconds
        val instant = Instant.ofEpochMilli(longValue)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(formatter)
    }


    private fun initViews() {

        communityGroupCL.visibility = VISIBLE

        groupAdapter = GroupAdapter(requireActivity(), groupModel, totalLimit)
        communityGroupRV.layoutManager = LinearLayoutManager(activity)
        communityGroupRV.adapter = groupAdapter
         groupAdapter!!.notifyDataSetChanged()

        communityGroupRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!communityGroupRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / strPageLimit
                    if (page <= limit) {
                        page += 1
                        communityViewModel.getGroupList(page.toString(), strPageLimit.toString())
                    }
                }
            }
        })


        groupAdapter!!.onJoinClicked = { pos, isJoin, joinTV ->
            currentPosition = pos
            communityViewModel.doJoinGroup(groupModel[pos]._id!!)
            Log.d("mTag","groupId = ${groupModel[pos]._id}")

            groupModel[pos].is_joined = !groupModel[pos].is_joined!!

            if (groupModel[currentPosition!!].is_joined!!) {
                groupModel.get(currentPosition!!).total_members =
                    groupModel.get(currentPosition!!).total_members!! + 1
                groupAdapter!!.notifyDataSetChanged()
            } else {
                groupModel.get(currentPosition!!).total_members =
                    groupModel.get(currentPosition!!).total_members!! - 1
                groupAdapter!!.notifyDataSetChanged()
            }
        }

        groupAdapter!!.onMuteClicked = { pos: Int, ismute: Boolean ->
            groupModel[pos].isMute = !groupModel[pos].isMute!!
            groupAdapter!!.notifyDataSetChanged()
        }

        groupAdapter!!.onItemClicked = { pos ->
            if (groupModel[pos].is_joined!!) {
                communityInterface.onCommunityGroupItemClicked(
                    groupModel[pos]._id!!,
                    groupModel[pos].group!!
                )
            } else {
                showSnackbar("Please Join the Group")
            }
        }
    }

    private fun openGuideLinesDialog() {
        val guidelines_layout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.guidelines_dailog_layout,null)
        val guideLineTitleTV = guidelines_layout.findViewById<TextView>(R.id.guideLineTitleTV)
        val guideLinesRV = guidelines_layout.findViewById<RecyclerView>(R.id.guideLinesRV)
        val closeIV = guidelines_layout.findViewById<ImageView>(R.id.closeIV)
        val disAgreeCL = guidelines_layout.findViewById<ConstraintLayout>(R.id.disAgreeCL)
        val agreeAndContinueBT = guidelines_layout.findViewById<MaterialButton>(R.id.agreeAndContinueBT)
        communityChatGuideLineDialog = Dialog(requireActivity())
        communityChatGuideLineDialog.setContentView(guidelines_layout)
        communityChatGuideLineDialog.setCanceledOnTouchOutside(false)
        communityChatGuideLineDialog.show()


        guideLineTitleTV.text =
            getString(R.string.community_guide_lines)
        addGuidelines()

        communityChatGuideLineAdapter = GuideLineAdapter(requireActivity(), guideLineModel)
        guideLinesRV.layoutManager = LinearLayoutManager(context)
        guideLinesRV.adapter = communityChatGuideLineAdapter
        communityChatGuideLineAdapter.notifyDataSetChanged()

        closeIV.setOnClickListener {
            communityChatGuideLineDialog.dismiss()
        }

       disAgreeCL.setOnClickListener {
            communityChatGuideLineDialog.dismiss()
        }
       agreeAndContinueBT.setOnClickListener {
            featureSelectionModel.add(MyCommunityAppConstants.COMMUNITY_CHAT_ID)
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
                viewModel.doCommunityFeatureEnable(gsonObject)
                communityEnableLayout.visibility = GONE
                communityChatGuideLineDialog.dismiss()
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

    companion object {
        @JvmStatic
        fun newInstance() =
            CommunityFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            communityInterface = context as CommunityStore
        }
    }



}