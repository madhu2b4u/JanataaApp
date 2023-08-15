package com.malkinfo.janataaapp.views.main.matrimony

import MyCommunityApp
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.ProfileBannerAdapter
import com.malkinfo.janataaapp.adapters.ProfileRecommendationAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.MatchProfileDetailsItem
import com.malkinfo.janataaapp.models.ProfileRecommendationItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator


class PartnerDetailsFragment : MyBaseFragment() {
    private var profileId: String? = null
    var partnerId: String? = null
    var partnerName: String? = null
    var partnerProfile: String? = null
    private lateinit var profileRecommendationAdapter: ProfileRecommendationAdapter
    private var profileRecommendationList: ArrayList<ProfileRecommendationItem> = ArrayList()
    private var profileBannerModel: ArrayList<String> = ArrayList()
    private lateinit var profileBannerAdapter: ProfileBannerAdapter
    private lateinit var partnerDetailsInterface: CommunityStore

    /**set ID*/
    private lateinit var dots_indicator: SpringDotsIndicator
    private lateinit var shortlistIV: CheckBox
    private lateinit var partnerNameTV:TextView
    private lateinit var partnerIdTV:TextView
    private lateinit var partnerDetailsTV:TextView
    private lateinit var aboutTV:TextView
    private lateinit var aboutPartnerDetailsTV:TextView
    private lateinit var dobTV:TextView
    private lateinit var maritalStatusTV:TextView
    private lateinit var languageTV:TextView
    private lateinit var locationTV:TextView
    private lateinit var phoneNumberTV:TextView
    private lateinit var educationalPlaceTV:TextView
    private lateinit var professionTV:TextView
    private lateinit var professionIV:ImageView
    private lateinit var annualIncomeTV:TextView
    private lateinit var qualificationTV:TextView
    private lateinit var starTV:TextView
    private lateinit var horoScopeTV:TextView
    private lateinit var familyTypeTV:TextView
    private lateinit var familyMembersTV:TextView
    private lateinit var actionBackIV:ImageView
    private lateinit var callIV:ImageView
    private lateinit var moreOptionsIV:ImageView
    private lateinit var chatIV:ImageView
    private lateinit var appBarLayout:AppBarLayout
    private lateinit var profileRecommendationRV:RecyclerView
    private lateinit var nestedScroll:NestedScrollView
    private lateinit var  partnerProfileImageVP :ViewPager

    /**=============================================================*/



    private  lateinit var listener : CommunityStore


    companion object {

        @JvmStatic
        fun newInstance(id: String?, profileUrl: String?, fullName: String?) =
            PartnerDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.PARTNER_ID, id)
                    putString(MyCommunityAppConstants.MSG_RECEIVER_PROFILE, profileUrl)
                    putString(MyCommunityAppConstants.MSG_RECEIVER_NAME, fullName)
                }
            }
    }

    private val partnerDetailsViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            partnerId = requireArguments().getString(MyCommunityAppConstants.PARTNER_ID)
            partnerName = requireArguments().getString(MyCommunityAppConstants.MSG_RECEIVER_NAME)
            partnerProfile = requireArguments().getString(MyCommunityAppConstants.MSG_RECEIVER_PROFILE)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partner_details, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            partnerDetailsInterface = context as CommunityStore
        }

        if(context is CommunityStore){
            listener = context as CommunityStore
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsID(view)
        setUpLoader(partnerDetailsViewModel)
        initViews(view)
    }
    private fun initsID(v:View){
        dots_indicator = v.findViewById(R.id.dots_indicator)
        shortlistIV = v.findViewById(R.id.shortlistIV)
        partnerNameTV = v.findViewById(R.id.partnerNameTV)
        partnerIdTV = v.findViewById(R.id.partnerIdTV)
        partnerDetailsTV = v.findViewById(R.id.partnerDetailsTV)
        aboutTV = v.findViewById(R.id.aboutTV)
        aboutPartnerDetailsTV = v.findViewById(R.id.aboutPartnerDetailsTV)
        dobTV = v.findViewById(R.id.dobTV)
        maritalStatusTV = v.findViewById(R.id.maritalStatusTV)
        languageTV = v.findViewById(R.id.languageTV)
        locationTV = v.findViewById(R.id.locationTV)
        phoneNumberTV = v.findViewById(R.id.phoneNumberTV)
        educationalPlaceTV = v.findViewById(R.id.educationalPlaceTV)
        professionTV = v.findViewById(R.id.professionTV)
        professionIV = v.findViewById(R.id.professionIV)
        annualIncomeTV = v.findViewById(R.id.annualIncomeTV)
        qualificationTV = v.findViewById(R.id.qualificationTV)
        starTV = v.findViewById(R.id.starTV)
        horoScopeTV = v.findViewById(R.id.horoScopeTV)
        familyTypeTV = v.findViewById(R.id.familyTypeTV)
        familyMembersTV = v.findViewById(R.id.familyMembersTV)
        actionBackIV = v.findViewById(R.id.actionBackIV)
        callIV = v.findViewById(R.id.callIV)
        moreOptionsIV = v.findViewById(R.id.moreOptionsIV)
        chatIV = v.findViewById(R.id.chatIV)
        appBarLayout = v.findViewById(R.id.appBarLayout)
        profileRecommendationRV = v.findViewById(R.id.profileRecommendationRV)
        nestedScroll = v.findViewById(R.id.nestedScroll)
        partnerProfileImageVP = v.findViewById(R.id.partnerProfileImageVP)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }


    override fun onResume() {
        super.onResume()
        partnerDetailsViewModel.doGetMatchProfileDetails(partnerId!!)
    }

    private fun setData(user: MatchProfileDetailsItem) {

        profileBannerModel.clear()
        profileBannerModel.addAll(user.matrimony_registeration!!.profile_url!!)
        partnerProfileImageVP.adapter = profileBannerAdapter
        profileBannerAdapter.notifyDataSetChanged()
        dots_indicator.setViewPager(partnerProfileImageVP)
        autoScroll(profileBannerAdapter.count,partnerProfileImageVP)

        shortlistIV.isChecked = user.is_shortlisted!!

        /* callIV.setOnClickListener {
                 Intent(
             startActivity(
                     Intent.ACTION_DIAL,
                     Uri.fromParts("tel", MyCommunityApp.getUser(requireActivity())!!.mobile, null)
                 )
             )
         }*/
        profileId= user._id
        partnerNameTV.text = user.full_name
        partnerIdTV.text = MyCommunityApp.getUser(requireActivity())!!.registration_number
        val age = user.matrimony_registeration!!.age.toString() + "yrs" + ","
        val height = user.matrimony_registeration!!.height.toString() + "cm" + ","
        val caste = user.caste!!.caste + "," + "\n"
        val education = user.matrimony_registeration!!.education + ","
        val profession = user.matrimony_registeration!!.employed_in!!.employed_in
        partnerDetailsTV.text = age + height + caste + education + profession
        aboutTV.text = user.matrimony_registeration!!.about_myself

        val weight = user.matrimony_registeration!!.weight.toString() + "Kgs" + ","
        val physicalStatus = user.matrimony_registeration!!.physical_status + "person" + ","
        val bodyType = user.matrimony_registeration!!.body_type
        aboutPartnerDetailsTV.text = weight + physicalStatus + bodyType
        dobTV.text = user.DOB
        maritalStatusTV.text = user.matrimony_registeration!!.marital_status
        languageTV.text = user.matrimony_registeration!!.mother_tongue!!.mother_tongue

        val city = user.matrimony_registeration!!.city_living_in!!.city
        val stateAddress = user.matrimony_registeration!!.state_living_in!!.state
        val country = user.matrimony_registeration!!.country_living_in!!.country
        locationTV.text = "Lives in" + city + "," + stateAddress + "," + country
        phoneNumberTV.text = user.mobile
        educationalPlaceTV.text = user.matrimony_registeration!!.employed_in!!.employed_in
        if(user.matrimony_registeration!!.employed_in!!.employed_in.equals("Private") ||user.matrimony_registeration!!.employed_in!!.employed_in.equals("Others") ){
            professionTV.visibility= VISIBLE
            professionIV.visibility= VISIBLE
            professionTV.text = user.mention_work
        }else{
            professionTV.visibility= GONE
            professionIV.visibility= GONE
        }
        annualIncomeTV.text=  user.matrimony_registeration!!.annual_income.toString()
        qualificationTV.text =  user.matrimony_registeration!!.education
        starTV.text= user.matrimony_registeration!!.star!!.star
        horoScopeTV.text=  user.matrimony_registeration!!.moon!!.moon_sign
        familyTypeTV.text=  user.matrimony_registeration!!.family_type+"Family"+"," + user.matrimony_registeration!!.family_status +","+ user.matrimony_registeration!!.ancestral_origin+"Origin"
        familyMembersTV.text="Father Occupation : "+ user.matrimony_registeration!!.fathers_occupation+","+"Mother Occupation :"+ user.matrimony_registeration!!.mother_occupation


        shortlistIV.setOnClickListener {
            partnerDetailsViewModel.doShortlist(profileId!!)
            user.is_shortlisted = ! user.is_shortlisted!!
        }
    }





    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViews(view: View) {

        actionBackIV.setOnClickListener {
            listener.onBack()
            requireActivity().viewModelStore.clear()
        }

        callIV.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", "0123456789", null)
                )
            )
        }
        moreOptionsIV.setOnClickListener { pos->
            openMoreOptionMenu(pos)
        }

        chatIV.setOnClickListener {
            partnerDetailsInterface.onOpenChatClicked(partnerId!!, partnerProfile, partnerName)
        }

        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (verticalOffset == 0) {
                    shortlistIV.visibility = VISIBLE
                    callIV.visibility = VISIBLE
                    moreOptionsIV.visibility = VISIBLE
                    dots_indicator.visibility = VISIBLE

                } else {
                    actionBackIV.imageTintList = requireActivity().getColorStateList(R.color.black)
                    shortlistIV.visibility = GONE
                    callIV.visibility = GONE
                    moreOptionsIV.visibility = GONE
                    dots_indicator.visibility = GONE
                    //toolbar.title=getString(R.string.profiledetails)
                    //toolbar.setTitleTextColor(requireActivity().getColor(R.color.textBlack))
                }
            })

        profileRecommendationAdapter = ProfileRecommendationAdapter(
            view.context,
            profileRecommendationList
        )
        profileRecommendationRV.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        profileRecommendationRV.adapter = profileRecommendationAdapter

        profileRecommendationAdapter.onItemClicked= { pos ->
            println("-----" + profileRecommendationList[pos]._id!!)
            partnerDetailsViewModel.doGetMatchProfileDetails(profileRecommendationList[pos]._id!!)
            nestedScroll.fullScroll(FOCUS_UP)
            appBarLayout.setExpanded(true)
        }
    }


    private fun openMoreOptionMenu(pos: View) {
        val popupMenu = PopupMenu(requireActivity(), moreOptionsIV)
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_block
                -> openBlockAlertDialog(pos)

                R.id.action_report_profile
                -> partnerDetailsInterface.onReportProfileClicked(profileId)
            }
            true
        })
        popupMenu.show()
    }

    private fun openBlockAlertDialog(pos: View) {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.block_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                partnerDetailsViewModel.doBlockMatrimonyProfile(profileId!!)
            })
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        profileBannerAdapter = ProfileBannerAdapter(requireContext(), profileBannerModel)
    }


    override fun initObservers() {
        partnerDetailsViewModel.matchProfileDetailsLiveData.observe(
            this,
            Observer {
                if (it != null) {
                    if (isAdded) {
                        if (it.user != null) {
                            setData(it.user!!)
                        }
                    }


                    if (it.recommended!!.any()) {
                        profileRecommendationList.clear()
                        profileRecommendationList.addAll(it.recommended!!)
                        profileRecommendationAdapter.notifyDataSetChanged()
                    }
                }
            })

        partnerDetailsViewModel.blockMatrimonyProfileLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar(it.message!!)
                listener.onBack()
                requireActivity().viewModelStore.clear()
            }
        })

        partnerDetailsViewModel.shortListLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                    // showSnackbar(it.message!!)
                }
            }
        })
    }

    override fun onPause() {
        handler!!.removeMessages(0)
        super.onPause()

    }

    override fun onDestroy() {
        handler!!.removeMessages(0)
        super.onDestroy()

    }

}