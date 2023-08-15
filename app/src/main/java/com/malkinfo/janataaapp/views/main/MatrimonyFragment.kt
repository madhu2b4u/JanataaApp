package com.malkinfo.janataaapp.views.main

import android.app.Dialog
import android.content.Context
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
import com.malkinfo.janataaapp.adapters.MatchProfileAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.GuideLineModel
import com.malkinfo.janataaapp.models.Matrimony.MatchProfileItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MatrimonyFragment : MyBaseFragment() {
    private var tempPage: Int = 0
    private var removedPosition: Int? = null
    private var featureSelectionModel: ArrayList<String> = ArrayList()
    private var matchProfileModel: ArrayList<MatchProfileItem> = ArrayList()
    private var tempMatchProfileModel: ArrayList<MatchProfileItem> = ArrayList()
    private lateinit var matchProfileAdapter: MatchProfileAdapter
    private lateinit var matrimonyGuideLineDialog: Dialog
    private lateinit var matrimonyInterface: CommunityStore
    private lateinit var matrimonyGuideLineAdapter: GuideLineAdapter
    private var guideLineModel: ArrayList<GuideLineModel> = ArrayList()

    private var totalLimit: Int? = null
    var page = 1
    var pageLimit = 10

    /**set id*/
    private lateinit var matrimonyEnableLayout:View
    private lateinit var matrimonyDetailsCL:ConstraintLayout
    private lateinit var enableIV:ImageView
    private lateinit var enableThisFeatureHintTV:TextView
    private lateinit var enableFeatureBT: MaterialButton
    private lateinit var userProfileIV: CircleImageView
    private lateinit var userNameTV:TextView
    private lateinit var matrimonyChatIV:ImageView
    private lateinit var matrimonyRV:RecyclerView
    private lateinit var locationIV:ImageView
    private lateinit var professionIV:ImageView
    private lateinit var starIV:ImageView
    private lateinit var educationIV:ImageView

    companion object {
        @JvmStatic
        fun newInstance() =
            MatrimonyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private val matchProfileViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_matrimony, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inits(view)
        setUpLoader(viewModel)
        setUpLoader(matchProfileViewModel)
        goGetMatchProfile(view)
    }
    private fun inits(v:View){
        matrimonyEnableLayout = v.findViewById(R.id.matrimonyEnableLayout)
        matrimonyDetailsCL = v.findViewById(R.id.matrimonyDetailsCL)
        enableIV = v.findViewById(R.id.enableIV)
        enableThisFeatureHintTV = v.findViewById(R.id.enableThisFeatureHintTV)
        enableFeatureBT = v.findViewById(R.id.enableFeatureBT)
        userProfileIV = v.findViewById(R.id.userProfileIV)
        userNameTV = v.findViewById(R.id.userNameTV)
        matrimonyChatIV = v.findViewById(R.id.matrimonyChatIV)
        matrimonyRV = v.findViewById(R.id.matrimonyRV)
        locationIV = v.findViewById(R.id.locationIV)
        professionIV = v.findViewById(R.id.professionIV)
        starIV = v.findViewById(R.id.starIV)
        educationIV = v.findViewById(R.id.educationIV)
    }


    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    private fun goGetMatchProfile(view: View) {
        if (SharedPrefManager.getInstance(view.context)
                .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE)
        ) {

            if (SharedPrefManager.getInstance(view.context)
                    .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
            ) {

                matrimonyEnableLayout.visibility = GONE
                matrimonyDetailsCL.visibility = VISIBLE
                matchProfileModel.clear()
                page = 1
                matchProfileViewModel.doDiscoverMatchProfile(page.toString(), pageLimit.toString())

            } else {
                if (SharedPrefManager.getInstance(view.context)
                        .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_REGISTERED_USER)
                ) {
                    matrimonyInterface.openPartnerPreferenceFragment(false)
                } else {
                    matrimonyInterface.openFindPerfectMatchFragment()
                }
            }

        } else {
            matrimonyEnableLayout.visibility = VISIBLE
            matrimonyDetailsCL.visibility = GONE
        }
        enableIV.load(R.drawable.matrimony)
        enableThisFeatureHintTV.text =
            getString(R.string.to_join_the_community_chat_tap_the_button_below_to_enable_the_matrimony_feature)
        enableFeatureBT.setOnClickListener {
            openGuideLinesDialog(view)
        }


        if (MyCommunityApp.getUser(view.context)!!.profile_url.isNullOrEmpty()) {
            userProfileIV.load(R.drawable.profile_iv)
        } else {
            userProfileIV.load(MyCommunityApp.getUser(view.context)!!.profile_url)
        }

        userNameTV.text = MyCommunityApp.getUser(view.context)!!.full_name

        matrimonyChatIV.setOnClickListener {
            matrimonyInterface.onChatClicked()
        }

    }

    override fun initObservers() {
        viewModel.matrimonyFeatureEnableLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                    if (it.featureEnable!![0] == MyCommunityAppConstants.MATRIMONY_ID) {
                        if (sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)) {

                            matchProfileViewModel.doDiscoverMatchProfile(
                                page.toString(),
                                pageLimit.toString()
                            )
                        } else {
                            if (sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_REGISTERED_USER)) {
                                matrimonyInterface.openPartnerPreferenceFragment(false)
                            } else {
                                matrimonyInterface.openFindPerfectMatchFragment()
                            }
                        }
                    }
                }
            }
        })
        matchProfileViewModel.matchProfileLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                    if (it.total_count != null) {
                        totalLimit = it.total_count
                        if (page == 1) {
                            initViews()
                        }
                    }
                    if (it.user!!.any()) {
                        if (page == 1) {
                            matchProfileModel.clear()
                            matchProfileModel.addAll(it.user!!)
                            matchProfileAdapter.notifyDataSetChanged()
                        } else {
                            if(tempPage != page){
                                matchProfileModel.addAll(it.user!!)
                                matchProfileAdapter.notifyDataSetChanged()
                                tempPage = page
                            }
                        }
                    }
                }
            }
        })
        matchProfileViewModel.shortListLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                //    showSnackbar(it.message!!)
                    matchProfileAdapter.notifyDataSetChanged()
                }
            }
        })
        matchProfileViewModel.blockMatchProfileLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                  //  showSnackbar("Never Show Again")
                    matchProfileAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun initViews() {
        matrimonyDetailsCL.visibility = VISIBLE
        matrimonyRV.layoutManager = LinearLayoutManager(requireActivity())
        matchProfileAdapter = MatchProfileAdapter(requireActivity(), matchProfileModel,totalLimit)
        matrimonyRV.adapter = matchProfileAdapter


        matrimonyRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!matrimonyRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / pageLimit
                    if (page <= limit) {
                        page += 1
                        matchProfileViewModel.doDiscoverMatchProfile(page.toString(),pageLimit.toString())
                    }
                }
            }
        })

        matchProfileAdapter.onShortlistClicked = { pos ->
            matchProfileViewModel.doShortlist(matchProfileModel[pos]._id!!)
            matchProfileModel[pos].is_shortlisted = !matchProfileModel[pos].is_shortlisted!!

        }

        matchProfileAdapter.onItemClicked = { pos ->
            matrimonyInterface.onPartnerProfileClicked(matchProfileModel[pos]._id!!,
                matchProfileModel[pos].profile_url,matchProfileModel[pos].full_name)
        }

        matchProfileAdapter.onChatNowClicked = { pos->
            matrimonyInterface.onOpenChatClicked(matchProfileModel[pos]._id,
                matchProfileModel[pos].profile_url,matchProfileModel[pos].full_name)
        }

        matchProfileAdapter.onDontShowClicked = { pos ->
            matchProfileViewModel.doBlockMatchProfile(matchProfileModel[pos]._id!!)
            matchProfileModel.removeAt(pos)
            matchProfileAdapter.notifyItemRemoved(pos)
            matchProfileAdapter.notifyDataSetChanged()
        }

        locationIV.setOnClickListener {
            matrimonyInterface.onFilterClicked("location")
        }

        professionIV.setOnClickListener {
            matrimonyInterface.onFilterClicked("occupation")
        }

        starIV.setOnClickListener {
            matrimonyInterface.onFilterClicked("star")
        }

        educationIV.setOnClickListener {
            matrimonyInterface.onFilterClicked("education")
        }

    }

    private fun openGuideLinesDialog(view: View) {

        val guidelines_layout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.guidelines_dailog_layout,null)

        val guideLineTitleTV = guidelines_layout.findViewById<TextView>(R.id.guideLineTitleTV)
        val guideLinesRV = guidelines_layout.findViewById<RecyclerView>(R.id.guideLinesRV)
        val closeIV = guidelines_layout.findViewById<ImageView>(R.id.closeIV)
        val disAgreeCL = guidelines_layout.findViewById<ConstraintLayout>(R.id.disAgreeCL)
        val agreeAndContinueBT = guidelines_layout.findViewById<MaterialButton>(R.id.agreeAndContinueBT)
        matrimonyGuideLineDialog = Dialog(view.context)
        matrimonyGuideLineDialog.setContentView(guidelines_layout)
        matrimonyGuideLineDialog.setCanceledOnTouchOutside(false)
        matrimonyGuideLineDialog.show()

       guideLineTitleTV.text=getString(R.string.matrimony_guide_lines)
        addGuidelines()
        matrimonyGuideLineAdapter = GuideLineAdapter(view.context, guideLineModel)
        guideLinesRV.layoutManager = LinearLayoutManager(context)
        guideLinesRV.adapter = matrimonyGuideLineAdapter
        matrimonyGuideLineAdapter.notifyDataSetChanged()

        closeIV.setOnClickListener {
            matrimonyGuideLineDialog.dismiss()
        }

       disAgreeCL.setOnClickListener {
            matrimonyGuideLineDialog.dismiss()
        }
        agreeAndContinueBT.setOnClickListener {
            featureSelectionModel.add(MyCommunityAppConstants.MATRIMONY_ID)

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
                viewModel.doMatrimonyFeatureEnable(gsonObject)
                matrimonyEnableLayout.visibility = GONE
                matrimonyGuideLineDialog.dismiss()
                matrimonyEnableLayout.visibility = GONE
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    private fun addGuidelines() {
        guideLineModel.clear()
        guideLineModel.add(GuideLineModel(getString(R.string.this_is_only_to_provide_a_platform_to_get_married_appname_is_not_responsible_for_any_violation_of_personal_information)))
        guideLineModel.add(GuideLineModel(getString(R.string.friend_requests_should_be_initiated)))
        guideLineModel.add(GuideLineModel(getString(R.string.after_accepting_friend_request_full_profile_information_as_per_registration_details_will_be_visible_to_both)))
        guideLineModel.add(GuideLineModel(getString(R.string.you_can_get_any_required_information_on_your_personal_at_your_own_risk)))
        guideLineModel.add(GuideLineModel(getString(R.string.no_charges_will_be_collected_at_appname_for_matrimony_process)))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CommunityStore) {
            matrimonyInterface = context as CommunityStore
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().viewModelStore.clear()
    }
}