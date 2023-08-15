package com.malkinfo.janataaapp.views.launch

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.LaunchStore
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FeatureSelectionFragment : MyBaseFragment() {

    private lateinit var featureSelectionInterface: LaunchStore
    private var featureSelectionModel : ArrayList<String> = ArrayList()
    private var getFeatureModel : ArrayList<String> = ArrayList()
    /**set id here*/
    private lateinit var communityChatCB: CheckBox
    private lateinit var joinBT: MaterialButton
    private lateinit var communityChatTV: TextView
    private lateinit var matrimonyCB:CheckBox
    private lateinit var matrimonyTV:TextView
    private lateinit var bloodCB:CheckBox
    private lateinit var bloodTV:TextView



    var semiBoldFont: Typeface? = null
    var regularFont: Typeface? = null

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }


    override fun onErrorCalled(it: String?) {
        if(it!=null){
            showSnackbar(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feature_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader(viewModel)
        initViews(view)
    }

    private fun initViews(v:View) {
        /**call Id*/
        communityChatCB = v.findViewById(R.id.communityChatCB)
        joinBT = v.findViewById(R.id.joinBT)
        communityChatTV = v.findViewById(R.id.communityChatTV)
        matrimonyCB = v.findViewById(R.id.matrimonyCB)
        matrimonyTV = v.findViewById(R.id.matrimonyTV)
        bloodCB = v.findViewById(R.id.bloodCB)
        bloodTV = v.findViewById(R.id.bloodTV)
        communityChatCB.setOnCheckedChangeListener { p0, isCommunity ->
            if (isCommunity) {
                joinBT.isClickable = true
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE, true)
                communityChatTV.setTextColor(resources.getColor(R.color.textBlack))
                semiBoldFont = Typeface.createFromAsset(requireActivity().assets, "font/lexend_semibold.ttf")
                communityChatTV.typeface = semiBoldFont
                joinBT.alpha = 0.9F
                featureSelectionModel.add(MyCommunityAppConstants.COMMUNITY_CHAT_ID)

            } else {
                joinBT.isClickable = false
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE, false)
                communityChatTV.setTextColor(resources.getColor(R.color.textGrey))
                regularFont = Typeface.createFromAsset(requireActivity().assets, "font/lexend_regular.ttf")
                communityChatTV.typeface = regularFont
                featureSelectionModel.remove(MyCommunityAppConstants.COMMUNITY_CHAT_ID)

                if (!sharedPrefManager
                        .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE) && !sharedPrefManager
                        .getBooleanPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE)) {
                    joinBT.alpha = 0.5F
                } else {
                    joinBT.alpha = 0.9F
                }
            }
        }

        matrimonyCB.setOnCheckedChangeListener { p0, isMatrimoy ->
            if (isMatrimoy) {
                joinBT.isClickable = true
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE, true)
                matrimonyTV.setTextColor(resources.getColor(R.color.textBlack))
                semiBoldFont =
                    Typeface.createFromAsset(requireActivity().assets, "font/lexend_semibold.ttf")
                matrimonyTV.typeface = semiBoldFont
                joinBT.alpha = 0.9F
                featureSelectionModel.add(MyCommunityAppConstants.MATRIMONY_ID)
            } else {
                joinBT.isClickable = false
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE, false)
                matrimonyTV.setTextColor(resources.getColor(R.color.textGrey))
                regularFont =
                    Typeface.createFromAsset(requireActivity().assets, "font/lexend_regular.ttf")
                matrimonyTV.typeface = regularFont
                featureSelectionModel.remove(MyCommunityAppConstants.MATRIMONY_ID)
                if (!sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE) && !sharedPrefManager
                        .getBooleanPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE)) {
                    joinBT.alpha = 0.5F
                } else {
                    joinBT.alpha = 0.9F
                }
            }
        }

        bloodCB.setOnCheckedChangeListener { p0, isBloodGroup ->
            if (isBloodGroup) {
                joinBT.isClickable = true
                sharedPrefManager
                    .setPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE, true)
                joinBT.alpha = 0.9F
                bloodTV.setTextColor(resources.getColor(R.color.textBlack))
                semiBoldFont =
                    Typeface.createFromAsset(requireActivity().assets, "font/lexend_semibold.ttf")
                bloodTV.typeface = semiBoldFont

                featureSelectionModel.add(MyCommunityAppConstants.BLOOD_DONATION_ID)
            } else {
                joinBT.isClickable = false
                sharedPrefManager
                    .setPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE, false)
                bloodTV.setTextColor(resources.getColor(R.color.textGrey))
                regularFont =
                    Typeface.createFromAsset(requireActivity().assets, "font/lexend_regular.ttf")
                bloodTV.typeface = regularFont
                featureSelectionModel.remove(MyCommunityAppConstants.BLOOD_DONATION_ID)
                if (!sharedPrefManager
                        .getBooleanPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE) && !sharedPrefManager
                        .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE)) {
                    joinBT.alpha = 0.5F
                } else {
                    joinBT.alpha = 0.9F
                }
            }
        }


        joinBT.setOnClickListener {
            if (sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE)
                || sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE)
                || sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE)
            ) {

                //Do FeatureEnable
                val main = JSONObject()
                val array = JSONArray()
                for(i in featureSelectionModel){
                    array.put(i)
                }

                try {
                    main.put("features_enabled", array)
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    viewModel.doFeatureEnable(gsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            } else {
                showSnackbar("Please join atleast one feature")
            }

        }


    }

    override fun initObservers() {

        viewModel.featureEnableLiveData.observe(this, Observer {
            if(it!=null){
                featureSelectionInterface.onFeatureClicked()
            }
        })


    }




    companion object {

        @JvmStatic
        fun newInstance() =
            FeatureSelectionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LaunchStore) {

            featureSelectionInterface = context as LaunchStore
        }
    }
}