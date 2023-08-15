package com.malkinfo.janataaapp.views.main.Profile

import MyCommunityApp
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.BloodGroupItem
import com.malkinfo.janataaapp.models.Matrimony.CountryItem
import com.malkinfo.janataaapp.models.base.*
import com.malkinfo.janataaapp.viewmodels.BaseViewModel
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class EditProfileFragment : MyBaseFragment() {
    var aadharCardBackUrl: String? = null
    var aadharCardFrontUrl: String? = null
    private var countryId: String? = null
    private var mandalID: String? = null
    private lateinit var villageAdapter: ArrayAdapter<String>
    private lateinit var districtAdapter: ArrayAdapter<String>
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var countryAdapter: ArrayAdapter<String>
    private lateinit var mandalAdapter: ArrayAdapter<String>
    var mTextValue: String? = null
    var mLastChar = '\u0000' // init with empty character
    var mKeyDel = 0
    private var isProfileImageUpload: Boolean? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    protected val REQUEST_CODE_GALLERY = 2
    private val STORAGE_CAMERA_PERMISSION_CODE = 34
    var imageBitmap: Bitmap? = null
    var file: File? = null
    var profileUrl: String? = null
    private var gender: String? = null
    private var employmentState: String? = null
    private var maritalStatus: String? = null
    private var bloodGroup: String? = null
    private var stateID: String? = null
    private var districtID: String? = null
    private var villageID: String? = null
    private var casteID: String? = null
    /*private lateinit var casteAdapter: CasteAdapter
    private var casteNameList: ArrayList<String> = ArrayList()
    private lateinit var casteListDialog: Dialog
    private lateinit var editProfileInterface: EditProfileInterface
    var aadharUrl: String? = null
    var isCondition = false*/
    private lateinit var errorMsg: String
    private var stateNameList: ArrayList<String> = ArrayList()
    private var districtNameList: ArrayList<String> = ArrayList()
    private var villageNameList: ArrayList<String> = ArrayList()
    private var countryNameList: ArrayList<String> = ArrayList()
    private var mandalNameList: ArrayList<String> = ArrayList()
    private var stateModel: ArrayList<StateItem> = ArrayList()
    private var countryModel: ArrayList<CountryItem> = ArrayList()
    private var mandalModel: ArrayList<MandalItem> = ArrayList()
    private var districtModel: ArrayList<DistrictItem> = ArrayList()
    private var villageModel: ArrayList<VillageItem> = ArrayList()
    private var casteModel: ArrayList<CasteItem> = ArrayList()
    private var bloodGroupModel: ArrayList<BloodGroupItem> = ArrayList()
    private var bloodGroupNameList: ArrayList<String> = ArrayList()
    private var maritalNameList: ArrayList<String> = ArrayList()
    private var employmentStateNameList: ArrayList<String> = ArrayList()
    private var maritalModel: ArrayList<BaseItem> = ArrayList()
    private var employmentModel: ArrayList<BaseItem> = ArrayList()
    private var isFrontImageUpload: Boolean? = null
    private lateinit var listener : CommunityStore

    /**set id*/
    private lateinit var profileIV: CircleImageView
    private lateinit var updateAadharCardNumberTV:TextView
    private lateinit var updateFullNameET: TextInputEditText
    private lateinit var updateParentNameET:TextInputEditText
    private lateinit var updateAddressET:TextInputEditText
    private lateinit var updatePinCodeET:TextInputEditText
    private lateinit var updateDateOfBirthET:TextInputEditText
    private lateinit var updatePrivateWorkET:TextInputEditText
    private lateinit var updateOtherWorkET:TextInputEditText
    private lateinit var updateMaleRB:RadioButton
    private lateinit var frontAadhaarCardIV:ImageView
    private lateinit var updateFemaleRB:RadioButton
    private lateinit var backAadhaarCardIV:ImageView
    private lateinit var updateBloodGroupSP:Spinner
    private lateinit var updateEmploymentStateSP:Spinner
    private lateinit var updateMaritalStatusSP:Spinner
    private lateinit var updateCountrySP:Spinner
    private lateinit var updateStateSP:Spinner
    private lateinit var updateDistrictSP:Spinner
    private lateinit var updateMandalSP:Spinner
    private lateinit var updateVillageSP:Spinner
    private lateinit var updatePrivateWorkTL: TextInputLayout
    private lateinit var updateOtherWorkTL:TextInputLayout
    private lateinit var frontAadharCL:ConstraintLayout
    private lateinit var backAadharCL:ConstraintLayout
    private lateinit var uploadPhotoCL:ConstraintLayout
    private lateinit var updateProfileBT: MaterialButton
    private lateinit var updateCasteTV:TextView
    /**=========================================================*/


    private val viewModel: BaseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BaseViewModel::class.java)
    }

    private val editProfileViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    /*interface EditProfileInterface {

        fun onUpdateProfileChangesClicked()
    }*/

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsa(view)
        setUpLoader(viewModel)
        setUpLoader(editProfileViewModel)
        initViews()
    }
    private fun initsa(v:View){
        profileIV = v.findViewById(R.id.profileIV)
        updateAadharCardNumberTV = v.findViewById(R.id.updateAadharCardNumberTV)
        updateFullNameET = v.findViewById(R.id.updateFullNameET)
        updateParentNameET = v.findViewById(R.id.updateParentNameET)
        updateAddressET = v.findViewById(R.id.updateAddressET)
        updatePinCodeET = v.findViewById(R.id.updatePinCodeET)
        updateDateOfBirthET = v.findViewById(R.id.updateDateOfBirthET)
        updatePrivateWorkET = v.findViewById(R.id.updatePrivateWorkET)
        updateOtherWorkET = v.findViewById(R.id.updateOtherWorkET)
        updateMaleRB = v.findViewById(R.id.updateMaleRB)
        frontAadhaarCardIV = v.findViewById(R.id.frontAadhaarCardIV)
        updateFemaleRB = v.findViewById(R.id.updateFemaleRB)
        backAadhaarCardIV = v.findViewById(R.id.backAadhaarCardIV)
        updateBloodGroupSP = v.findViewById(R.id.updateBloodGroupSP)
        updateEmploymentStateSP = v.findViewById(R.id.updateEmploymentStateSP)
        updateMaritalStatusSP = v.findViewById(R.id.updateMaritalStatusSP)
        updateCountrySP = v.findViewById(R.id.updateCountrySP)
        updateStateSP = v.findViewById(R.id.updateStateSP)
        updateDistrictSP = v.findViewById(R.id.updateDistrictSP)
        updateMandalSP = v.findViewById(R.id.updateMandalSP)
        updateVillageSP = v.findViewById(R.id.updateVillageSP)
        updatePrivateWorkTL = v.findViewById(R.id.updatePrivateWorkTL)
        updateOtherWorkTL = v.findViewById(R.id.updateOtherWorkTL)
        frontAadharCL = v.findViewById(R.id.frontAadharCL)
        backAadharCL = v.findViewById(R.id.backAadharCL)
        uploadPhotoCL = v.findViewById(R.id.uploadPhotoCL)
        updateProfileBT = v.findViewById(R.id.updateProfileBT)
        updateCasteTV = v.findViewById(R.id.updateCasteTV)
    }

    private fun initViews() {
        addBloodGroup()
        addMaritalStatus()
        addEmploymentStatus()

        if (MyCommunityApp.getUser(requireActivity())!!.profile_url.isNullOrEmpty()) {
            profileIV.load(R.drawable.profile_iv)

        } else {
            profileIV.load(MyCommunityApp.getUser(requireActivity())!!.profile_url)
        }
        updateAadharCardNumberTV.setText(MyCommunityApp.getUser(requireActivity())!!.aadhar_number)
        updateFullNameET.setText(MyCommunityApp.getUser(requireActivity())!!.full_name)
        updateParentNameET.setText(MyCommunityApp.getUser(requireActivity())!!.parent_name)
        updateAddressET.setText(MyCommunityApp.getUser(requireActivity())!!.address)
        updatePinCodeET.setText(MyCommunityApp.getUser(requireActivity())!!.pincode)
        updateDateOfBirthET.setText(MyCommunityApp.getUser(requireActivity())!!.DOB)
        updatePrivateWorkET.setText(MyCommunityApp.getUser(requireActivity())!!.mention_work)
        updateOtherWorkET.setText(MyCommunityApp.getUser(requireActivity())!!.others)


        if (MyCommunityApp.getUser(requireActivity())!!.gender.equals("Male")) {
            updateMaleRB.isChecked = true
        } else
            if (MyCommunityApp.getUser(requireActivity())!!.gender.equals("Female")) { updateFemaleRB.isChecked = true }

        if (MyCommunityApp.getUser(requireActivity())!!.aadhar_card_front.isNullOrEmpty()) {
            frontAadhaarCardIV.visibility = GONE
        } else {
            frontAadhaarCardIV.visibility = VISIBLE
            frontAadhaarCardIV.load((MyCommunityApp.getUser(requireActivity())!!.aadhar_card_front))
        }

        if (MyCommunityApp.getUser(requireActivity())!!.aadhar_card_back.isNullOrEmpty()) {
            backAadhaarCardIV.visibility = GONE
        } else {
            backAadhaarCardIV.visibility = VISIBLE
            backAadhaarCardIV.load((MyCommunityApp.getUser(requireActivity())!!.aadhar_card_back))
        }

        val bloodGroupAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, bloodGroupNameList)
        bloodGroupAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updateBloodGroupSP.adapter = bloodGroupAdapter

        for (i in 0 until bloodGroupModel.size) {
            if (MyCommunityApp.getUser(requireActivity())!!.blood_group.equals(bloodGroupModel[i].name)) {
                updateBloodGroupSP.setSelection(i)
            }
        }

        val employmentStateAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, employmentStateNameList)
        employmentStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updateEmploymentStateSP.adapter = employmentStateAdapter

        for (i in 0 until employmentModel.size) {
            if (MyCommunityApp.getUser(requireActivity())!!.employment_state.equals(employmentModel[i].name)) {
                updateEmploymentStateSP.setSelection(i)
            }
        }

        val maritalStateAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, maritalNameList)
        maritalStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        updateMaritalStatusSP.adapter = maritalStateAdapter

        for (i in 0 until maritalModel.size) {
            if (MyCommunityApp.getUser(requireActivity())!!.marital_status.equals(maritalModel[i].name)) {
                updateMaritalStatusSP.setSelection(i)
            }
        }


        updateCountrySP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                countryId = countryModel[pos]._id

                if (updateCountrySP.selectedItem.equals(getString(R.string.select_country))) {
                    updateStateSP.isClickable = false
                    updateDistrictSP.isClickable = false
                    updateMandalSP.isClickable = false
                    updateVillageSP.isClickable = false
                } else {
                    updateStateSP.isClickable = true
                    viewModel.getStateList(countryId!!)
                }
            }

        }

        updateStateSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                stateID = stateModel[pos]._id

                if (updateStateSP.selectedItem.equals(getString(R.string.select_state))) {
                    updateDistrictSP.isClickable = false
                    updateMandalSP.isClickable = false
                    updateMandalSP.isClickable = false
                } else {
                    updateDistrictSP.isClickable = true
                    viewModel.getDistrictList(stateID!!)
                }
            }

        }

        updateDistrictSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    districtID = districtModel[pos]._id
                    if (updateDistrictSP.selectedItem.equals(getString(R.string.select_district))) {
                        updateMandalSP.isClickable = false
                        updateVillageSP.isClickable = false
                    } else {
                        updateMandalSP.isClickable = true
                        viewModel.getMandalList(districtID!!)
                    }

                }

            }

        updateMandalSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    mandalID = mandalModel[pos]._id
                    if (updateMandalSP.selectedItem.equals(getString(R.string.select_mandal))) {
                        updateVillageSP.isClickable = false
                    } else {
                        updateVillageSP.isClickable = true
                        viewModel.getVillageList(mandalID!!)
                    }

                }

            }

        updateVillageSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    updateVillageSP.isClickable =
                        !updateCountrySP.selectedItem.equals(getString(R.string.select_country))
                    villageID = villageModel[pos]._id
                }

            }

        updateBloodGroupSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    bloodGroup = bloodGroupModel[pos].name
                }

            }

        updateMaritalStatusSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    maritalStatus = maritalModel[pos].name
                }

            }

        updateEmploymentStateSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                    employmentState = employmentModel[pos].name

                    if (employmentState.equals("Private")) {
                        updatePrivateWorkTL.visibility = View.VISIBLE
                    } else {
                        updatePrivateWorkTL.visibility = View.GONE
                    }

                    if (employmentState.equals("Others")) {
                        updateOtherWorkTL.visibility = View.VISIBLE
                    } else {
                        updateOtherWorkTL.visibility = View.GONE
                    }
                }

            }

        updateDateOfBirthET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s!!.isNotEmpty()) {// save the last char value
                    mLastChar = s.elementAt(s.length - 1);
                } else {
                    mLastChar = '0'
                }
            }

            override fun onTextChanged(s: CharSequence?, count: Int, before: Int, start: Int) {


                updateDateOfBirthET.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL) mKeyDel = 1
                    false
                })
                if (mKeyDel == 0) {
                    if ((updateDateOfBirthET.text!!.length) == 2 || (updateDateOfBirthET.text!!.length) == 5) {
                        updateDateOfBirthET.setText(updateDateOfBirthET.text.toString() + "-")
                        updateDateOfBirthET.setSelection(updateDateOfBirthET.text!!.length)
                    }
                    mTextValue = updateDateOfBirthET.text.toString()
                } else {
                    mTextValue = updateDateOfBirthET.text.toString()
                    if (mLastChar == '-') {
                        mTextValue = mTextValue!!.substring(0, mTextValue!!.length - 1)
                        updateDateOfBirthET.setText(mTextValue)
                        updateDateOfBirthET.setSelection(mTextValue!!.length)
                    }
                    mKeyDel = 0
                }


            }

        })

        updateFemaleRB.setOnCheckedChangeListener { compoundButton, isFemale ->
            updateMaleRB.isChecked = !isFemale
            if (updateFemaleRB.isChecked) {
                gender = requireActivity().getString(R.string.female)
            }
        }

        updateMaleRB.setOnCheckedChangeListener { compoundButton, isMale ->
            updateFemaleRB.isChecked = !isMale

            if (updateMaleRB.isChecked) {
                gender = requireActivity().getString(R.string.male)
            }
        }


        frontAadharCL.setOnClickListener {
            isProfileImageUpload = false
            isFrontImageUpload = true
            requestPermission()
        }
        backAadharCL.setOnClickListener {
            isProfileImageUpload = false
            isFrontImageUpload = false
            requestPermission()
        }
        uploadPhotoCL.setOnClickListener {
            isProfileImageUpload = true
            requestPermission()
        }
        updateProfileBT.setOnClickListener {
            if (validation()) {
                if (employmentState.equals("Others")) {
                    employmentState=updateOtherWorkET.text.toString()
                }
                //Do editProfile
                val main = JSONObject()
                try {
                    main.put("aadhar_number", updateAadharCardNumberTV.text.toString())
                    main.put("full_name", updateFullNameET.text.toString())
                    main.put("profile_url", profileUrl)
                    main.put("parent_name", updateParentNameET.text.toString())
                    main.put("DOB", updateDateOfBirthET.text.toString())
                    main.put("blood_group", bloodGroup)
                    main.put("marital_status", maritalStatus)
                    main.put("employment_state", employmentState)
                    main.put("caste", casteID)
                    main.put("gender", gender)
                    main.put("country", countryId)
                    main.put("state", stateID)
                    main.put("district", districtID)
                    main.put("mandal", mandalID)
                    main.put("village", villageID)
                    main.put("address", updateAddressET.text.toString())
                    main.put("others", updateOtherWorkET.text.toString())
                    main.put("mention_work", updatePrivateWorkET.text.toString())
                    main.put("aadhar_card_front", aadharCardFrontUrl)
                    main.put("aadhar_card_back", aadharCardBackUrl)
                    main.put("pincode", updatePinCodeET.text.toString().toInt())
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    openUpdateChangesAlertDialog(gsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                showSnackbar(errorMsg)
            }

        }


        viewModel.getCountryList()
        viewModel.getCasteList()
    }


    private fun openUpdateChangesAlertDialog(gsonObject: JsonObject) {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.update_changes_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                editProfileViewModel.doUpdateProfile(gsonObject)
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {

            STORAGE_CAMERA_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()) {

                    var granted = true

                    for (result in grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED)
                            granted = false
                    }

                    if (granted) {
                        showSelector()
                    } else {
                        showSnackbar("External storage permission not granted!")
                    }

                } else {
                    showSnackbar("External storage permission not granted!")
                }
            }
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
                intent.action = Intent.ACTION_PICK
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
        imageBitmap = data!!.extras!!["data"] as Bitmap?
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

    private fun gallery(data: Intent) {
        imageBitmap = MediaStore.Images.Media.getBitmap(
            requireActivity().applicationContext.contentResolver,
            data.data
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

    private fun uploadFile() {
        //uploadImage
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file!!.getName(), requestFile)
        viewModel.uploadFile(body)
    }

    private fun addBloodGroup() {
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

    private fun addEmploymentStatus() {
        employmentModel.clear()
        employmentModel.add(BaseItem("Select Employment Status"))
        employmentModel.add(BaseItem("Government"))
        employmentModel.add(BaseItem("Private"))
        employmentModel.add(BaseItem("Business"))
        employmentModel.add(BaseItem("Others"))
        employmentStateNameList.clear()
        for (i in 0 until employmentModel.size) {
            employmentStateNameList.add(employmentModel[i].name!!)
        }
    }

    private fun validation(): Boolean {

        var formOk = true
         if (!FormValidator.requiredField(updateFullNameET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_name)

        } else if (!FormValidator.requiredField(updateParentNameET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_parent_name)

        } else if (updateCountrySP.selectedItem.equals(getString(R.string.select_country))) {
            formOk = false
            errorMsg = getString(R.string.select_country)
        } else if (updateStateSP.selectedItem.equals(getString(R.string.select_state))) {
            formOk = false
            errorMsg = getString(R.string.select_state)
        } else if (updateDistrictSP.selectedItem.equals(getString(R.string.select_district))) {
            formOk = false
            errorMsg = getString(R.string.select_district)
        } else if (updateMandalSP.selectedItem.equals(getString(R.string.select_mandal))) {
            formOk = false
            errorMsg = getString(R.string.select_mandal)
        } else if (updateVillageSP.selectedItem.equals(getString(R.string.select_village))) {
            formOk = false
            errorMsg = getString(R.string.select_village)
        } else if (!FormValidator.requiredField(updateAddressET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_address)

        } else if (!FormValidator.requiredField(updatePinCodeET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_pincode)

        } else if (!FormValidator.equalField(updatePinCodeET.text.toString(), 6)) {
            formOk = false
            errorMsg = getString(R.string.invalid_pincode)

        } else if (!FormValidator.requiredField(updateDateOfBirthET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_dob)

        } else if (updateBloodGroupSP.selectedItem.equals("Select blood group")) {
            formOk = false
            errorMsg = getString(R.string.select_blood_group)
        } else if (updateMaritalStatusSP.selectedItem.equals("Select Marital Status")) {
            formOk = false
            errorMsg = getString(R.string.select_marital_status)
        } else if (updateEmploymentStateSP.selectedItem.equals("Select Employment Status")) {
            formOk = false
            errorMsg = getString(R.string.select_employment_status)
        } else if (updateEmploymentStateSP.selectedItem.equals("Private") && !FormValidator.requiredField(
                updatePrivateWorkET.text.toString(),
                1
            )
        ) {
            formOk = false
            errorMsg = getString(R.string.enter_work)

        } else if (updateEmploymentStateSP.selectedItem.equals("Others") && !FormValidator.requiredField(
                updateOtherWorkET.text.toString(),
                1
            )
        ) {
            formOk = false
            errorMsg = getString(R.string.enter_work)

        }  else if (!updateMaleRB.isChecked && !updateFemaleRB.isChecked) {
            formOk = false
            errorMsg = getString(R.string.select_gender)
        }
        return formOk
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EditProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun initObservers() {

        viewModel.countryLiveData.observe(this, Observer {
            if (it.country!!.any()) {
                countryModel.clear()
                countryNameList.clear()
                countryModel.add(CountryItem("0", getString(R.string.select_country)))
                countryModel.addAll(it.country!!)
                for (i in 0 until countryModel.size) {
                    countryNameList.add(countryModel[i].country!!)
                }
                countryAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, countryNameList)
                countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown)

                if(updateCountrySP != null) {
                    updateCountrySP.adapter = countryAdapter
                    for (i in 0 until countryModel.size) {
                        if (MyCommunityApp.getUser(requireActivity())!!.country!!._id.equals(countryModel[i]._id)) { updateCountrySP.setSelection(i) }
                    }
                }
            }
        })

        viewModel.stateLiveData.observe(this, Observer {

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
                updateStateSP.adapter = stateAdapter
                for (i in 0 until stateModel.size) {
                    if (MyCommunityApp.getUser(requireActivity())!!.state!!._id.equals(stateModel[i]._id)) {
                        updateStateSP.setSelection(i)
                    }
                }
            }
        })

        viewModel.districtLiveData.observe(this, Observer {
            if (it.district!!.any()) {
                districtModel.clear()
                districtNameList.clear()
                districtModel.add(DistrictItem("0", getString(R.string.select_district)))
                districtModel.addAll(it.district!!)
                for (i in 0 until districtModel.size) {
                    districtNameList.add(districtModel[i].district!!)
                }
            }

            districtAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, districtNameList)
            districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
            updateDistrictSP.adapter = districtAdapter
            for (i in 0 until districtModel.size) {
                if (MyCommunityApp.getUser(requireActivity())!!.district!!._id.equals(districtModel[i]._id)) {
                    updateDistrictSP.setSelection(i)
                }
            }
        })


        viewModel.mandalLiveData.observe(this, Observer {
            if (it.mandals!!.any()) {
                mandalModel.clear()
                mandalNameList.clear()
                mandalModel.add(MandalItem("0", getString(R.string.select_mandal)))
                mandalModel.addAll(it.mandals!!)
                for (i in 0 until mandalModel.size) {
                    mandalNameList.add(mandalModel[i].mandal!!)
                }
            }

            mandalAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, mandalNameList)
            mandalAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
            updateMandalSP.adapter = mandalAdapter
            for (i in 0 until mandalModel.size) {
                if (MyCommunityApp.getUser(requireActivity())!!.mandal!!._id.equals(mandalModel[i]._id)) {
                    updateMandalSP.setSelection(i)
                }
            }
        })

        viewModel.villageLiveData.observe(this, Observer {
            if (it.village!!.any()) {
                villageModel.clear()
                villageNameList.clear()
                villageModel.addAll(it.village!!)
                for (i in 0 until villageModel.size) {
                    villageNameList.add(villageModel[i].village!!)
                }

                villageAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, villageNameList)
                villageAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                updateVillageSP.adapter = villageAdapter
                for (i in 0 until villageModel.size) {
                    if (MyCommunityApp.getUser(requireActivity())!!.village!!._id.equals(
                            villageModel[i]._id
                        )
                    ) {
                        updateVillageSP.setSelection(i)
                    }
                }
            }
        })

        viewModel.casteLiveData.observe(this, Observer {
            if (it.community!!.any()) {
                casteModel.clear()
                casteModel.addAll(it.community!!)
                updateCasteTV.text = MyCommunityApp.getUser(requireContext())!!.caste!!.caste
            }

        })

        viewModel.fileUploadLiveData.observe(this, Observer {
            if (it != null) {
                if (isProfileImageUpload!!) {
                    profileIV.load(it.media_file!![0].fileurl)
                    profileUrl = it.media_file!![0].fileurl
                } else {
                    if (isFrontImageUpload!!) {
                        frontAadhaarCardIV.visibility = VISIBLE
                        frontAadhaarCardIV.load(it.media_file!![0].fileurl)
                        aadharCardFrontUrl = it.media_file!![0].fileurl
                    } else {
                        backAadhaarCardIV.visibility = VISIBLE
                        backAadhaarCardIV.load(it.media_file!![0].fileurl)
                        aadharCardBackUrl = it.media_file!![0].fileurl
                    }
                }

            }
        })

        editProfileViewModel.updateProfileLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar(it.message!!)
                listener.onBack()
                requireActivity().viewModelStore.clear()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunityStore){
            listener = context as CommunityStore
        }
    }
}