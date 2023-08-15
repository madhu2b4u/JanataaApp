package com.malkinfo.janataaapp.views.main.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONException
import org.json.JSONObject


class ReportFragment : MyBaseFragment() {

    private lateinit var communityStore:CommunityStore
    private var reason: String? = null
    private lateinit var errorMsg: String
    private var reportComment: String? = null
    private var groupPostId: String? = null
    private var profileId: String? = null
    private var reasonModel: ArrayList<String> = ArrayList()

    /**set Id*/
    private lateinit var backIV:ImageView
    private lateinit var reportReasonSP:Spinner
    private lateinit var reportCL:ConstraintLayout
    private lateinit var reportED: EditText

    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    private val reportProfileViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(profileId: String?,groupId:String?) =
            ReportFragment().apply {
                arguments = Bundle().apply {

                    putString(MyCommunityAppConstants.GROUP_POST_ID, groupId)
                    putString(MyCommunityAppConstants.PROFILE_ID, profileId)

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                groupPostId = it.getString(MyCommunityAppConstants.GROUP_POST_ID)
                profileId = it.getString(MyCommunityAppConstants.PROFILE_ID)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsID(view)
        setUpLoader(communityViewModel)
        initViews(view)
    }
    private fun initsID(v:View){
        backIV = v.findViewById(R.id.backIV)
        reportReasonSP = v.findViewById(R.id.reportReasonSP)
        reportCL = v.findViewById(R.id.reportCL)
        reportED = v.findViewById(R.id.reportED)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            communityStore = context as CommunityStore
        }
    }


    override fun initObservers() {
        communityViewModel.reportPostLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar("Reported Successfully")

                communityStore.onBack()
                requireActivity().viewModelStore.clear()
            }
        })

        reportProfileViewModel.reportProfileLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar("Reported Successfully")

                communityStore.onBack()
                requireActivity().viewModelStore.clear()
            }
        })
    }

    private fun initViews(view: View) {
        backIV.setOnClickListener {
            requireActivity().onBackPressed()
        }

        addReportReasons()
        val reasonAdapter = ArrayAdapter(view.context, R.layout.spinner_item, reasonModel)
        reasonAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        reportReasonSP.adapter = reasonAdapter

        reportReasonSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                reason = reasonModel[pos]
            }

        }

        reportCL.setOnClickListener {

            if (validation()) {
                if(profileId!= null && profileId!!.isNotEmpty()){

                    //Do report profile
                    val main = JSONObject()
                    try {
                        reportComment = reportED.text.toString()
                        main.put("report_user", profileId)
                        main.put("reason", reason)
                        main.put("comment", reportComment)
                        val jsonParser = JsonParser()
                        val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                        reportProfileViewModel.doReportMatrimonyProfile(gsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }else if (groupPostId != null && groupPostId!!.isNotEmpty()) {
                    //Do report post
                    val main = JSONObject()
                    try {
                        reportComment = reportED.text.toString()
                        main.put("reason", reason)
                        main.put("comment", reportComment)
                        val jsonParser = JsonParser()
                        val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                        communityViewModel.doReportPost(groupPostId!!, gsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } else {
                showSnackbar(errorMsg)
            }

        }
    }

    private fun validation(): Boolean {
        var formOk = true

        if (reportReasonSP.selectedItem.equals("Select Reason")) {
            formOk = false
            errorMsg = getString(R.string.select_reason)
        } else if (!FormValidator.requiredField(reportED.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_comment)
        }
        return formOk
    }

    private fun addReportReasons() {
        reasonModel.clear()
        reasonModel.add("Select Reason")
        reasonModel.add("Spam")
        reasonModel.add("Harassment")
        reasonModel.add("violence")
        reasonModel.add("nudity")
        reasonModel.add("hate speech")
    }

}