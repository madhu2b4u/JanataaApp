package com.malkinfo.janataaapp.views.main

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.BannerSliderAdapter
import com.malkinfo.janataaapp.adapters.BloodGroupRequestAdapter
import com.malkinfo.janataaapp.adapters.GuideLineAdapter
import com.malkinfo.janataaapp.adapters.MyBloodGroupRequestAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.models.BloodGroupItem
import com.malkinfo.janataaapp.models.GuideLineModel
import com.malkinfo.janataaapp.models.bloodgroup.BloodGroupRequestItem
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel
import com.malkinfo.janataaapp.viewmodels.BloodGroupViewModel
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import com.malkinfo.janataaapp.views.main.bloodreq.RequestForm
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * ------------------------------------------
 * Editor by Farida Shekh.
 * This Community App BloodGroupFragment Page Fragment.
 * ------------------------------------------
 */

class BloodGroupFragment : MyBaseFragment() {
    private var  tempPage: Int = 0
    private lateinit var bloodGroupAdapter: ArrayAdapter<String>
    private lateinit var errorMsg: String
    private var location: String? = null
    private var bloodType: String? = null
    private var bloodGroupName: String? = null
    private lateinit var bloodGroupGuideLineAdapter: GuideLineAdapter
    private var guideLineModel: ArrayList<GuideLineModel> = ArrayList()
    private lateinit var bloodGroupGuideLineDialog: Dialog
    private lateinit var bloodGroupRequestAdapter: BloodGroupRequestAdapter
    private lateinit var myBloodGroupRequestAdapter: MyBloodGroupRequestAdapter
    private var bloodGroupRequestModel: ArrayList<BloodGroupRequestItem> = ArrayList()
    private var myBloodGroupRequestModel: ArrayList<BloodGroupRequestItem> = ArrayList()
    private var bloodGroupModel: ArrayList<BloodGroupItem> = ArrayList()
    private var bloodGroupNameList: ArrayList<String> = ArrayList()
    private var featureSelectionModel: ArrayList<String> = ArrayList()

    private var totalLimit: Int? = null
    var page = 1
    var pageLimit = 10

    /**set id*/
    private lateinit var spinner:Spinner
    private lateinit var bloodGroupRequestRV:RecyclerView
    private lateinit var myRequestRV:RecyclerView
    private lateinit var bloodEnableLayout :View
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var enableIV: ImageView
    private lateinit var enableThisFeatureHintTV:TextView
    private lateinit var enableFeatureBT: MaterialButton
    private lateinit var myRequestsTV:TextView
    private lateinit var bloodRequestBT:MaterialButton
    private lateinit var locationET:EditText
    private lateinit var refreshRequestIV:ImageView
    private lateinit var filterBloodGroupIV:ImageView

    /**view Pager*/
    private lateinit var vPBannerADIVBlood:ViewPager2

    /**Banner Ad ViewPager image slider*/
    private lateinit var sliderItemList:ArrayList<HomePostModel>
    private lateinit var sliderAdpter: BannerSliderAdapter
    private lateinit var sliderHandle: Handler
    private lateinit var sliderRun:Runnable
    /**============================================*/
    var bannerViewcount:Int = 0


    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private val bloodGroupRequestViewModel: BloodGroupViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BloodGroupViewModel::class.java)
    }
    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BloodGroupFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader(viewModel)
        setUpLoader(bloodGroupRequestViewModel)
        initViews(view)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    private fun initViews(view: View) {
        communityViewModel.getHomePosts(MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP,"?","?")
        spinner = view.findViewById(R.id.spinner)
        bloodGroupRequestRV = view.findViewById(R.id.bloodGroupRequestRV)
        myRequestRV = view.findViewById(R.id.myRequestRV)
        bloodEnableLayout = view.findViewById(R.id.bloodEnableLayout)
        nestedScrollView = view.findViewById(R.id.nestedScrollView)
        enableIV = view.findViewById(R.id.enableIV)
        enableThisFeatureHintTV = view.findViewById(R.id.enableThisFeatureHintTV)
        enableFeatureBT = view.findViewById(R.id.enableFeatureBT)
        myRequestsTV = view.findViewById(R.id.myRequestsTV)
        bloodRequestBT = view.findViewById(R.id.bloodRequestBT)
        locationET = view.findViewById(R.id.locationET)
        refreshRequestIV = view.findViewById(R.id.refreshRequestIV)
        filterBloodGroupIV = view.findViewById(R.id.filterBloodGroupIV)
        vPBannerADIVBlood = view.findViewById(R.id.vPBannerIV)

        addBloodGroupName()

        var aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bloodGroupNameList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(spinner)
        {
            adapter = aa
            setSelection(0, true)
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER
        }

        bloodGroupRequestRV.layoutManager = LinearLayoutManager(requireContext())
        myRequestRV.layoutManager = LinearLayoutManager(requireContext())

        bloodGroupAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, bloodGroupNameList)
        bloodGroupAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        /*spinner.adapter = bloodGroupAdapter*/

        myBloodGroupRequestAdapter = MyBloodGroupRequestAdapter(requireContext(), myBloodGroupRequestModel)
        myRequestRV.adapter = myBloodGroupRequestAdapter
        myRequestRV.isNestedScrollingEnabled=true

        bloodGroupGuideLineDialog = Dialog(view.context)

        if (sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE)) {
            page=1
            bloodGroupRequestModel.clear()
            bloodGroupRequestViewModel.getBloodGroupRequestList(page.toString(),pageLimit.toString())
            bloodEnableLayout.visibility = GONE
            //bloodAppbarLayout.visibility = VISIBLE
            nestedScrollView.visibility = VISIBLE
        } else {

            nestedScrollView.visibility = GONE
            bloodEnableLayout.visibility = VISIBLE
            //bloodAppbarLayout.visibility = GONE
        }
        myBloodGroupRequestAdapter.copyPasteItemClicked = {decribe->
            registerForContextMenu(decribe);
        }

        enableIV.load(R.drawable.bloodgroup)

        enableThisFeatureHintTV.text =
            getString(R.string.to_join_the_community_chat_tap_the_button_below_to_enable_the_blood_donation_feature)
        enableFeatureBT.setOnClickListener {
            openGuideLinesDialog()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                bloodGroupName = bloodGroupModel[pos].name
            }

        }

        if(myBloodGroupRequestModel.size>0){
            myRequestsTV.visibility= VISIBLE
        }else{
            myRequestsTV.visibility= GONE
        }

        myBloodGroupRequestAdapter.onremoveRequestClicked = { pos ->
            openRemoveRequestAlert(pos)
        }



        bloodRequestBT.setOnClickListener {
            if (validation()) {
                val bloodReqForm = RequestForm(requireActivity(),bloodGroupRequestViewModel)
                //Do blood request
                location = locationET.text.toString()
                bloodReqForm.showBloodRequest(location!!,bloodGroupName!!)
//                val main = JSONObject()
//                try {
//                    location = locationET.text.toString()
//                    main.put("blood", bloodGroupName)
//                    main.put("location", location)
//                    val jsonParser = JsonParser()
//                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
//                    bloodGroupRequestViewModel.doBloodGroupRequest(gsonObject)
//
//
//                    showImageConfirmation("",
//                        getString(R.string.ok),
//                        R.drawable.green_tick,
//                        getString(R.string.request_post_alert), "",
//                        DialogInterface.OnClickListener { dialogInterface, i ->
//                            dialogInterface.dismiss()
//                        })
//
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }

            } else {
                showSnackbar(errorMsg)
            }

        }

        refreshRequestIV.setOnClickListener {
            bloodGroupRequestViewModel.getBloodGroupRequestList(page.toString(),pageLimit.toString())
        }

        filterBloodGroupIV.setOnClickListener {
            openBloodGroupPopupMenu()
        }

    }
    /**Banner Ad ViewPager fun call*/
    private fun sliderView(){
        sliderItemList = ArrayList()
        sliderAdpter = BannerSliderAdapter(requireActivity(),vPBannerADIVBlood)
        vPBannerADIVBlood.adapter = sliderAdpter
        vPBannerADIVBlood.clipToPadding = false
        vPBannerADIVBlood.clipChildren = false

        vPBannerADIVBlood.currentItem = 2

        vPBannerADIVBlood.offscreenPageLimit = 3
        vPBannerADIVBlood.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        val composPageTarn = CompositePageTransformer()
        composPageTarn.addTransformer(MarginPageTransformer(40))
        composPageTarn.addTransformer(object : ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                val r: Float = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

        })

        vPBannerADIVBlood.setPageTransformer(composPageTarn)
        sliderHandle = Handler()

        sliderRun = object :Runnable{
            override fun run() {
                vPBannerADIVBlood.setCurrentItem(vPBannerADIVBlood.currentItem + 1)
            }

        }

        vPBannerADIVBlood.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandle.removeCallbacks(sliderRun)
                    sliderHandle.postDelayed(sliderRun, 3000)

                }
            }
        )



    }

    private fun validation(): Boolean {
        var formOk = true

        if (!FormValidator.requiredField(locationET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_your_location)

        } else if (spinner.selectedItem.equals(getString(R.string.select_blood_hint))) {
            formOk = false
            errorMsg = getString(R.string.select_blood_group)
        }
        return formOk
    }

    private fun openRemoveRequestAlert(pos: Int) {
        showAlertDialog(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.remove_request_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                bloodGroupRequestViewModel.doDeleteMyBloodGroupRequest(myBloodGroupRequestModel[pos]._id!!)
                dialogInterface.dismiss()
                myBloodGroupRequestModel.removeAt(pos)
                myBloodGroupRequestAdapter.notifyItemRemoved(pos)
                myBloodGroupRequestAdapter.notifyDataSetChanged()
            })
    }

    private fun openCallAlertDialog(pos: Int) {
        showAlertDialog(getString(R.string.no),
            getString(R.string.yes),
            "",
            "Call"+"\t"+ bloodGroupRequestModel[pos].user_id!!.full_name +"\t"+"regarding blood donation?",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.fromParts("tel", bloodGroupRequestModel[pos].mobile.toString(), null)
                    )
                )
            })

    }

    private fun openBloodGroupPopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), filterBloodGroupIV)
        popupMenu.menuInflater.inflate(R.menu.blood_group_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_a_positive ->
                    bloodType = item.title.toString()
                R.id.action_a_negative ->
                    bloodType = item.title.toString()
                R.id.action_b_positive ->
                    bloodType = item.title.toString()
                R.id.action_b_negative ->
                    bloodType = item.title.toString()
                R.id.action_ab_positive -> {
                    bloodType = item.title.toString()
                }
                R.id.action_ab_negative ->
                    bloodType = item.title.toString()
                R.id.action_o_positive ->
                    bloodType = item.title.toString()
                R.id.action_o_neagtive ->
                    bloodType = item.title.toString()
            }


            val main = JSONObject()
            try {
                main.put("blood_type", bloodType)
                val jsonParser = JsonParser()
                val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                bloodGroupRequestViewModel.doFindBloodGroupRequest(gsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            true
        })
        popupMenu.show()
    }

    private fun addBloodGroupName() {
        bloodGroupModel.clear()
        bloodGroupModel.add(BloodGroupItem("Select blood group"))
        bloodGroupModel.add(BloodGroupItem("A+"))
        bloodGroupModel.add(BloodGroupItem("A-"))
        bloodGroupModel.add(BloodGroupItem("AB+"))
        bloodGroupModel.add(BloodGroupItem("AB-"))
        bloodGroupModel.add(BloodGroupItem("O+"))
        bloodGroupModel.add(BloodGroupItem("O-"))
        bloodGroupModel.add(BloodGroupItem("B+"))
        bloodGroupModel.add(BloodGroupItem("B-"))

        bloodGroupNameList.clear()
        for (i in 0 until bloodGroupModel.size) {
            bloodGroupNameList.add(bloodGroupModel[i].name)
        }
    }

    private fun openGuideLinesDialog() {
        val guidelines_layout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.guidelines_dailog_layout,null)
        val guideLineTitleTV = guidelines_layout.findViewById<TextView>(R.id.guideLineTitleTV)
        val guideLinesRV = guidelines_layout.findViewById<RecyclerView>(R.id.guideLinesRV)
        val closeIV = guidelines_layout.findViewById<ImageView>(R.id.closeIV)
        val disAgreeCL  = guidelines_layout.findViewById<ConstraintLayout>(R.id.disAgreeCL)
        val agreeAndContinueBT = guidelines_layout.findViewById<MaterialButton>(R.id.agreeAndContinueBT)
        bloodGroupGuideLineDialog.setContentView(guidelines_layout)
        bloodGroupGuideLineDialog.setCanceledOnTouchOutside(false)
        bloodGroupGuideLineDialog.show()


        guideLineTitleTV.text=getString(R.string.blood_donation_guide_lines)

        addGuidelines()

        bloodGroupGuideLineAdapter = GuideLineAdapter(requireActivity(), guideLineModel)
        guideLinesRV.layoutManager = LinearLayoutManager(context)
        guideLinesRV.adapter = bloodGroupGuideLineAdapter
        bloodGroupGuideLineAdapter.notifyDataSetChanged()

       closeIV.setOnClickListener {
            bloodGroupGuideLineDialog.dismiss()
        }
        disAgreeCL.setOnClickListener {
            bloodGroupGuideLineDialog.dismiss()
        }
       agreeAndContinueBT.setOnClickListener {
            featureSelectionModel.add(MyCommunityAppConstants.BLOOD_DONATION_ID)
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
                viewModel.doBloodGroupFeatureEnable(gsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun addGuidelines() {
        guideLineModel.clear()
        guideLineModel.add(GuideLineModel(getString(R.string.this_is_only_to_provide_blood_availability_based_on_requirement)))
        guideLineModel.add(GuideLineModel(getString(R.string.we_are_not_responsible_for_any_misuse_of_blood)))
        guideLineModel.add(GuideLineModel(getString(R.string.no_charges_will_be_collected_at_appname_for_any_group_of_blood_requirements)))

    }


    override fun initObservers() {
        viewModel.bloodGroupFeatureEnableLiveData.observe(this, Observer {
            if (it != null) {
                if (it.featureEnable!![0] == MyCommunityAppConstants.BLOOD_DONATION_ID) {
                    bloodGroupGuideLineDialog.dismiss()
                    bloodEnableLayout.visibility = GONE
                    //bloodAppbarLayout.visibility = VISIBLE
                    nestedScrollView.visibility = VISIBLE
                    bloodGroupRequestViewModel.getBloodGroupRequestList(page.toString(),pageLimit.toString())
                }
            }
        })
        communityViewModel.viewPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.post != null) {
                    bannerViewcount = it.post!!.views_count!!
                }
            }
        })

        bloodGroupRequestViewModel.bloodGroupRequestListLiveData.observe(this, Observer {

                if (it != null) {

                    if(myBloodGroupRequestModel == null){
                        myBloodGroupRequestModel = ArrayList()
                    }
                    if(bloodGroupRequestModel == null){
                        bloodGroupRequestModel = ArrayList()
                    }
                    if(it.total_count!=null){
                        totalLimit=it.total_count
                        if(page==1){
                            initBlood()
                        }
                    }

                    if (it.blood!!.any()) {

                        if(page ==1){
                            bloodGroupRequestModel.clear()
                            bloodGroupRequestModel.addAll(it.blood!!)
                            bloodGroupRequestAdapter.notifyDataSetChanged()
                        }else{
                            if(tempPage != page) {
                                bloodGroupRequestModel.addAll(it.blood!!)
                                bloodGroupRequestAdapter.notifyDataSetChanged()
                                tempPage = page
                            }
                        }

                    }
                    if (it.myBloodRequest!!.any()) {
                        myBloodGroupRequestModel.clear()
                        myBloodGroupRequestModel.addAll(it.myBloodRequest!!)
                        myBloodGroupRequestAdapter.notifyDataSetChanged()
                    }
                   /* if (it.blood!!.any()) {
                        bloodGroupRequestModel.clear()
                        bloodGroupRequestModel.addAll(it.blood!!)
                        bloodGroupRequestAdapter.notifyDataSetChanged()
                    }*/
                }
            })

        bloodGroupRequestViewModel.bloodGroupRequestLiveData.observe(this, Observer {
            if (it != null) {
                if (it.blood != null) {

                    bloodGroupRequestViewModel.getBloodGroupRequestList(page.toString(),pageLimit.toString())
                    locationET.setText("")
                    myBloodGroupRequestAdapter.notifyDataSetChanged()
                    bloodGroupAdapter.notifyDataSetChanged()

                }
            }
        })

        bloodGroupRequestViewModel.bloodGroupRequestFilterListLiveData.observe(this, Observer {

                if (it != null) {
                    if (it.blood!!.any()) {
                        bloodGroupRequestModel.clear()
                        bloodGroupRequestModel.addAll(it.blood!!)
                        bloodGroupRequestAdapter.notifyDataSetChanged()
                    }else{
                        showSnackbar("No Request available at the moment.!")
                    }
                }
            })

        bloodGroupRequestViewModel.deleteMyBloodGroupRequestLiveData.observe(this, Observer { if (it != null) { myBloodGroupRequestAdapter.notifyDataSetChanged() } })

        communityViewModel.homePostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                    if (page == 1){
                        sliderView()
                    }
                }
                if (it.posts!!.any()) {
                    if (page == 1) {
                        sliderItemList.clear()
                        getBannerImg(it.posts!!)


                    } else {
                        getBannerImg(it.posts!!)
                    }
                }
            }
        })
    }
    private fun getBannerImg(posts: ArrayList<HomePostModel>) {
        for (isPost in posts){
          sliderItemList.add(isPost)

        }
        sliderAdpter.addSliderList(sliderItemList)

    }

    private fun initBlood() {
        bloodGroupRequestAdapter = BloodGroupRequestAdapter(requireContext(), bloodGroupRequestModel,totalLimit)
        bloodGroupRequestRV.adapter = bloodGroupRequestAdapter
        bloodGroupRequestRV.isNestedScrollingEnabled=true

        bloodGroupRequestAdapter.onViewMoreClicked={
            val limit: Int = totalLimit!! / pageLimit
            if (page <= limit) {
                page += 1
                bloodGroupRequestViewModel.getBloodGroupRequestList(page.toString(),pageLimit.toString())
            }
        }

       /* bloodGroupRequestRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!bloodGroupRequestRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / pageLimit
                    if (page <= limit) {
                        page += 1
                        bloodGroupRequestViewModel.getBloodGroupRequestList(page.toString(),pageLimit.toString())
                    }
                }
            }
        })*/
        bloodGroupRequestAdapter.onCallClicked = { pos ->
            openCallAlertDialog(pos)
        }
        bloodGroupRequestAdapter.copyPasteItemClicked = { decribe ->
            registerForContextMenu(decribe);
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(0, v.getId(),0, "Copy");
        menu.setHeaderTitle("Copy text"); //setting header title for menu
        val textView = v as TextView
        val manager: ClipboardManager? = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData = ClipData.newPlainText("text", textView.text)
        manager!!.setPrimaryClip(clipData);
    }

    override fun onPause() {
        super.onPause()
        requireActivity().viewModelStore.clear()
    }
}