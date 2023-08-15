package com.malkinfo.janataaapp.views.launch

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.CasteAdapter
import com.malkinfo.janataaapp.helpers.DividerItemDecorator
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.LaunchStore
import com.malkinfo.janataaapp.models.base.*
import com.malkinfo.janataaapp.models.BloodGroupItem
import com.malkinfo.janataaapp.models.Matrimony.CountryItem
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


class SignUpFragment : MyBaseFragment() {
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
    private var isFrontImageUpload: Boolean? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    protected val REQUEST_CODE_GALLERY = 2
    private val STORAGE_CAMERA_PERMISSION_CODE = 34
    var imageBitmap: Bitmap? = null
    var file: File? = null
    var profileUrl: String? = null
    var aadharCardFrontUrl: String? = null
    var aadharCardBackUrl: String? = null
    private var gender: String? = null
    private var employmentState: String? = null
    private var maritalStatus: String? = null
    private var bloodGroup: String? = null
    private var countryId: String? = null
    private var stateID: String? = null
    private var districtID: String? = null
    private var villageID: String? = null
    private var casteID: String? = null
    private lateinit var signUpInterface: LaunchStore
    private lateinit var casteAdapter: CasteAdapter
    private var casteNameList: ArrayList<String> = ArrayList()
    private lateinit var casteListDialog: Dialog
    private lateinit var dialog: Dialog
    var isCondition = false
    private lateinit var errorMsg: String
    private var countryNameList: ArrayList<String> = ArrayList()
    private var mandalNameList: ArrayList<String> = ArrayList()
    private var stateNameList: ArrayList<String> = ArrayList()
    private var districtNameList: ArrayList<String> = ArrayList()
    private var villageNameList: ArrayList<String> = ArrayList()
    private var countryModel: ArrayList<CountryItem> = ArrayList()
    private var stateModel: ArrayList<StateItem> = ArrayList()
    private var districtModel: ArrayList<DistrictItem> = ArrayList()
    private var mandalModel: ArrayList<MandalItem> = ArrayList()
    private var villageModel: ArrayList<VillageItem> = ArrayList()
    private var casteModel: ArrayList<CasteItem> = ArrayList()
    private var bloodGroupModel: ArrayList<BloodGroupItem> = ArrayList()
    private var bloodGroupNameList: ArrayList<String> = ArrayList()
    private var maritalNameList: ArrayList<String> = ArrayList()
    private var employmentStateNameList: ArrayList<String> = ArrayList()
    private var maritalModel: ArrayList<BaseItem> = ArrayList()
    private var employmentModel: ArrayList<BaseItem> = ArrayList()

    /**set Id*/
    private lateinit var bloodGroupSP: Spinner
    private lateinit var countrySP:Spinner
    private lateinit var signUpStateSP:Spinner
    private lateinit var casteTV:TextView
    private lateinit var districtSP:Spinner
    private lateinit var signUpMandalSP:Spinner
    private lateinit var tahsilVillageSP:Spinner
    private lateinit var profileIV: CircleImageView
    private lateinit var frontAadhaarCardIV:ImageView
    private lateinit var backAadhaarCardIV:ImageView
    private lateinit var maritalStatusSP:Spinner
    private lateinit var employmentStateSP:Spinner
    private lateinit var otherWorkTL: TextInputLayout
    private lateinit var privateWorkTL:TextInputLayout
    private lateinit var dobET: TextInputEditText
    private lateinit var termsAndConditionsCB:CheckBox
    private lateinit var submitProfileBT: MaterialButton
    private lateinit var femaleRB:RadioButton
    private lateinit var maleRB:RadioButton
    private lateinit var frontAadharCL: ConstraintLayout
    private lateinit var backAadharCL:ConstraintLayout
    private lateinit var uploadPhotoCL:ConstraintLayout
    private lateinit var aadharNumberET:TextInputEditText
    private lateinit var fullNameET:TextInputEditText
    private lateinit var parentNameET:TextInputEditText
    private lateinit var addressET:TextInputEditText
    private lateinit var otherWorkET:TextInputEditText
    private lateinit var privateWorkET:TextInputEditText
    private lateinit var pincodeET:TextInputEditText
    private lateinit var privacyTermsTV:TextView
    /**====================================================*/



    private val viewModel: BaseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BaseViewModel::class.java)
    }

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        //requestPermission()
        setUpLoader(viewModel)
        setUpLoader(userViewModel)

    }


    override fun initObservers() {

        viewModel.countryLiveData.observe(this, Observer {
            if (it.country!!.any()) {
                countryModel.clear()
                countryNameList.clear()
                countryModel.add(CountryItem("0",getString(R.string.select_country)))
                countryModel.addAll(it.country!!)
                for (i in 0 until countryModel.size) {
                    countryNameList.add(countryModel[i].country!!)
                    Log.d("mTag","countryNameList = $countryNameList")
                }
                countryAdapter =
                    ArrayAdapter(requireActivity(), R.layout.spinner_item, countryNameList)
                countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                countrySP.adapter = countryAdapter
            }
            Log.d("mTag","I am exit countryLiveData")
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
                signUpStateSP.adapter = stateAdapter

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

                districtAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, districtNameList)
                districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                districtSP.adapter = districtAdapter

            }
        })

        viewModel.signUpMandalLiveData.observe(this, Observer {
            if (it.mandals!!.any()) {
                mandalModel.clear()
                mandalNameList.clear()
                mandalModel.add(MandalItem("0", getString(R.string.select_mandal)))
                mandalModel.addAll(it.mandals!!)

                for (i in 0 until mandalModel.size) {
                    mandalNameList.add(mandalModel[i].mandal!!)
                }

                mandalAdapter =
                    ArrayAdapter(requireActivity(), R.layout.spinner_item, mandalNameList)
                mandalAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                signUpMandalSP.adapter = mandalAdapter
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

                villageAdapter =
                    ArrayAdapter(requireActivity(), R.layout.spinner_item, villageNameList)
                villageAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
                tahsilVillageSP.adapter = villageAdapter

            }
        })

        viewModel.casteLiveData.observe(this, Observer {
            if (it.community!!.any()) {
                casteModel.clear()
                casteModel.addAll(it.community!!)
                casteTV.text = getString(R.string.select_caste)
            }

        })

        viewModel.fileUploadLiveData.observe(this, Observer {
            if (it != null) {
                if (isProfileImageUpload!!) {
                    profileIV.load(it.media_file!![0].fileurl)
                    profileUrl = it.media_file!![0].fileurl
                } else {

                    if(isFrontImageUpload!!){
                        frontAadhaarCardIV.visibility= VISIBLE
                        frontAadhaarCardIV.load(it.media_file!![0].fileurl)
                        aadharCardFrontUrl= it.media_file!![0].fileurl
                    }else{
                        backAadhaarCardIV.visibility= VISIBLE
                        backAadhaarCardIV.load(it.media_file!![0].fileurl)
                        aadharCardBackUrl= it.media_file!![0].fileurl
                    }
                }

            }
        })

        userViewModel.signUpLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar(it.message!!)
                signUpInterface.profileUpdateClicked(true)
            }
        })
    }

    private fun initViews(view: View) {

        /**call all id*/
        bloodGroupSP = view.findViewById(R.id.bloodGroupSP)
        countrySP = view.findViewById(R.id.countrySP)
        signUpStateSP = view.findViewById(R.id.signUpStateSP)
        casteTV = view.findViewById(R.id.casteTV)
        districtSP = view.findViewById(R.id.districtSP)
        signUpMandalSP = view.findViewById(R.id.signUpMandalSP)
        tahsilVillageSP = view.findViewById(R.id.tahsilVillageSP)
        profileIV = view.findViewById(R.id.profileIV)
        frontAadhaarCardIV = view.findViewById(R.id.frontAadhaarCardIV)
        backAadhaarCardIV = view.findViewById(R.id.backAadhaarCardIV)
        maritalStatusSP = view.findViewById(R.id.maritalStatusSP)
        employmentStateSP = view.findViewById(R.id.employmentStateSP)
        otherWorkTL = view.findViewById(R.id.otherWorkTL)
        privateWorkTL = view.findViewById(R.id.privateWorkTL)
        dobET = view.findViewById(R.id.dobET)
        termsAndConditionsCB = view.findViewById(R.id.termsAndConditionsCB)
        submitProfileBT = view.findViewById(R.id.submitProfileBT)
        femaleRB = view.findViewById(R.id.femaleRB)
        maleRB = view.findViewById(R.id.maleRB)
        frontAadharCL = view.findViewById(R.id.frontAadharCL)
        backAadharCL = view.findViewById(R.id.backAadharCL)
        uploadPhotoCL = view.findViewById(R.id.uploadPhotoCL)
        aadharNumberET = view.findViewById(R.id.aadharNumberET)
        fullNameET = view.findViewById(R.id.fullNameET)
        parentNameET = view.findViewById(R.id.parentNameET)
        addressET = view.findViewById(R.id.addressET)
        privateWorkET = view.findViewById(R.id.privateWorkET)
        otherWorkET = view.findViewById(R.id.otherWorkET)
        pincodeET = view.findViewById(R.id.pincodeET)
        privacyTermsTV = view.findViewById(R.id.privacyTermsTV)
        privacyTermsTV.setMovementMethod(LinkMovementMethod.getInstance());
        viewModel.getCountryList()
        viewModel.getCasteList()
        addBloodGroup()
        addMaritalStatus()
        addEmploymentStatus()


        val bloodGroupAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, bloodGroupNameList)
        bloodGroupAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        bloodGroupSP.adapter = bloodGroupAdapter


        val employmentStateAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, employmentStateNameList)
        employmentStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        employmentStateSP.adapter = employmentStateAdapter


        val maritalStateAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, maritalNameList)
        maritalStateAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        maritalStatusSP.adapter = maritalStateAdapter


        countrySP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                countryId = countryModel[pos]._id
                if (countrySP.selectedItem.equals(getString(R.string.select_country))) {
                    signUpStateSP.isClickable = false
                    districtSP.isClickable = false
                    signUpMandalSP.isClickable = false
                    tahsilVillageSP.isClickable = false
                } else {
                    stateNameList.clear()
                    districtNameList.clear()
                    mandalNameList.clear()
                    villageNameList.clear()
                    signUpStateSP.adapter = null
                    districtSP.adapter = null
                    signUpMandalSP.adapter = null
                    tahsilVillageSP.adapter = null
                    signUpStateSP.isClickable = true
                    viewModel.getStateList(countryId!!)
                }
            }

        }

        signUpStateSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                stateID = stateModel[pos]._id
                if (signUpStateSP.selectedItem.equals(getString(R.string.select_state))) {
                    districtSP.isClickable = false
                    signUpMandalSP.isClickable = false
                    tahsilVillageSP.isClickable = false
                } else {
                    districtNameList.clear()
                    mandalNameList.clear()
                    villageNameList.clear()
                    districtSP.adapter = null
                    signUpMandalSP.adapter = null
                    tahsilVillageSP.adapter = null
                    districtSP.isClickable = true
                    viewModel.getDistrictList(stateID!!)
                }
            }

        }

        districtSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    districtID = districtModel[pos]._id
                    if (districtSP.selectedItem == getString(R.string.select_district)) {
                        signUpMandalSP.isClickable = false
                        tahsilVillageSP.isClickable = false
                    } else {
                        mandalNameList.clear()
                        villageNameList.clear()
                        signUpMandalSP.adapter = null
                        tahsilVillageSP.adapter = null
                        signUpMandalSP.isClickable = true
                        viewModel.getSignupMandalList(districtID!!)
                    }

                }

            }

        signUpMandalSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                mandalID = mandalModel[pos]._id
                if (signUpMandalSP.selectedItem == getString(R.string.select_mandal)) {
                    tahsilVillageSP.isClickable = false
                } else {
                    villageNameList.clear()
                    tahsilVillageSP.adapter = null
                    tahsilVillageSP.isClickable = true
                    viewModel.getVillageList(mandalID!!)
                }
            }

        }

        tahsilVillageSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    tahsilVillageSP.isClickable =
                        !countrySP.selectedItem.equals(getString(R.string.select_country))
                    villageID = villageModel[pos]._id
                }

            }


        bloodGroupSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    bloodGroup = bloodGroupModel[pos].name
                }

            }

        maritalStatusSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    maritalStatus = maritalModel[pos].name
                }

            }

        employmentStateSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                    employmentState = employmentModel[pos].name

                    if (employmentStateSP.selectedItem == "Others") {
                        otherWorkTL.visibility = VISIBLE
                    } else {
                        otherWorkTL.visibility = GONE
                    }


                    if (employmentStateSP.selectedItem == "Private") {
                        privateWorkTL.visibility = VISIBLE
                    } else {
                        privateWorkTL.visibility = GONE
                    }


                }

            }

        dobET.addTextChangedListener(object : TextWatcher {
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


                dobET.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL) mKeyDel = 1
                    false
                })
                if (mKeyDel == 0) {
                    if ((dobET.text!!.length) == 2 || (dobET.text!!.length) == 5) {
                        dobET.setText(dobET.text.toString() + "-")
                        dobET.setSelection(dobET.text!!.length)
                    }
                    mTextValue = dobET.text.toString()
                } else {
                    mTextValue = dobET.text.toString()
                    if (mLastChar == '-') {
                        mTextValue = mTextValue!!.substring(0, mTextValue!!.length - 1)
                        dobET.setText(mTextValue)
                        dobET.setSelection(mTextValue!!.length)
                    }
                    mKeyDel = 0
                }


            }

        })

        termsAndConditionsCB.setOnCheckedChangeListener { _p, isTermsAndCondition ->
            if (isTermsAndCondition) {
                submitProfileBT.alpha = 0.9F
                submitProfileBT.isClickable = true
            } else {
                submitProfileBT.alpha = 0.5F
                submitProfileBT.isClickable = false
            }
        }

        femaleRB.setOnCheckedChangeListener { compoundButton, isFemale ->
            maleRB.isChecked = !isFemale
            if (femaleRB.isChecked) {
                gender = requireActivity().getString(R.string.female)
            }
        }

        maleRB.setOnCheckedChangeListener { compoundButton, isMale ->
            femaleRB.isChecked = !isMale

            if (maleRB.isChecked) {
                gender = requireActivity().getString(R.string.male)
            }
        }

        casteTV.setOnClickListener {
            openCasteListDialog()
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

        submitProfileBT.setOnClickListener {

            if (validation()) {

                //Do SignUp
                val main = JSONObject()
                try {
                    main.put("aadhar_number", aadharNumberET.text.toString())
                    main.put("full_name", fullNameET.text.toString())
                    main.put("profile_url", profileUrl)
                    main.put("parent_name", parentNameET.text.toString())
                    main.put("DOB", dobET.text.toString())
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
                    main.put("address", addressET.text.toString())
                    main.put("others", otherWorkET.text.toString())
                    main.put("mention_work", privateWorkET.text.toString())
                    main.put("aadhar_card_front", aadharCardFrontUrl)
                    main.put("aadhar_card_back", aadharCardBackUrl)
                    main.put("pincode", pincodeET.text.toString().toInt())
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    userViewModel.doSignUp(gsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                showSnackbar(errorMsg)
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    private fun openCasteListDialog() {

        val caste_layout = LayoutInflater.from(requireActivity())
            .inflate(R.layout.caste_dailog_layout,null)
        val casteRV = caste_layout.findViewById<RecyclerView>(R.id.casteRV)
        casteListDialog = Dialog(requireActivity())
        casteListDialog.setContentView(caste_layout)
        casteListDialog.show()

        casteAdapter = CasteAdapter(context, casteModel)
        casteRV.layoutManager = LinearLayoutManager(context)
        casteRV.adapter = casteAdapter
        casteRV.isNestedScrollingEnabled = false
        casteRV.addItemDecoration(
            DividerItemDecorator(
                activity?.applicationContext!!,
                showFirstDivider = false,
                showLastDivider = false
            )
        )
        casteAdapter.notifyDataSetChanged()

        casteAdapter.onCasteRadioButtonClicked = { pos ->

                for (i in 0 until casteModel.size) {
                    if (i == pos) {
                        // casteModel[i].isChecked=!casteModel[i].isChecked!!
                        casteModel[i].isChecked = true
                        casteListDialog.dismiss()
                    } else {
                        if (casteModel[i].isChecked!!) {
                            casteModel[i].isChecked = false
                        }
                    }
                }

            casteTV.text = casteModel[pos].caste
            casteID = casteModel[pos]._id
        }
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
        employmentModel.add(BaseItem(getString(R.string.select_employment_status)))
        employmentModel.add(BaseItem("Government"))
        employmentModel.add(BaseItem("Private"))
        employmentModel.add(BaseItem("Business"))
        employmentModel.add(BaseItem("Others"))
        employmentStateNameList.clear()
        for (i in 0 until employmentModel.size) {
            employmentStateNameList.add(employmentModel[i].name!!)
        }
    }

    override fun onErrorCalled(it: String?) {
        if (it != null) {
            showSnackbar(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LaunchStore) {

            signUpInterface = context as LaunchStore
        }
    }

    private fun validation(): Boolean {

        var formOk = true
        if (!FormValidator.requiredField(aadharNumberET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_aadhar_number)

        } else if (!FormValidator.equalField(aadharNumberET.text.toString(), 12)) {
            formOk = false
            errorMsg = getString(R.string.invalid_aadhar_number)

        } else if (!FormValidator.requiredField(fullNameET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_name)

        } else if (!FormValidator.requiredField(parentNameET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_parent_name)

        } else if (countrySP==null || countrySP.selectedItem.equals(getString(R.string.select_country))) {
            formOk = false
            errorMsg = getString(R.string.select_country)
        } else if (signUpStateSP.selectedItem==null || signUpStateSP.selectedItem.equals(getString(R.string.select_state))) {
            formOk = false
            errorMsg = getString(R.string.select_state)
        } else if (districtSP.selectedItem==null || districtSP.selectedItem.equals(getString(R.string.select_district))) {
            formOk = false
            errorMsg = getString(R.string.select_district)
        } else if (signUpMandalSP.selectedItem==null || signUpMandalSP.selectedItem.equals(getString(R.string.select_mandal))) {
            formOk = false
            errorMsg = getString(R.string.select_mandal)
        } else if (tahsilVillageSP.selectedItem==null || tahsilVillageSP.selectedItem.equals(getString(R.string.select_village))) {
            formOk = false
            errorMsg = getString(R.string.select_village)
        } else if (!FormValidator.requiredField(addressET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_address)

        } else if (!FormValidator.requiredField(pincodeET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_pincode)

        } else if (!FormValidator.equalField(pincodeET.text.toString(), 6)) {
            formOk = false
            errorMsg = getString(R.string.invalid_pincode)

        } else if (!FormValidator.requiredField(dobET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_dob)

        } else if (bloodGroupSP==null || bloodGroupSP.selectedItem.equals("Select blood group")) {
            formOk = false
            errorMsg = getString(R.string.select_blood_group)
        } else if (maritalStatusSP==null || maritalStatusSP.selectedItem.equals("Select Marital Status")) {
            formOk = false
            errorMsg = getString(R.string.select_marital_status)
        } else if (employmentStateSP==null || employmentStateSP.selectedItem.equals("Select Employment Status")) {
            formOk = false
            errorMsg = getString(R.string.select_employment_status)
        } else if (employmentStateSP==null || employmentStateSP.selectedItem.equals("Private") && !FormValidator.requiredField(privateWorkET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.enter_work)

        } else if (employmentStateSP==null || employmentStateSP.selectedItem.equals("Others") && !FormValidator.requiredField(
                otherWorkET.text.toString(),
                1
            )
        ) {
            formOk = false
            errorMsg = getString(R.string.enter_work)
        } else if (casteTV.text == "Select Caste") {
            formOk = false
            errorMsg = getString(R.string.select_caste)
        } else if (!maleRB.isChecked && !femaleRB.isChecked) {
            formOk = false
            errorMsg = getString(R.string.select_gender)
        }
        else if(aadharCardFrontUrl.isNullOrEmpty()){
            formOk = false
            errorMsg=getString(R.string.upload_front_side_aadhar_card)
        }
        else if(aadharCardBackUrl.isNullOrEmpty()){
            formOk = false
            errorMsg=getString(R.string.upload_back_side_aadhar_card)
        }
        else if (termsAndConditionsCB.isChecked == false) {
            formOk = false
            errorMsg = getString(R.string.select_terms_and_condition)
        }
        return formOk
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}