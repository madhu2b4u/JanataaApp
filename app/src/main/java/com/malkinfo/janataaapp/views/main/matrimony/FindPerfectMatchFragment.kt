package com.malkinfo.janataaapp.views.main.matrimony

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.MediaAdapter
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.*
import com.malkinfo.janataaapp.models.base.BaseItem
import com.malkinfo.janataaapp.models.base.FileUploadItem
import com.malkinfo.janataaapp.models.base.StateItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class FindPerfectMatchFragment : MyBaseFragment() {

    private var uriArray: ArrayList<Uri> = ArrayList()
    private var moonSign: String? = null
    private var imageUrl: String? = null
    var imageBitmap: Bitmap? = null
    var file: File? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    protected val REQUEST_CODE_GALLERY = 2
    private val STORAGE_CAMERA_PERMISSION_CODE = 34
    private  var errorMsg: String = "Some Error"
    private lateinit var familyType: String
    private var star: String? = null
    private lateinit var countryAdapter: ArrayAdapter<String>
    private var motherTongue: String? = null
    private var bodyType: String? = null
    private var physicalStatus: String? = null
    private var employmentType: String? = null
    private var educationType: String? = null
    private var countryId: String? = null
    private var maritalStatus: String? = null
    private var cityId: String? = null
    private var stateID: String? = null
    private lateinit var motherTongueAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var starAdapter: ArrayAdapter<String>
    private lateinit var zodiacSignAdapter: ArrayAdapter<String>
    private lateinit var bodyTypeAdapter: ArrayAdapter<String>
    private lateinit var physicalStatusAdapter: ArrayAdapter<String>
    private lateinit var maritalStateAdapter: ArrayAdapter<String>
    private lateinit var employmentAdapter: ArrayAdapter<String>
    private lateinit var educationAdapter: ArrayAdapter<String>
    private lateinit var findPerfectMatchInterface: CommunityStore
    private var maritalModel: ArrayList<BaseItem> = ArrayList()
    private var maritalNameList: ArrayList<String> = ArrayList()
    private var educationModel: ArrayList<BaseItem> = ArrayList()
    private var educationNameList: ArrayList<String> = ArrayList()
    private var physicalStatusModel: ArrayList<BaseItem> = ArrayList()
    private var physicalStatusList: ArrayList<String> = ArrayList()
    private var bodyTypeModel: ArrayList<BaseItem> = ArrayList()
    private var bodyTypeList: ArrayList<String> = ArrayList()
    private var motherTongueModel: ArrayList<MotherTongueItem> = ArrayList()
    private var countryModel: ArrayList<CountryItem> = ArrayList()
    private var stateModel: ArrayList<StateItem> = ArrayList()
    private var cityModel: ArrayList<CityItem> = ArrayList()
    private var employmentModel: ArrayList<EmploymentItem> = ArrayList()
    private var starModel: ArrayList<StarItem> = ArrayList()
    private var zodiacModel: ArrayList<MoonItem> = ArrayList()
    private var employmentNameList: ArrayList<String> = ArrayList()
    private var motherTongueNameList: ArrayList<String> = ArrayList()
    private var countryNameList: ArrayList<String> = ArrayList()
    private var starNameList: ArrayList<String> = ArrayList()
    private var zodiacSignNameList: ArrayList<String> = ArrayList()
    private var stateNameList: ArrayList<String> = ArrayList()
    private var cityNameList: ArrayList<String> = ArrayList()
    private var profileImageModel: ArrayList<FileUploadItem> = ArrayList()
    private var profileImageUrlList: ArrayList<String> = ArrayList()
    private lateinit var profileImageAdapter: MediaAdapter
    /**set ID*/
    private lateinit var jointFamilyRB:RadioButton
    private lateinit var profileImageRV:RecyclerView
    private lateinit var zodiacSignSP:Spinner
    private lateinit var starSP:Spinner
    private lateinit var maritalStatusSP:Spinner
    private lateinit var educationSP:Spinner
    private lateinit var employedInSP:Spinner
    private lateinit var countryLivingInSP:Spinner
    private lateinit var stateLivingInSP:Spinner
    private lateinit var cityLivingInSP:Spinner
    private lateinit var motherTongueSP:Spinner
    private lateinit var physicalStatusSP:Spinner
    private lateinit var bodyTypeSP:Spinner
    private lateinit var stateLivingInHintTV:TextView
    private lateinit var nuclearFamilyRB:RadioButton
    private lateinit var cityLivingInHintTV:TextView
    private lateinit var openGalleryTV:TextView
    private lateinit var findPerfectMatchBT: MaterialButton
    private lateinit var heightED: TextInputEditText
    private lateinit var ageED:TextInputEditText
    private lateinit var weightED:TextInputEditText
    private lateinit var annualIncomeED:TextInputEditText
    private lateinit var familyStatusED:TextInputEditText
    private lateinit var ancestralOriginED:TextInputEditText
    private lateinit var assetsED:TextInputEditText
    private lateinit var fatherOccupationED:TextInputEditText
    private lateinit var motherOccupationED:TextInputEditText
    private lateinit var parentsContactNumberED:TextInputEditText
    private lateinit var aboutMyselfED:EditText
    private lateinit var chooseMediaTagTV:TextView

    companion object {
        @JvmStatic
        fun newInstance() =
            FindPerfectMatchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private val matrimonyViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_find_perfect_match, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            findPerfectMatchInterface = context as CommunityStore
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIds(view)
        setUpLoader(matrimonyViewModel)
        initViews(view)
    }
    private fun initIds(v:View){
        jointFamilyRB = v.findViewById(R.id.jointFamilyRB)
        profileImageRV = v.findViewById(R.id.profileImageRV)
        zodiacSignSP = v.findViewById(R.id.zodiacSignSP)
        starSP = v.findViewById(R.id.starSP)
        maritalStatusSP = v.findViewById(R.id.maritalStatusSP)
        educationSP = v.findViewById(R.id.educationSP)
        employedInSP = v.findViewById(R.id.employedInSP)
        countryLivingInSP = v.findViewById(R.id.countryLivingInSP)
        stateLivingInSP = v.findViewById(R.id.stateLivingInSP)
        cityLivingInSP = v.findViewById(R.id.cityLivingInSP)
        motherTongueSP = v.findViewById(R.id.motherTongueSP)
        physicalStatusSP = v.findViewById(R.id.physicalStatusSP)
        bodyTypeSP = v.findViewById(R.id.bodyTypeSP)
        stateLivingInHintTV = v.findViewById(R.id.stateLivingInHintTV)
        nuclearFamilyRB = v.findViewById(R.id.nuclearFamilyRB)
        cityLivingInHintTV = v.findViewById(R.id.cityLivingInHintTV)
        openGalleryTV = v.findViewById(R.id.openGalleryTV)
        findPerfectMatchBT = v.findViewById(R.id.findPerfectMatchBT)
        heightED = v.findViewById(R.id.heightED)
        ageED = v.findViewById(R.id.ageED)
        weightED = v.findViewById(R.id.weightED)
        annualIncomeED = v.findViewById(R.id.annualIncomeED)
        familyStatusED =v.findViewById(R.id.familyStatusED)
        ancestralOriginED = v.findViewById(R.id.ancestralOriginED)
        assetsED = v.findViewById(R.id.assetsED)
        fatherOccupationED = v.findViewById(R.id.fatherOccupationED)
        motherOccupationED = v.findViewById(R.id.motherOccupationED)
        parentsContactNumberED = v.findViewById(R.id.parentsContactNumberED)
        aboutMyselfED = v.findViewById(R.id.aboutMyselfED)
        chooseMediaTagTV = v.findViewById(R.id.chooseMediaTagTV)
    }


    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    private fun initViews(view: View) {
        matrimonyViewModel.getMatrimonyRegisterDetails()

        addBodyType()
        addPhysicalStatus()
        addMaritalStatus()
        addEmploymentStatus()
        addEducation()

        familyType = jointFamilyRB.text.toString()

        profileImageAdapter = MediaAdapter(view.context, profileImageModel)
        profileImageRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        profileImageRV.adapter = profileImageAdapter
        profileImageAdapter.notifyDataSetChanged()

        profileImageAdapter.onAddMoreMediaClicked = {
            requestPermission()
        }

        profileImageAdapter.onDeleteMediaClicked = { pos ->
            profileImageUrlList.removeAt(pos)
            profileImageModel.removeAt(pos)
            profileImageAdapter.notifyItemRemoved(pos)
            profileImageAdapter.notifyDataSetChanged()
        }

        zodiacSignAdapter = ArrayAdapter(view.context, R.layout.spinner_item, zodiacSignNameList)
        zodiacSignAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        zodiacSignSP.adapter = zodiacSignAdapter

        starAdapter = ArrayAdapter(view.context, R.layout.spinner_item, starNameList)
        starAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        starSP.adapter = starAdapter


        maritalStateAdapter = ArrayAdapter(view.context, R.layout.spinner_item, maritalNameList)
        maritalStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        maritalStatusSP.adapter = maritalStateAdapter

        educationAdapter = ArrayAdapter(view.context, R.layout.spinner_item, educationNameList)
        educationAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        educationSP.adapter = educationAdapter

        employmentAdapter = ArrayAdapter(view.context, R.layout.spinner_item, employmentNameList)
        employmentAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        employedInSP.adapter = employmentAdapter

        countryAdapter = ArrayAdapter(view.context, R.layout.spinner_item, countryNameList)
        countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        countryLivingInSP.adapter = countryAdapter

        stateAdapter = ArrayAdapter(view.context, R.layout.spinner_item, stateNameList)
        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        stateLivingInSP.adapter = stateAdapter

        cityAdapter = ArrayAdapter(view.context, R.layout.spinner_item, cityNameList)
        cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        cityLivingInSP.adapter = cityAdapter

        motherTongueAdapter = ArrayAdapter(view.context, R.layout.spinner_item, motherTongueNameList)
        motherTongueAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        motherTongueSP.adapter = motherTongueAdapter

        physicalStatusAdapter = ArrayAdapter(view.context, R.layout.spinner_item, physicalStatusList)
        physicalStatusAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        physicalStatusSP.adapter = physicalStatusAdapter

        bodyTypeAdapter = ArrayAdapter(view.context, R.layout.spinner_item, bodyTypeList)
        bodyTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        bodyTypeSP.adapter = bodyTypeAdapter

        zodiacSignSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                moonSign = zodiacModel[pos]._id
            }

        }

        starSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                star = starModel[pos]._id
            }

        }

        maritalStatusSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                maritalStatus = maritalModel[pos].name
            }

        }

        educationSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                educationType = educationModel[pos].name


            }

        }

        employedInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                employmentType = employmentModel[pos]._id
            }

        }

        countryLivingInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                countryId = countryModel[pos]._id

                if (countryLivingInSP.selectedItem.equals("India")) {
                    stateLivingInSP.visibility = VISIBLE
                    stateLivingInHintTV.visibility = VISIBLE
                    cityLivingInSP.visibility = VISIBLE
                    cityLivingInHintTV.visibility = VISIBLE
                    matrimonyViewModel.getStateList(countryId!!)
                } else {
                    stateLivingInSP.visibility = GONE
                    stateLivingInHintTV.visibility = GONE
                    cityLivingInSP.visibility = GONE
                    cityLivingInHintTV.visibility = GONE
                }


            }

        }

        stateLivingInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {


                if (stateLivingInSP.selectedItem.equals(getString(R.string.select_state))) {
                    cityLivingInSP.isClickable = false
                } else {
                    cityLivingInSP.isClickable = true
                    stateID = stateModel[pos]._id
                    matrimonyViewModel.getCity(stateID!!)
                }

            }

        }

        cityLivingInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                cityId = cityModel[pos]._id
            }

        }

        motherTongueSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                motherTongue = motherTongueModel[pos]._id
            }

        }

        physicalStatusSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                physicalStatus = physicalStatusModel[pos].name
            }

        }

        bodyTypeSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                bodyType = bodyTypeModel[pos].name
            }

        }

        jointFamilyRB.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isJointChecked: Boolean) {
                if (isJointChecked) {

                    familyType = jointFamilyRB.text.toString()

                    if (nuclearFamilyRB.isChecked) {
                        nuclearFamilyRB.setChecked(false)
                    }

                } else {
                    if (isJointChecked) {
                        jointFamilyRB.setChecked(false)
                    }
                }
            }

        })

        nuclearFamilyRB.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isNuclearChecked: Boolean) {
                if (isNuclearChecked) {
                    familyType = nuclearFamilyRB.text.toString()
                    if (jointFamilyRB.isChecked) {
                        jointFamilyRB.setChecked(false)
                    }
                } else {
                    if (isNuclearChecked) {
                        nuclearFamilyRB.setChecked(false)
                    }
                }
            }

        })

        openGalleryTV.setOnClickListener {
            requestPermission()
        }

        findPerfectMatchBT.setOnClickListener {
            Log.d("mTag","validation =  ${validation()}")

            if (validation()) {
                //Do Matrimonys Register
                val matimonyDetails = JSONObject()
                val main = JSONObject()
                val array = JSONArray()
                for (i in profileImageUrlList) {
                    array.put(i)
                }
                val height = heightED.text.toString().toInt()
                val age = ageED.text.toString().toInt()
                val weight = weightED.text.toString().toInt()
                val annualIncome = annualIncomeED.text.toString().toInt()
                val familyStatus = familyStatusED.text.toString()
                val ancestralOrigin = ancestralOriginED.text.toString()
                val assets = assetsED.text.toString()
                val fatherOccupation = fatherOccupationED.text.toString()
                val motherOccupation = motherOccupationED.text.toString()
                val parentContactNumber = parentsContactNumberED.text.toString()
                val aboutMyself = aboutMyselfED.text.toString()
                try {

                    main.put("star", star)
                    main.put("moon", moonSign)
                    main.put("mother_tongue", motherTongue)
                    main.put("marital_status", maritalStatus)
                    main.put("country_living_in", countryId)
                    main.put("state_living_in", stateID)
                    main.put("city_living_in", cityId)
                    main.put("height", height)
                    main.put("physical_status", physicalStatus)
                    main.put("body_type", bodyType)
                    main.put("age", age)
                    main.put("weight", weight)
                    main.put("education", educationType)
                    main.put("employed_in", employmentType)
                    main.put("annual_income", annualIncome)
                    main.put("family_status", familyStatus)
                    main.put("family_type", familyType)
                    main.put("family_values", employmentType)
                    main.put("ancestral_origin", ancestralOrigin)
                    main.put("assets", assets)
                    main.put("fathers_occupation", fatherOccupation)
                    main.put("mother_occupation", motherOccupation)
                    main.put("parent_number", parentContactNumber)
                    main.put("about_myself", aboutMyself)
                    main.put("profile_url", array)

                    matimonyDetails.put("matrimony_registeration", main)
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(matimonyDetails.toString()) as JsonObject
                    matrimonyViewModel.doMatrimonyRegistration(gsonObject)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                showSnackbar(errorMsg)
            }

        }

    }

    private fun validation(): Boolean {
        var formOk = true
        if (zodiacSignSP.selectedItem == null || zodiacSignSP.selectedItem.equals(getString(R.string.select_zodiac_sign))) {
            formOk = false
            errorMsg = getString(R.string.select_zodiac_sign)
        } else if (starSP.selectedItem == null || starSP.selectedItem.equals(getString(R.string.select_star))) {
            formOk = false
            errorMsg = getString(R.string.select_star)
        } else if (maritalStatusSP.selectedItem == null || maritalStatusSP.selectedItem.equals(getString(R.string.select_marital_status))) {
            formOk = false
            errorMsg = getString(R.string.select_marital_status)
        } else if (educationSP.selectedItem == null || educationSP.selectedItem.equals(getString(R.string.select_education))) {
            formOk = false
            errorMsg = getString(R.string.select_education)
        } else if (employedInSP.selectedItem == null || employedInSP.selectedItem.equals(getString(R.string.select_employment_status))) {
            formOk = false
            errorMsg = getString(R.string.select_employment_status)
        } else if (countryLivingInSP.selectedItem == null || countryLivingInSP.selectedItem.equals(getString(R.string.select_country))) {
            formOk = false
            errorMsg = getString(R.string.select_country)
        } else if (stateLivingInSP.selectedItem == null || stateLivingInSP.visibility == VISIBLE && stateLivingInSP.selectedItem.equals(getString(R.string.select_state))) {
            formOk = false
            errorMsg = getString(R.string.select_state)
        } else if (motherTongueSP.selectedItem == null || motherTongueSP.selectedItem.equals(getString(R.string.select_mother_tongue))) {
            formOk = false
            errorMsg = getString(R.string.select_mother_tongue)
        } else if (physicalStatusSP.selectedItem == null || physicalStatusSP.selectedItem.equals(getString(R.string.select_physical_status))) {
            formOk = false
            errorMsg = getString(R.string.select_physical_status)
        } else if (bodyTypeSP.selectedItem == null || bodyTypeSP.selectedItem.equals(getString(R.string.select_body_type))) {
            formOk = false
            errorMsg = getString(R.string.select_body_type)
        } else if (!FormValidator.equalField(ageED.text.toString(), 2)) {
            formOk = false
            ageED.error = getString(R.string.enter_age)
        } else if (!FormValidator.equalField(heightED.text.toString(), 3)) {
            formOk = false
            heightED.error = getString(R.string.enter_valid_height)
        } else if (!FormValidator.requiredField(weightED.text.toString(), 2)) {
            formOk = false
            weightED.error = getString(R.string.enter_valid_weight)
        } else if (!FormValidator.requiredField(annualIncomeED.text.toString(), 4)) {
            formOk = false
            annualIncomeED.error = getString(R.string.enter_valid_annual_income)
        } else if (!FormValidator.requiredField(familyStatusED.text.toString(), 1)) {
            formOk = false
            familyStatusED.error = getString(R.string.enter_family_status)
        } else if (!FormValidator.requiredField(ancestralOriginED.text.toString(), 1)) {
            formOk = false
            ancestralOriginED.error = getString(R.string.enter_ancestral_origin)
        } else if (!FormValidator.requiredField(assetsED.text.toString(), 1)) {
            formOk = false
            assetsED.error = getString(R.string.enter_assets)
        } else if (!FormValidator.requiredField(fatherOccupationED.text.toString(), 1)) {
            formOk = false
            fatherOccupationED.error = getString(R.string.enter_father_occupation)
        } else if (!FormValidator.requiredField(motherOccupationED.text.toString(), 1)) {
            formOk = false
            motherOccupationED.error = getString(R.string.enter_father_occupation)
        } else if (!FormValidator.isValidMobile(parentsContactNumberED.text.toString())) {
            formOk = false
            parentsContactNumberED.error = getString(R.string.enter_valid_mobile_number)
        } else if (!FormValidator.requiredField(aboutMyselfED.text.toString(), 1)) {
            formOk = false
            aboutMyselfED.error = getString(R.string.enter_about_myself)
        } else if (profileImageUrlList.isEmpty()) {
            errorMsg = getString(R.string.upload_image)
            formOk = false
        }

        return formOk
    }

    private fun addBodyType() {
        bodyTypeModel.clear()
        bodyTypeModel.add(BaseItem(getString(R.string.select_body_type)))
        bodyTypeModel.add(BaseItem("Slim"))
        bodyTypeModel.add(BaseItem("Fit"))
        bodyTypeModel.add(BaseItem("Fat"))
        bodyTypeList.clear()
        for (i in 0 until bodyTypeModel.size) {
            bodyTypeList.add(bodyTypeModel[i].name!!)
        }
    }

    private fun addPhysicalStatus() {
        physicalStatusModel.clear()
        physicalStatusModel.add(BaseItem(getString(R.string.select_physical_status)))
        physicalStatusModel.add(BaseItem("Physically Challenge"))
        physicalStatusModel.add(BaseItem("Normal"))
        physicalStatusList.clear()
        for (i in 0 until physicalStatusModel.size) {
            physicalStatusList.add(physicalStatusModel[i].name!!)
        }
    }

    private fun addEducation() {
        educationModel.clear()
        educationModel.add(BaseItem("Select Education"))
        educationModel.add(BaseItem("No Education"))
        educationModel.add(BaseItem("SSLC"))
        educationModel.add(BaseItem("Intermediate"))
        educationModel.add(BaseItem("UG"))
        educationModel.add(BaseItem("PG"))
        educationNameList.clear()
        for (i in 0 until educationModel.size) {
            educationNameList.add(educationModel[i].name!!)
        }
    }

    private fun addEmploymentStatus() {
        employmentModel.clear()
        employmentNameList.clear()
        for (i in 0 until employmentModel.size) {
            employmentNameList.add(employmentModel[i].employed_in!!)
        }
    }

    private fun addMaritalStatus() {
        maritalModel.clear()
        maritalModel.add(BaseItem("Select Marital Status"))
        maritalModel.add(BaseItem("Married"))
        maritalModel.add(BaseItem("UnMarried"))
        maritalModel.add(BaseItem("Widow"))
        maritalNameList.clear()
        for (i in 0 until maritalModel.size) {
            maritalNameList.add(maritalModel[i].name!!)
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showSelector()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                STORAGE_CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun showSelector() {
        val options = arrayOf<CharSequence>("Camera", "Photo Library", "Cancel")

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Document Image Source: ")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Camera") {

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }

            } else if (options[item] == "Photo Library") {

                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, REQUEST_CODE_GALLERY)

                /*val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_CODE_GALLERY)*/

            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                gallery(data)
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                camera(data)
            }
        }
    }

    private fun camera(data: Intent) {
        imageBitmap = data.extras!!["data"] as Bitmap?
        file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())

        try {
            file!!.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //val bitmap: Bitmap = this.imageBitmap!!

        val bos = ByteArrayOutputStream()
        imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

        if (imageBitmap != null) {
            uploadFile()
        }

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapdata)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            fos!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            fos!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun gallery(data: Intent) {

        if (data.clipData != null) {
            val cout = data.clipData!!.itemCount
            val imageCount = profileImageModel.size+ cout
            if(imageCount<=5) {
                for (i in 0 until cout) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    uriArray.add(imageUri)
                }
            }
            else{
                showSnackbar("You can upload maximum 5 images")
            }
            val fileParts = arrayOfNulls<MultipartBody.Part>(uriArray.size)
            for (i in 0 until uriArray.size) {

                imageBitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().applicationContext.contentResolver,
                    uriArray[i]
                )
                file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())
                try {
                    file!!.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val bos = ByteArrayOutputStream()
                imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
                val bitmapdata = bos.toByteArray()

                val requestBody: RequestBody =RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                fileParts[i] = MultipartBody.Part.createFormData("file", file!!.name, requestBody)


                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(file)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

                try {
                    fos!!.write(bitmapdata)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    fos!!.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            if (imageBitmap != null) {
                matrimonyViewModel.uploadMultipleProfileImage(fileParts)
            }

        } else {
            //single data from gallery
            val imageUri = data.data

            imageBitmap = MediaStore.Images.Media.getBitmap(
                requireActivity().applicationContext.contentResolver,
                imageUri
            )
            file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())
            try {
                file!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val bos = ByteArrayOutputStream()
            imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            if (imageBitmap != null) {
               uploadFile()
            }

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            try {
                fos!!.write(bitmapdata)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                fos!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun uploadFile() {
        //uploadImage
        val requestBody: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file!!.name, requestBody)
        println("---file name----" + file!!.name)
        matrimonyViewModel.uploadProfileImage(body)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)


        profileImageAdapter = MediaAdapter(requireContext(), profileImageModel)
        zodiacSignAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, zodiacSignNameList)
        starAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, starNameList)
        maritalStateAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, maritalNameList)
        educationAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, educationNameList)
        employmentAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, employmentNameList)
        countryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, countryNameList)
        stateAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, stateNameList)
        cityAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, cityNameList)
        motherTongueAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, motherTongueNameList)
        physicalStatusAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, physicalStatusList)
        bodyTypeAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, bodyTypeList)
    }


    override fun initObservers() {

        matrimonyViewModel.matrimonyRegistrationDetailsLiveData.observe(this, Observer {
            if (it != null) {
                if (it.userDetails != null) {
                    if (it.userDetails!!.moon!!.any()) {
                        zodiacModel.clear()
                        zodiacSignNameList.clear()
                        zodiacModel = ArrayList()
                        zodiacModel.add(MoonItem("0", "Select Zodiac Sign"))
                        zodiacModel.addAll(it.userDetails!!.moon!!)
                        for (i in 0 until zodiacModel.size) {
                            zodiacSignNameList.add(zodiacModel[i].moon_sign!!)
                        }
                        //zodiacSignAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                        //zodiacSignSP.adapter = zodiacSignAdapter
                        zodiacSignAdapter.notifyDataSetChanged()
                    }

                    if (it.userDetails!!.star!!.any()) {
                        starModel.clear()
                        starNameList.clear()
                        starModel.add(StarItem("0", "Select Star"))
                        starModel.addAll(it.userDetails!!.star!!)
                        for (i in 0 until starModel.size) {
                            starNameList.add(starModel[i].star!!)
                        }
                        starAdapter.notifyDataSetChanged()
                    }

                    if (it.userDetails!!.country!!.any()) {
                        countryModel.clear()
                        countryNameList.clear()
                        countryModel.add(CountryItem("0","Select Country"))
                        countryModel.addAll(it.userDetails!!.country!!)
                        for (i in 0 until countryModel.size) {
                            countryNameList.add(countryModel[i].country!!)
                        }
                        countryAdapter.notifyDataSetChanged()
                    }

                    if (it.userDetails!!.employed_id!!.any()) {
                        employmentModel.clear()
                        employmentNameList.clear()
                        employmentModel.add(EmploymentItem("0","Select Employment Status"))
                        employmentModel.addAll(it.userDetails!!.employed_id!!)
                        for (i in 0 until employmentModel.size) {
                            employmentNameList.add(employmentModel[i].employed_in!!)
                        }
                        employmentAdapter.notifyDataSetChanged()
                    }

                    if (it.userDetails!!.mothertongues!!.any()) {
                        motherTongueModel.clear()
                        motherTongueNameList.clear()
                        motherTongueModel.add(MotherTongueItem("0", "Select Mother Tongue"))
                        motherTongueModel.addAll(it.userDetails!!.mothertongues!!)
                        for (i in 0 until motherTongueModel.size) {
                            motherTongueNameList.add(motherTongueModel[i].mother_tongue!!)
                        }
                        motherTongueAdapter.notifyDataSetChanged()
                    }
                }
            }

        })

        matrimonyViewModel.stateLiveData.observe(this, Observer {
            if(isAdded()) {
                if (it.state!!.any()) {
                    stateModel.clear()
                    stateNameList.clear()
                    stateModel.add(StateItem("0", "Select State"))
                    stateModel.addAll(it.state!!)
                    for (i in 0 until stateModel.size) {
                        stateNameList.add(stateModel[i].state!!)
                    }
                    stateAdapter.notifyDataSetChanged()
                }
            }
        })

        matrimonyViewModel.cityLiveData.observe(this, Observer {
            if(isAdded()) {
                if (it.cities!!.any()) {
                    cityModel.clear()
                    cityNameList.clear()
                    cityModel.addAll(it.cities!!)
                    for (i in 0 until cityModel.size) {
                        cityNameList.add(cityModel[i].city!!)
                    }
                    cityAdapter.notifyDataSetChanged()
                }
            }
        })

        matrimonyViewModel.profileImageLiveData.observe(this, Observer {
            if(isAdded()) {
                if (it != null) {

                    profileImageModel.addAll(it.media_file!!)
                    println("----profilemodel---" + it.media_file!!.size)
                    profileImageRV.visibility = VISIBLE
                    profileImageAdapter.notifyDataSetChanged()
                    openGalleryTV.visibility = GONE
                    chooseMediaTagTV.visibility = GONE
                    for (i in 0 until it.media_file!!.size) {
                        imageUrl = it.media_file!![i].fileurl!!
                        profileImageUrlList.add(imageUrl!!)
                    }
                    println("----profilemodellist---" + profileImageUrlList)
                    uriArray.clear()
                }
            }
        })

        matrimonyViewModel.matrimonyRegistrationLiveData.observe(this, Observer {
            if(isAdded()) {
                if (it != null) {
                    showSnackbar(it.message!!)
                    findPerfectMatchInterface.onFindPerfectMatchClicked(false)
                }
            }
        })

    }
}