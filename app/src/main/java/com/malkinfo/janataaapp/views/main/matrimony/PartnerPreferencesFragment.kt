package com.malkinfo.janataaapp.views.main.matrimony

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.*
import com.malkinfo.janataaapp.models.base.BaseItem
import com.malkinfo.janataaapp.models.base.StateItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONException
import org.json.JSONObject

class PartnerPreferencesFragment : MyBaseFragment() {
    private var isUpdate: Boolean?= null
    private lateinit var errorMsg: String
    private var ageId: Int? = null
    private lateinit var partnerAgeAdapter: ArrayAdapter<String>
    private lateinit var partnerCountryAdapter: ArrayAdapter<String>
    private var partnerStarId: String? = null
    private var partnerMotherTongueId: String? = null
    private var partnerPhysicalStatus: String? = null
    private var partnerEmploymentType: String? = null
    private var partnerEducationType: String? = null
    private var partnerCountryId: String? = null
    private var partnerMaritalStatus: String? = null
    private var partnerCityId: String? = null
    private var partnerStateID: String? = null
    private lateinit var partnerMotherTongueAdapter: ArrayAdapter<String>
    private lateinit var partnerCityAdapter: ArrayAdapter<String>
    private lateinit var partnerStateAdapter: ArrayAdapter<String>
    private lateinit var partnerStarAdapter: ArrayAdapter<String>
    private lateinit var partnerPhysicalStatusAdapter: ArrayAdapter<String>
    private lateinit var partnerMaritalStateAdapter: ArrayAdapter<String>
    private lateinit var partnerEmploymentStateAdapter: ArrayAdapter<String>
    private lateinit var partnerEducationAdapter: ArrayAdapter<String>
    private var partnerMaritalModel: ArrayList<BaseItem> = ArrayList()
    private var partnerMaritalNameList: ArrayList<String> = ArrayList()
    private var partnerEducationModel: ArrayList<BaseItem> = ArrayList()
    private var partnerEducationNameList: ArrayList<String> = ArrayList()
    private var partnerPhysicalStatusModel: ArrayList<BaseItem> = ArrayList()
    private var partnerPhysicalStatusList: ArrayList<String> = ArrayList()
    private var partnerMotherTongueModel: ArrayList<MotherTongueItem> = ArrayList()
    private var partnerCountryModel: ArrayList<CountryItem> = ArrayList()
    private var partnerStateModel: ArrayList<StateItem> = ArrayList()
    private var partnerAgeModel: ArrayList<BaseItem> = ArrayList()
    private var partnerEmploymentModel: ArrayList<EmploymentItem> = ArrayList()
    private var partnerStarModel: ArrayList<StarItem> = ArrayList()
    private var partnerEmploymentNameList: ArrayList<String> = ArrayList()
    private var partnerMotherTongueNameList: ArrayList<String> = ArrayList()
    private var partnerCountryNameList: ArrayList<String> = ArrayList()
    private var partnerStarNameList: ArrayList<String> = ArrayList()
    private var partnerStateNameList: ArrayList<String> = ArrayList()
    private var partnerAgeList: ArrayList<String> = ArrayList()
    private lateinit var partnerPreferencesInterface: CommunityStore
    private lateinit var listener: CommunityStore

    /**set ID*/
    private lateinit var partnerMotherTongueSP:Spinner
    private lateinit var partnerCountryLivingInSP:Spinner
    private lateinit var partnerEmployedInSP:Spinner
    private lateinit var partnerStarSP:Spinner
    private lateinit var partnerStateLivingInSP:Spinner
    private lateinit var saveDetailsBT: MaterialButton
    private lateinit var partnerAgeSP:Spinner
    private lateinit var partnerMaritalStatusSP:Spinner
    private lateinit var partnerPhysicalStatusSP:Spinner
    private lateinit var partnerEducationSP:Spinner
    private lateinit var partnerAnnualIncomeED: TextInputEditText
    private lateinit var partnerHeightED:TextInputEditText
    private lateinit var partnerStateLivingInHintTV:TextView
    private lateinit var partnerBackIVs:ImageView

    companion object {

        @JvmStatic
        fun newInstance(update: Boolean) =
            PartnerPreferencesFragment().apply {
                arguments = Bundle().apply {
                   putBoolean("isUpdate",update)
                }
            }
    }

    private val partnerPreferecneViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if(arguments!=null){
                isUpdate=it.getBoolean("isUpdate")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partner_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsIDs(view)
        setUpLoader(partnerPreferecneViewModel)
        initViews(view)
    }

    private fun initsIDs(v:View){
        partnerMotherTongueSP = v.findViewById(R.id.partnerMotherTongueSP)
        partnerCountryLivingInSP = v.findViewById(R.id.partnerCountryLivingInSP)
        partnerEmployedInSP = v.findViewById(R.id.partnerEmployedInSP)
        partnerStarSP = v.findViewById(R.id.partnerStarSP)
        partnerStateLivingInSP = v.findViewById(R.id.partnerStateLivingInSP)
        saveDetailsBT = v.findViewById(R.id.saveDetailsBT)
        partnerAgeSP = v.findViewById(R.id.partnerAgeSP)
        partnerMaritalStatusSP = v.findViewById(R.id.partnerMaritalStatusSP)
        partnerPhysicalStatusSP = v.findViewById(R.id.partnerPhysicalStatusSP)
        partnerEducationSP = v.findViewById(R.id.partnerEducationSP)
        partnerAnnualIncomeED = v.findViewById(R.id.partnerAnnualIncomeED)
        partnerHeightED = v.findViewById(R.id.partnerHeightED)
        partnerStateLivingInHintTV = v.findViewById(R.id.partnerStateLivingInHintTV)
        partnerBackIVs = v.findViewById(R.id.partnerBackIVs)

    }
    override fun onErrorCalled(it: String?) {
        it?.let {
            showSnackbar(it)
        }

    }

    override fun initObservers() {
        partnerPreferecneViewModel.partnerPreferenceDetailsLiveData.observe(this,
            Observer {
                if (it != null) {
                    if (it.userDetails != null) {
                        if (it.userDetails!!.mothertongues!!.any()) {
                            partnerMotherTongueModel.clear()
                            partnerMotherTongueNameList.clear()
                            partnerMotherTongueModel.add(
                                MotherTongueItem(
                                    "0",
                                    getString(R.string.select_mother_tongue)
                                )
                            )
                            partnerMotherTongueModel.addAll(it.userDetails!!.mothertongues!!)
                            for (i in 0 until partnerMotherTongueModel.size) {
                                partnerMotherTongueNameList.add(partnerMotherTongueModel[i].mother_tongue!!)
                            }

                            if (sharedPrefManager
                                    .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
                            ) {
                                for (i in 0 until partnerMotherTongueModel.size) {
                                    if (MyCommunityApp.getPartnerPreference(requireActivity())!!.mother_tongue.equals(
                                            partnerMotherTongueModel[i]._id
                                        )
                                    )
                                        partnerMotherTongueSP.setSelection(i)
                                }

                            }
                            partnerMotherTongueAdapter.notifyDataSetChanged()
                        }

                        if (it.userDetails!!.country!!.any()) {
                            partnerCountryModel.clear()
                            partnerCountryNameList.clear()
                            partnerCountryModel.add(
                                CountryItem(
                                    "0",
                                    getString(R.string.select_country)
                                )
                            )
                            partnerCountryModel.addAll(it.userDetails!!.country!!)
                            for (i in 0 until partnerCountryModel.size) {
                                partnerCountryNameList.add(partnerCountryModel[i].country!!)
                            }

                            if (sharedPrefManager
                                    .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
                            ) {
                                for (i in 0 until partnerCountryModel.size) {
                                    if (MyCommunityApp.getPartnerPreference(requireActivity())!!.country_livingin.equals(
                                            partnerCountryModel[i]._id
                                        )
                                    )
                                        partnerCountryLivingInSP.setSelection(i)
                                }
                            }
                            partnerCountryAdapter.notifyDataSetChanged()
                        }

                        if (it.userDetails!!.employed_id!!.any()) {
                            partnerEmploymentModel.clear()
                            partnerEmploymentNameList.clear()
                            partnerEmploymentModel.add(
                                EmploymentItem(
                                    "0",
                                    getString(R.string.select_employment_status)
                                )
                            )
                            partnerEmploymentModel.addAll(it.userDetails!!.employed_id!!)
                            for (i in 0 until partnerEmploymentModel.size) {
                                partnerEmploymentNameList.add(partnerEmploymentModel[i].employed_in!!)
                            }

                            if (sharedPrefManager
                                    .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
                            ) {

                                for (i in 0 until partnerEmploymentModel.size) {
                                    if (MyCommunityApp.getPartnerPreference(requireActivity())!!.employed_in.equals(
                                            partnerEmploymentModel[i]._id
                                        )
                                    )
                                        partnerEmployedInSP.setSelection(i)

                                }
                            }
                            partnerEmploymentStateAdapter.notifyDataSetChanged()

                        }


                        if (it.userDetails!!.star!!.any()) {
                            partnerStarModel.clear()
                            partnerStarNameList.clear()
                            partnerStarModel.add(StarItem("0", getString(R.string.select_star)))
                            partnerStarModel.addAll(it.userDetails!!.star!!)
                            for (i in 0 until partnerStarModel.size) {
                                partnerStarNameList.add(partnerStarModel[i].star!!)
                            }

                            if (sharedPrefManager
                                    .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
                            ) {
                                for (i in 0 until partnerStarModel.size) {
                                    if (MyCommunityApp.getPartnerPreference(requireActivity())!!.star.equals(
                                            partnerStarModel[i]._id
                                        )
                                    )
                                        partnerStarSP.setSelection(i)
                                    partnerStarAdapter.notifyDataSetChanged()
                                }
                            }
                            partnerStarAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })


        partnerPreferecneViewModel.partnerStateLiveData.observe(this, Observer {
            if (it.state!!.any()) {
                partnerStateModel.clear()
                partnerStateNameList.clear()
                partnerStateModel.add(StateItem("0", getString(R.string.select_state)))
                partnerStateModel.addAll(it.state!!)
                for (i in 0 until partnerStateModel.size) {
                    partnerStateNameList.add(partnerStateModel[i].state!!)
                }
                if (sharedPrefManager
                        .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
                ) {

                    for (i in 0 until partnerStateModel.size) {
                        if (MyCommunityApp.getPartnerPreference(requireActivity())!!.state_livingin.equals(
                                partnerStateModel[i]._id
                            )
                        )
                            partnerStateLivingInSP.setSelection(i)
                        partnerStateAdapter.notifyDataSetChanged()
                    }
                }
                partnerStateAdapter.notifyDataSetChanged()
            }

        })

        partnerPreferecneViewModel.partnerPreferenceLiveData.observe(this, Observer {
            if (it != null) {

                /*if(isUpdate!!){
                 //   requireActivity().onBackPressed()

                }else {*/
                    partnerPreferencesInterface.onSaveDetailsClicked()
                    requireActivity().viewModelStore.clear()
               // }
                showSnackbar(it.message!!)
            }
        })

        partnerPreferecneViewModel.updatePartnerPreferenceLiveData.observe(this, Observer {
            if(it!=null){
                listener.onBack()
                requireActivity().viewModelStore.clear()
               // showSnackbar(it.message!!)
            }
        })
    }

    private fun initViews(view: View) {

        partnerPreferecneViewModel.getPartnerPreferenceDetails()

        addAge()
        addEducation()
        addMaritalStatus()
        addPhysicalStatus()

        if(isUpdate!!){
            saveDetailsBT.text=getString(R.string.update)
        }else{

            saveDetailsBT.text=getString(R.string.save_amp_continue)

        }


        partnerAgeAdapter = ArrayAdapter(view.context, R.layout.spinner_item, partnerAgeList)
        partnerAgeAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerAgeSP.adapter = partnerAgeAdapter

        partnerMaritalStateAdapter =
            ArrayAdapter(view.context, R.layout.spinner_item, partnerMaritalNameList)
        partnerMaritalStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerMaritalStatusSP.adapter = partnerMaritalStateAdapter

        partnerPhysicalStatusAdapter =
            ArrayAdapter(view.context, R.layout.spinner_item, partnerPhysicalStatusList)
        partnerPhysicalStatusAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerPhysicalStatusSP.adapter = partnerPhysicalStatusAdapter

        partnerMotherTongueAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, partnerMotherTongueNameList)
        partnerMotherTongueAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerMotherTongueSP.adapter = partnerMotherTongueAdapter

        partnerStarAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, partnerStarNameList)
        partnerStarAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerStarSP.adapter = partnerStarAdapter

        partnerCountryAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, partnerCountryNameList)
        partnerCountryAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerCountryLivingInSP.adapter = partnerCountryAdapter

        partnerStateAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, partnerStateNameList)
        partnerStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerStateLivingInSP.adapter = partnerStateAdapter


        partnerEducationAdapter =
            ArrayAdapter(view.context, R.layout.spinner_item, partnerEducationNameList)
        partnerEducationAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerEducationSP.adapter = partnerEducationAdapter


        partnerEmploymentStateAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, partnerEmploymentNameList)
        partnerEmploymentStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        partnerEmployedInSP.adapter = partnerEmploymentStateAdapter

        if (sharedPrefManager
                .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)
        ) {

            partnerAnnualIncomeED.setText(MyCommunityApp.getPartnerPreference(requireActivity())!!.annual_income.toString())
            partnerHeightED.setText(MyCommunityApp.getPartnerPreference(requireActivity())!!.height.toString())




            when (MyCommunityApp.getPartnerPreference(requireActivity())!!.age!![0]) {
                "18" -> {
                    partnerAgeSP.setSelection(1)
                }
                "23" -> {
                    partnerAgeSP.setSelection(2)
                }
                "28" -> {
                    partnerAgeSP.setSelection(3)
                }
                "33" -> {
                    partnerAgeSP.setSelection(4)
                }
                "38" -> {
                    partnerAgeSP.setSelection(5)
                }
                "43" -> {
                    partnerAgeSP.setSelection(6)
                }
                "48" -> {
                    partnerAgeSP.setSelection(7)
                }
            }

            for (i in 0 until partnerMaritalModel.size) {
                if (MyCommunityApp.getPartnerPreference(requireActivity())!!.marital_status.equals(
                        partnerMaritalModel[i].name
                    )
                ) {
                    partnerMaritalStatusSP.setSelection(i)
                }
            }

            for (i in 0 until partnerPhysicalStatusModel.size) {
                if (MyCommunityApp.getPartnerPreference(requireActivity())!!.physical_status.equals(
                        partnerPhysicalStatusModel[i].name
                    )
                )
                    partnerPhysicalStatusSP.setSelection(i)
            }


            for (i in 0 until partnerEducationModel.size) {
                if (MyCommunityApp.getPartnerPreference(requireActivity())!!.education.equals(
                        partnerEducationModel[i].name
                    )
                )
                    partnerEducationSP.setSelection(i)
            }


        }

        partnerAgeSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                when (partnerAgeSP.selectedItem) {
                    "18 to 22" -> {
                        ageId = 0
                    }
                    "23 to 27" -> {
                        ageId = 1
                    }
                    "28 to 32" -> {
                        ageId = 2
                    }
                    "33 to 37" -> {
                        ageId = 3
                    }
                    "38 to 42" -> {
                        ageId = 4
                    }
                    "43 to 47" -> {
                        ageId = 5
                    }
                    "48 to 52" -> {
                        ageId = 6
                    }
                }
            }

        }

        partnerCountryLivingInSP.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                    partnerCountryId = partnerCountryModel[pos]._id

                    if (partnerCountryLivingInSP.selectedItem.equals("India")) {
                        partnerStateLivingInSP.visibility = VISIBLE
                        partnerStateLivingInHintTV.visibility = VISIBLE
                        partnerPreferecneViewModel.getPartnerStateList(partnerCountryId!!)
                    } else {
                        partnerStateLivingInSP.visibility = GONE
                        partnerStateLivingInHintTV.visibility = GONE
                    }

                }

            }
        partnerStateLivingInSP.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    partnerStateID = partnerStateModel[pos]._id
                }

            }



        partnerMotherTongueSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                partnerMotherTongueId = partnerMotherTongueModel[pos]._id
            }

        }

        partnerMaritalStatusSP.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    partnerMaritalStatus = partnerMaritalModel[pos].name
                }

            }

        partnerEducationSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                partnerEducationType = partnerEducationModel[pos].name
            }

        }

        partnerEmployedInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                partnerEmploymentType = partnerEmploymentModel[pos]._id
            }

        }

        partnerPhysicalStatusSP.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    partnerPhysicalStatus = partnerPhysicalStatusModel[pos].name
                }

            }

        partnerStarSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                partnerStarId = partnerStarModel[pos]._id
            }

        }

        saveDetailsBT.setOnClickListener {

            if (validation()) {
                val main = JSONObject()
                try {

                    main.put("age", ageId)
                    main.put("height", partnerHeightED.text.toString().toInt())
                    main.put("marital_status", partnerMaritalStatus)
                    main.put("physical_status", partnerPhysicalStatus)
                    main.put("mother_tongue", partnerMotherTongueId)
                    main.put("star", partnerStarId)
                    main.put("country_livingin", partnerCountryId)
                    main.put("state_livingin", partnerStateID)
                    main.put("education", partnerEducationType)
                    main.put("employed_in", partnerEmploymentType)
                    main.put("annual_income", partnerAnnualIncomeED.text.toString().toInt())
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject

                    if(isUpdate!!){
                        partnerPreferecneViewModel.doUpdatePartnerPreference(MyCommunityApp.getPartnerPreference(requireActivity())!!._id!!,gsonObject)
                    }else{
                        partnerPreferecneViewModel.doPartnerPreference(gsonObject)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                showSnackbar(errorMsg)
            }

        }


        partnerBackIVs.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun validation(): Boolean {
        var formOk = true

        if (partnerAgeSP.selectedItem == getString(R.string.select_age)) {
            formOk = false
            errorMsg = getString(R.string.select_age)
        } else if (partnerMaritalStatusSP.selectedItem == getString(R.string.select_marital_status)) {
            formOk = false
            errorMsg = getString(R.string.select_marital_status)
        } else if (partnerPhysicalStatusSP.selectedItem == getString(R.string.select_physical_status)) {
            formOk = false
            errorMsg = getString(R.string.select_physical_status)
        } else if (partnerMotherTongueSP.selectedItem == getString(R.string.select_mother_tongue)) {
            formOk = false
            errorMsg = getString(R.string.select_mother_tongue)
        } else if (partnerStarSP.selectedItem == getString(R.string.select_star)) {
            formOk = false
            errorMsg = getString(R.string.select_star)
        } else if (partnerCountryLivingInSP.selectedItem == getString(R.string.select_country)) {
            formOk = false
            errorMsg = getString(R.string.select_country)
        } else if (partnerStateLivingInSP.visibility == VISIBLE && partnerStateLivingInSP.selectedItem == getString(
                R.string.select_state
            )
        ) {
            formOk = false
            errorMsg = getString(R.string.select_state)
        } else if (partnerEducationSP.selectedItem == getString(R.string.select_education)) {
            formOk = false
            errorMsg = getString(R.string.select_education)
        } else if (partnerEmployedInSP.selectedItem == getString(R.string.select_employment_status)) {
            formOk = false
            errorMsg = getString(R.string.select_employment_status)
        } else if (!FormValidator.requiredField(partnerAnnualIncomeED.text.toString(), 4)) {
            formOk = false
            partnerAnnualIncomeED.error = getString(R.string.enter_valid_annual_income)
        } else if (!FormValidator.requiredField(partnerHeightED.text.toString(), 3)) {
            formOk = false
            partnerHeightED.error = getString(R.string.enter_valid_height)
        }
        return formOk
    }

    private fun addAge() {
        partnerAgeModel.clear()
        partnerAgeModel.add(BaseItem(getString(R.string.select_age)))
        partnerAgeModel.add(BaseItem("18 to 22"))
        partnerAgeModel.add(BaseItem("23 to 27"))
        partnerAgeModel.add(BaseItem("28 to 32"))
        partnerAgeModel.add(BaseItem("33 to 37"))
        partnerAgeModel.add(BaseItem("38 to 42"))
        partnerAgeModel.add(BaseItem("43 to 47"))
        partnerAgeModel.add(BaseItem("48 to 52"))
        partnerAgeList.clear()
        for (i in 0 until partnerAgeModel.size) {
            partnerAgeList.add(partnerAgeModel[i].name!!)
        }
    }

    private fun addPhysicalStatus() {
        partnerPhysicalStatusModel.clear()
        partnerPhysicalStatusModel.add(BaseItem("Select Physical Status"))
        partnerPhysicalStatusModel.add(BaseItem("Physically Challenge"))
        partnerPhysicalStatusModel.add(BaseItem("Normal"))
        partnerPhysicalStatusList.clear()
        for (i in 0 until partnerPhysicalStatusModel.size) {
            partnerPhysicalStatusList.add(partnerPhysicalStatusModel[i].name!!)
        }
    }

    private fun addEducation() {
        partnerEducationModel.clear()
        partnerEducationModel.add(BaseItem("Select Education"))
        partnerEducationModel.add(BaseItem("No Education"))
        partnerEducationModel.add(BaseItem("SSLC"))
        partnerEducationModel.add(BaseItem("Intermediate"))
        partnerEducationModel.add(BaseItem("UG"))
        partnerEducationModel.add(BaseItem("PG"))
        partnerEducationNameList.clear()
        for (i in 0 until partnerEducationModel.size) {
            partnerEducationNameList.add(partnerEducationModel[i].name!!)
        }
    }


    private fun addMaritalStatus() {
        partnerMaritalModel.clear()
        partnerMaritalModel.add(BaseItem("Select Marital Status"))
        partnerMaritalModel.add(BaseItem("Married"))
        partnerMaritalModel.add(BaseItem("UnMarried"))
        partnerMaritalModel.add(BaseItem("Widow"))
        partnerMaritalNameList.clear()
        for (i in 0 until partnerMaritalModel.size) {
            partnerMaritalNameList.add(partnerMaritalModel[i].name!!)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CommunityStore) {

            partnerPreferencesInterface = context as CommunityStore
        }
         if(context is CommunityStore){
             listener = context as CommunityStore
         }
    }


}