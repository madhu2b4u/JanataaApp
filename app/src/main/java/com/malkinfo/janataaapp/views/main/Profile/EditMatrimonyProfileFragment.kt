package com.malkinfo.janataaapp.views.main.Profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
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


class EditMatrimonyProfileFragment : MyBaseFragment() {

    private var uriArray: ArrayList<Uri> = ArrayList()
    private var moonSign: String? = null
    private var imageUrl: String? = null
    var imageBitmap: Bitmap? = null
    var file: File? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    protected val REQUEST_CODE_GALLERY = 2
    private val STORAGE_CAMERA_PERMISSION_CODE = 34
    private lateinit var errorMsg: String
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
    /**set ID */
    private lateinit var motherTongueAdapter: ArrayAdapter<String>
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var starAdapter: ArrayAdapter<String>
    private lateinit var zodiacSignAdapter: ArrayAdapter<String>
    private lateinit var bodyTypeAdapter: ArrayAdapter<String>
    private lateinit var physicalStatusAdapter: ArrayAdapter<String>
    private lateinit var maritalStateAdapter: ArrayAdapter<String>
    private lateinit var employmentStateAdapter: ArrayAdapter<String>
    private lateinit var educationAdapter: ArrayAdapter<String>
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
    /**===============================================================================*/

    private lateinit var listener: CommunityStore

    /**set Id*/
    private lateinit var moonSignSP:Spinner
    private lateinit var updateStarSP:Spinner
    private lateinit var updateMotherTongueSP:Spinner
    private lateinit var updateCountryLivingInSP:Spinner
    private lateinit var updateEmployedInSP:Spinner
    private lateinit var updateStateLivingInSP:Spinner
    private lateinit var updateCityLivingInSP:Spinner
    private lateinit var updateProfileImageRV:RecyclerView
    private lateinit var openGalleryTV:TextView
    private lateinit var chooseMediaTagTV:TextView
    private lateinit var updateAgeED: TextInputEditText
    private lateinit var updateHeightED:TextInputEditText
    private lateinit var updateWeightED:TextInputEditText
    private lateinit var updateAnnualIncomeED:TextInputEditText
    private lateinit var updateFamilyStatusED:TextInputEditText
    private lateinit var updateAncestralOriginED:TextInputEditText
    private lateinit var updateAssetsED:TextInputEditText
    private lateinit var updateFatherOccupationED:TextInputEditText
    private lateinit var updateMotherOccupationED:TextInputEditText
    private lateinit var updateParentsContactNumberED:TextInputEditText
    private lateinit var updateAboutMyselfED:TextInputEditText
    private lateinit var updateJointFamilyRB:RadioButton
    private lateinit var updateNuclearFamilyRB:RadioButton
    private lateinit var updateBodyTypeSP:Spinner
    private lateinit var updatePhysicalStatusSP:Spinner
    private lateinit var updateEducationSP:Spinner
    private lateinit var updateMaritalStatusSP:Spinner
    private lateinit var updateStateLivingInHintTV:TextView
    private lateinit var updateCityLivingInHintTV:TextView
    private lateinit var updateMatrimonyProfileBT: MaterialButton





    companion object {

        @JvmStatic
        fun newInstance() =
            EditMatrimonyProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val updateMatrimonyProfileViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_edit_matrimony_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initss(view)
        setUpLoader(updateMatrimonyProfileViewModel)
        initViews()
    }
    private fun initss(v:View){
        moonSignSP = v.findViewById(R.id.moonSignSP)
        updateStarSP = v.findViewById(R.id.updateStarSP)
        updateMotherTongueSP = v.findViewById(R.id.updateMotherTongueSP)
        updateCountryLivingInSP = v.findViewById(R.id.updateCountryLivingInSP)
        updateEmployedInSP = v.findViewById(R.id.updateEmployedInSP)
        updateStateLivingInSP = v.findViewById(R.id.updateStateLivingInSP)
        updateCityLivingInSP = v.findViewById(R.id.updateCityLivingInSP)
        updateProfileImageRV = v.findViewById(R.id.updateProfileImageRV)
        openGalleryTV = v.findViewById(R.id.openGalleryTV)
        chooseMediaTagTV = v.findViewWithTag(R.id.chooseMediaTagTV)
        updateAgeED = v.findViewById(R.id.updateAgeED)
        updateHeightED = v.findViewById(R.id.updateHeightED)
        updateWeightED  = v.findViewById(R.id.updateWeightED)
        updateAnnualIncomeED = v.findViewById(R.id.updateAnnualIncomeED)
        updateFamilyStatusED = v.findViewById(R.id.updateFamilyStatusED)
        updateAncestralOriginED = v.findViewById(R.id.updateAncestralOriginED)
        updateAssetsED = v.findViewById(R.id.updateAssetsED)
        updateFatherOccupationED = v.findViewById(R.id.updateFatherOccupationED)
        updateMotherOccupationED = v.findViewById(R.id.updateMotherOccupationED)
        updateParentsContactNumberED = v.findViewById(R.id.updateParentsContactNumberED)
        updateAboutMyselfED = v.findViewById(R.id.updateAboutMyselfED)
        updateJointFamilyRB = v.findViewById(R.id.updateJointFamilyRB)
        updateNuclearFamilyRB = v.findViewById(R.id.updateNuclearFamilyRB)
        updateBodyTypeSP = v.findViewById(R.id.updateBodyTypeSP)
        updatePhysicalStatusSP = v.findViewById(R.id.updatePhysicalStatusSP)
        updateEducationSP = v.findViewById(R.id.updateEducationSP)
        updateMaritalStatusSP = v.findViewById(R.id.updateMaritalStatusSP)
        updateStateLivingInHintTV = v.findViewById(R.id.updateStateLivingInHintTV)
        updateCityLivingInHintTV = v.findViewById(R.id.updateCityLivingInHintTV)
        updateMatrimonyProfileBT = v.findViewById(R.id.updateMatrimonyProfileBT)

    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }


    override fun initObservers() {


        updateMatrimonyProfileViewModel.matrimonyProfileDetailsLiveData.observe(
            requireActivity(),
            Observer {


                if (it != null) {
                    if (it.userDetails != null) {

                        if (it.userDetails!!.moon!!.any()) {
                            zodiacModel.clear()
                            zodiacSignNameList.clear()
                            zodiacModel.add(MoonItem("0", getString(R.string.select_zodiac_sign)))
                            zodiacModel.addAll(it.userDetails!!.moon!!)
                            for (i in 0 until zodiacModel.size) {
                                zodiacSignNameList.add(zodiacModel[i].moon_sign!!)
                            }

                            zodiacSignAdapter = ArrayAdapter(
                                requireActivity(),
                                R.layout.spinner_item,
                                zodiacSignNameList
                            )
                            zodiacSignAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                            moonSignSP.adapter = zodiacSignAdapter

                            for (i in 0 until zodiacModel.size) {
                                if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.moon.equals(
                                        zodiacModel[i]._id
                                    )
                                ) {
                                    moonSignSP.setSelection(i)
                                }
                            }
                        }

                        if (it.userDetails!!.star!!.any()) {
                            starModel.clear()
                            starNameList.clear()
                            starModel.add(StarItem("0", getString(R.string.select_star)))
                            starModel.addAll(it.userDetails!!.star!!)
                            for (i in 0 until starModel.size) {
                                starNameList.add(starModel[i].star!!)
                            }
                            starAdapter =
                                ArrayAdapter(requireActivity(), R.layout.spinner_item, starNameList)
                            starAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                            updateStarSP.adapter = starAdapter

                            for (i in 0 until starModel.size) {
                                if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.star.equals(
                                        starModel[i]._id
                                    )
                                ) {
                                    updateStarSP.setSelection(i)
                                }
                            }
                        }

                        if (it.userDetails!!.mothertongues!!.any()) {
                            motherTongueModel.clear()
                            motherTongueNameList.clear()
                            motherTongueModel.add(
                                MotherTongueItem(
                                    "0",
                                    getString(R.string.select_mother_tongue)
                                )
                            )
                            motherTongueModel.addAll(it.userDetails!!.mothertongues!!)
                            for (i in 0 until motherTongueModel.size) {
                                motherTongueNameList.add(motherTongueModel[i].mother_tongue!!)
                            }

                            motherTongueAdapter = ArrayAdapter(
                                requireActivity(),
                                R.layout.spinner_item,
                                motherTongueNameList
                            )
                            motherTongueAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                            updateMotherTongueSP.adapter = motherTongueAdapter

                            for (i in 0 until motherTongueModel.size) {
                                if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.mother_tongue!!.equals(
                                        motherTongueModel[i]._id
                                    )
                                ) {
                                    updateMotherTongueSP.setSelection(i)
                                }
                            }
                        }

                        if (it.userDetails!!.country!!.any()) {
                            countryModel.clear()
                            countryNameList.clear()
                            countryModel.add(CountryItem("0", getString(R.string.select_country)))
                            countryModel.addAll(it.userDetails!!.country!!)
                            for (i in 0 until countryModel.size) {
                                countryNameList.add(countryModel[i].country!!)
                            }

                            countryAdapter = ArrayAdapter(
                                requireActivity(),
                                R.layout.spinner_item,
                                countryNameList
                            )
                            countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                            updateCountryLivingInSP.adapter = countryAdapter


                            for (i in 0 until countryModel.size) {
                                if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.country_living_in.equals(
                                        countryModel[i]._id
                                    )
                                ) {
                                    updateCountryLivingInSP.setSelection(i)
                                }
                            }

                        }

                        if (it.userDetails!!.employed_id!!.any()) {
                            employmentModel.clear()
                            employmentNameList.clear()
                            employmentModel.add(
                                EmploymentItem(
                                    "0",
                                    getString(R.string.select_employment_status)
                                )
                            )
                            employmentModel.addAll(it.userDetails!!.employed_id!!)
                            for (i in 0 until employmentModel.size) {
                                employmentNameList.add(employmentModel[i].employed_in!!)
                            }

                            employmentStateAdapter =
                                ArrayAdapter(
                                    requireActivity(),
                                    R.layout.spinner_item,
                                    employmentNameList
                                )
                            employmentStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                            updateEmployedInSP.adapter = employmentStateAdapter

                            for (i in 0 until employmentModel.size) {
                                if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.employed_in.equals(
                                        employmentModel[i]._id
                                    )
                                ) {
                                    updateEmployedInSP.setSelection(i)
                                }
                            }

                        }

                    }
                }

            })

        updateMatrimonyProfileViewModel.profileStateLiveData.observe(this, Observer {
            if (it.state!!.any()) {
                stateModel.clear()
                stateNameList.clear()
                stateModel.add(StateItem("0", getString(R.string.select_state)))
                stateModel.addAll(it.state!!)
                for (i in 0 until stateModel.size) {
                    stateNameList.add(stateModel[i].state!!)
                }
                stateAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, stateNameList)
                stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                updateStateLivingInSP.adapter = stateAdapter

                for (i in 0 until stateModel.size) {
                    if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.state_living_in!!.equals(
                            stateModel[i]._id
                        )
                    ) {
                        updateStateLivingInSP.setSelection(i)
                    }
                }
            }
        })

        updateMatrimonyProfileViewModel.profileCityLiveData.observe(this, Observer {
            if (it.cities!!.any()) {
                cityModel.clear()
                cityNameList.clear()
                cityModel.addAll(it.cities!!)
                for (i in 0 until cityModel.size) {
                    cityNameList.add(cityModel[i].city!!)
                }
            }

            cityAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, cityNameList)
            cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
            updateCityLivingInSP.adapter = cityAdapter

            for (i in 0 until cityModel.size) {
                if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.city_living_in.equals(
                        cityModel[i]._id
                    )
                ) {
                    updateCityLivingInSP.setSelection(i)
                }
            }

        })


        updateMatrimonyProfileViewModel.updateProfileImageLiveData.observe(
            requireActivity(),
            Observer {
                if (it != null) {
                    profileImageModel.addAll(it.media_file!!)
                    updateProfileImageRV.visibility = View.VISIBLE
                    profileImageAdapter.notifyDataSetChanged()
                    openGalleryTV.visibility = View.GONE
                    chooseMediaTagTV.visibility = View.GONE
                    for (i in 0 until it.media_file!!.size) {
                        imageUrl = it.media_file!![i].fileurl!!
                        profileImageUrlList.add(imageUrl!!)
                    }
                    uriArray.clear()
                }

            })


        updateMatrimonyProfileViewModel.matrimonyProfileUpdateLiveData.observe(
            requireActivity(),
            Observer {
                if (it != null) {
                    showSnackbar(it.message!!)
                    listener.onBack()
                    requireActivity().viewModelStore.clear()
                }
            })
    }


    private fun initViews() {
        updateMatrimonyProfileViewModel.getMatrimonyProfileDetails()

        addBodyType()
        addPhysicalStatus()
        addMaritalStatus()
        addEmploymentStatus()
        addEducation()

        profileImageUrlList.addAll(MyCommunityApp.getMatrimonyUser(requireActivity())!!.profile_url!!)
        for (i in profileImageUrlList) {
            profileImageModel.add(FileUploadItem(i, ""))
        }

        updateProfileImageRV.visibility = View.VISIBLE
        chooseMediaTagTV.visibility = View.GONE
        openGalleryTV.visibility = View.GONE

        updateAgeED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.age.toString())
        updateHeightED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.height.toString())
        updateWeightED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.weight.toString())
        updateAnnualIncomeED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.annual_income.toString())
        updateFamilyStatusED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.family_status)
        updateAncestralOriginED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.ancestral_origin)
        updateAssetsED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.assets)
        updateFatherOccupationED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.fathers_occupation)
        updateMotherOccupationED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.mother_occupation)
        updateParentsContactNumberED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.parent_number)
        updateAboutMyselfED.setText(MyCommunityApp.getMatrimonyUser(requireActivity())!!.about_myself)


        if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.family_type.equals("Joint")) {
            updateJointFamilyRB.isChecked = true
            updateNuclearFamilyRB.isChecked = false
            familyType = updateJointFamilyRB.text.toString()
        } else if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.family_type.equals("Nuclear")) {
            updateNuclearFamilyRB.isChecked = true
            updateJointFamilyRB.isChecked = false
            familyType = updateNuclearFamilyRB.text.toString()
        }



        profileImageAdapter = MediaAdapter(requireActivity(), profileImageModel)
        updateProfileImageRV.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        updateProfileImageRV.adapter = profileImageAdapter
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

        bodyTypeAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, bodyTypeList)
        bodyTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updateBodyTypeSP.adapter = bodyTypeAdapter
        for (i in 0 until bodyTypeModel.size) {
            if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.body_type.equals(bodyTypeModel[i].name)) {
                updateBodyTypeSP.setSelection(i)
            }
        }

        physicalStatusAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, physicalStatusList)
        physicalStatusAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updatePhysicalStatusSP.adapter = physicalStatusAdapter


        for (i in 0 until physicalStatusModel.size) {
            if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.physical_status.equals(
                    physicalStatusModel[i].name
                )
            ) {
                updatePhysicalStatusSP.setSelection(i)
            }
        }

        educationAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, educationNameList)
        educationAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updateEducationSP.adapter = educationAdapter


        for (i in 0 until educationModel.size) {
            if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.education.equals(educationModel[i].name)) {
                updateEducationSP.setSelection(i)
            }
        }

        maritalStateAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, maritalNameList)
        maritalStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updateMaritalStatusSP.adapter = maritalStateAdapter

        for (i in 0 until maritalModel.size) {
            if (MyCommunityApp.getMatrimonyUser(requireActivity())!!.marital_status.equals(
                    maritalModel[i].name
                )
            ) {
                updateMaritalStatusSP.setSelection(i)
            }
        }


        updateCountryLivingInSP.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    countryId = countryModel[pos]._id

                    if (updateCountryLivingInSP.selectedItem.equals("India")) {
                        updateStateLivingInSP.visibility = View.VISIBLE
                        updateStateLivingInHintTV.visibility = View.VISIBLE
                        updateCityLivingInSP.visibility = View.VISIBLE
                        updateCityLivingInHintTV.visibility = View.VISIBLE
                        updateMatrimonyProfileViewModel.getProfileStateList(countryId!!)
                    } else {
                        updateStateLivingInSP.visibility = View.GONE
                        updateStateLivingInHintTV.visibility = View.GONE
                        updateCityLivingInSP.visibility = View.GONE
                        updateCityLivingInHintTV.visibility = View.GONE
                    }

                }

            }
        updateStateLivingInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {


                if (updateStateLivingInSP.selectedItem.equals(getString(R.string.select_state))) {
                    updateCityLivingInSP.isClickable = false
                } else {
                    updateCityLivingInSP.isClickable = true
                    stateID = stateModel[pos]._id
                    updateMatrimonyProfileViewModel.getProfileCity(stateID!!)
                }

            }

        }

        updateCityLivingInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                cityId = cityModel[pos]._id
            }

        }

        updateMotherTongueSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                motherTongue = motherTongueModel[pos]._id
            }

        }

        updateMaritalStatusSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                maritalStatus = maritalModel[pos].name
            }

        }

        updateEducationSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                educationType = educationModel[pos].name
            }

        }

        updateEmployedInSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                employmentType = employmentModel[pos]._id
            }

        }

        updatePhysicalStatusSP.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    physicalStatus = physicalStatusModel[pos].name
                }

            }

        updateBodyTypeSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                bodyType = bodyTypeModel[pos].name
            }

        }

        updateStarSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                star = starModel[pos]._id
            }

        }

        moonSignSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                moonSign = zodiacModel[pos]._id
            }

        }
        updateJointFamilyRB.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isJointChecked: Boolean) {
                if (isJointChecked) {

                    familyType = updateJointFamilyRB.text.toString()

                    if (updateNuclearFamilyRB.isChecked) {
                        updateNuclearFamilyRB.setChecked(false)
                    }

                } else {
                    if (isJointChecked) {
                        updateJointFamilyRB.setChecked(false)
                    }
                }
            }

        })


        updateNuclearFamilyRB.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isNuclearChecked: Boolean) {
                if (isNuclearChecked) {
                    familyType = updateNuclearFamilyRB.text.toString()
                    if (updateJointFamilyRB.isChecked) {
                        updateJointFamilyRB.setChecked(false)
                    }
                } else {
                    if (isNuclearChecked) {
                        updateNuclearFamilyRB.setChecked(false)
                    }
                }
            }

        })

        openGalleryTV.setOnClickListener {
            requestPermission()
        }

        updateMatrimonyProfileBT.setOnClickListener {

            if (validation()) {
                //Do Matrimonys Register
                val matimonyDetails = JSONObject()
                val main = JSONObject()
                val array = JSONArray()
                for (i in profileImageUrlList) {
                    array.put(i)
                }
                val height = updateHeightED.text.toString().toInt()
                val age = updateAgeED.text.toString().toInt()
                val weight = updateWeightED.text.toString().toInt()
                val annualIncome = updateAnnualIncomeED.text.toString().toInt()
                val familyStatus = updateFamilyStatusED.text.toString()
                val ancestralOrigin = updateAncestralOriginED.text.toString()
                val assets = updateAssetsED.text.toString()
                val fatherOccupation = updateFatherOccupationED.text.toString()
                val motherOccupation = updateMotherOccupationED.text.toString()
                val parentContactNumber = updateParentsContactNumberED.text.toString()
                val aboutMyself = updateAboutMyselfED.text.toString()
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
                    updateMatrimonyProfileViewModel.doMatrimonyProfileUpdate(gsonObject)
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
        if (moonSignSP.selectedItem.equals(getString(R.string.select_zodiac_sign))) {
            formOk = false
            errorMsg = getString(R.string.select_zodiac_sign)
        } else if (updateStarSP.selectedItem.equals(getString(R.string.select_star))) {
            formOk = false
            errorMsg = getString(R.string.select_star)
        } else if (updateMaritalStatusSP.selectedItem.equals(getString(R.string.select_marital_status))) {
            formOk = false
            errorMsg = getString(R.string.select_marital_status)
        } else if (updateEducationSP.selectedItem.equals(getString(R.string.select_education))) {
            formOk = false
            errorMsg = getString(R.string.select_education)
        } else if (updateEmployedInSP.selectedItem.equals(getString(R.string.select_employment_status))) {
            formOk = false
            errorMsg = getString(R.string.select_employment_status)
        } else if (updateCountryLivingInSP.selectedItem.equals(getString(R.string.select_country))) {
            formOk = false
            errorMsg = getString(R.string.select_country)
        } else if (updateStateLivingInSP.visibility == View.VISIBLE && updateStateLivingInSP.selectedItem.equals(
                getString(R.string.select_state)
            )
        ) {
            formOk = false
            errorMsg = getString(R.string.select_state)
        } else if (updateMotherTongueSP.selectedItem.equals(getString(R.string.select_mother_tongue))) {
            formOk = false
            errorMsg = getString(R.string.select_mother_tongue)
        } else if (updatePhysicalStatusSP.selectedItem.equals(getString(R.string.select_physical_status))) {
            formOk = false
            errorMsg = getString(R.string.select_physical_status)
        } else if (updateBodyTypeSP.selectedItem.equals(getString(R.string.select_body_type))) {
            formOk = false
            errorMsg = getString(R.string.select_body_type)
        } else if (!FormValidator.equalField(updateAgeED.text.toString(), 2)) {
            formOk = false
            updateAgeED.error = getString(R.string.enter_age)
        } else if (!FormValidator.equalField(updateHeightED.text.toString(), 3)) {
            formOk = false
            updateHeightED.error = getString(R.string.enter_valid_height)
        } else if (!FormValidator.requiredField(updateWeightED.text.toString(), 2)) {
            formOk = false
            updateWeightED.error = getString(R.string.enter_valid_weight)
        } else if (!FormValidator.requiredField(updateAnnualIncomeED.text.toString(), 4)) {
            formOk = false
            updateAnnualIncomeED.error = getString(R.string.enter_valid_annual_income)
        } else if (!FormValidator.requiredField(updateFamilyStatusED.text.toString(), 1)) {
            formOk = false
            updateFamilyStatusED.error = getString(R.string.enter_family_status)
        } else if (!FormValidator.requiredField(updateAncestralOriginED.text.toString(), 1)) {
            formOk = false
            updateAncestralOriginED.error = getString(R.string.enter_ancestral_origin)
        } else if (!FormValidator.requiredField(updateAssetsED.text.toString(), 1)) {
            formOk = false
            updateAssetsED.error = getString(R.string.enter_assets)
        } else if (!FormValidator.requiredField(updateFatherOccupationED.text.toString(), 1)) {
            formOk = false
            updateFatherOccupationED.error = getString(R.string.enter_father_occupation)
        } else if (!FormValidator.requiredField(updateMotherOccupationED.text.toString(), 1)) {
            formOk = false
            updateMotherOccupationED.error = getString(R.string.enter_father_occupation)
        } else if (!FormValidator.isValidMobile(updateParentsContactNumberED.text.toString())) {
            formOk = false
            updateParentsContactNumberED.error = getString(R.string.enter_valid_mobile_number)
        } else if (!FormValidator.requiredField(updateAboutMyselfED.text.toString(), 1)) {
            formOk = false
            updateAboutMyselfED.error = getString(R.string.enter_about_myself)
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
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    REQUEST_CODE_GALLERY
                )

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
        imageBitmap = data!!.extras!!["data"] as Bitmap?
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

    private fun gallery(data: Intent) {
        if (data.clipData != null) {
            val cout = data.clipData!!.itemCount
            val imageCount = profileImageModel.size + cout
            if (imageCount <= 5) {
                for (i in 0 until cout) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    uriArray.add(imageUri)
                }
            } else {
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

                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                fileParts[i] =
                    MultipartBody.Part.createFormData("file", file!!.getName(), requestFile)


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
                updateMatrimonyProfileViewModel.updateMultipleProfileImage(fileParts)
            }


        } else {
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
            val bitmap: Bitmap = this.imageBitmap!!

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
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
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file!!.getName(), requestFile)
        updateMatrimonyProfileViewModel.updateProfileImage(body)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            listener = context as CommunityStore
        }
    }
}