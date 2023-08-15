package com.malkinfo.janataaapp.views.launch

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.LaunchStore
import com.malkinfo.janataaapp.viewmodels.LaunchViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class GetOtpFragment : MyBaseFragment() {
    var mKeyDel = 0
    private lateinit var errorMsg: String
    private var otpId: String? = null
    private var otpCode: String? = null
    private var mobileNumber: String? = null
    private  var verifyToken:ForceResendingToken? = null
    private lateinit var otpinterface: LaunchStore
    /**set id*/
    private lateinit var firstNumberED: EditText
    private lateinit var secondNumberED:EditText
    private lateinit var thirdNumberED:EditText
    private lateinit var fourthNumberED:EditText
    private lateinit var fifthNumberED:EditText
    private lateinit var lastNumberED:EditText
    private lateinit var resendOtpCL: ConstraintLayout
    private lateinit var verifyOtpBT: MaterialButton
    /**otp Firebase*/
    private var storedVerificationId :String = ""
    var queue: RequestQueue? = null
    /**===========================================*/
    /**set Layout*/
    private lateinit var otpLayoutCL:ConstraintLayout


    private val viewModel: LaunchViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LaunchViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            otpId = it.getString(MyCommunityAppConstants.OTP_ID)
            mobileNumber = it.getString(MyCommunityAppConstants.MOBILE_NUMBER)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLoader(viewModel)
        startPhoneNumberVerification(view,"+91$mobileNumber!!")

        initViews(view)
    }

    private fun initViews(v:View) {
        /**call all id*/
        firstNumberED = v.findViewById(R.id.firstNumberED)
        secondNumberED = v.findViewById(R.id.secondNumberED)
        thirdNumberED = v.findViewById(R.id.thirdNumberED)
        fourthNumberED = v.findViewById(R.id.fourthNumberED)
        fifthNumberED = v.findViewById(R.id.fifthNumberED)
        lastNumberED = v.findViewById(R.id.lastNumberED)
        resendOtpCL = v.findViewById(R.id.resendOtpCL)
        verifyOtpBT = v.findViewById(R.id.verifyOtpBT)

        queue = Volley.newRequestQueue(requireActivity());
        firstNumberED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (firstNumberED.text.length == 1) {
                    secondNumberED.requestFocus()
                } else if (firstNumberED.text.isEmpty()) {
                    firstNumberED.requestFocus()
                }
            }

        })
        secondNumberED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (secondNumberED.text.length == 1) {
                    thirdNumberED.requestFocus()
                } else if (secondNumberED.text.isEmpty()) {
                    secondNumberED.clearFocus()
                    firstNumberED.requestFocus()
                }
            }

        })
        thirdNumberED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, count: Int, p2: Int, p3: Int) {


                if (thirdNumberED.text.length == 1) {
                    fourthNumberED.requestFocus()
                } else if (thirdNumberED.text.isEmpty()) {
                    thirdNumberED.clearFocus()
                    secondNumberED.requestFocus()
                }
            }

        })
        fourthNumberED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (fourthNumberED.text.length == 1) {
                    fifthNumberED.requestFocus()
                } else if (fourthNumberED.text.isEmpty()) {
                    fourthNumberED.clearFocus()
                    thirdNumberED.requestFocus()
                }

            }

        })
        fifthNumberED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (fifthNumberED.text.length == 1) {
                    lastNumberED.requestFocus()
                } else if (fifthNumberED.text.isEmpty()) {
                    fifthNumberED.clearFocus()
                    fourthNumberED.requestFocus()
                }

            }

        })
        lastNumberED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (lastNumberED.text.isEmpty()) {
                    lastNumberED.clearFocus()
                    fifthNumberED.requestFocus()
                }
            }

        })


        //resend code
        resendOtpCL.setOnClickListener {
            if (mobileNumber != null && mobileNumber!!.isNotEmpty()) {
                val main = JSONObject()
                try {
                    main.put("mobile", mobileNumber)
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    viewModel.doResendOtp(gsonObject)
                    resendVerificationCode("+91$mobileNumber",verifyToken!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }

        verifyOtpBT.setOnClickListener {
            otpCode = firstNumberED.text.toString() + secondNumberED.text.toString() + thirdNumberED.text.toString() + fourthNumberED.text.toString() + fifthNumberED.text.toString() + lastNumberED.text.toString()
            completeVerification(otpCode!!)

        }


    }
    private fun startPhoneNumberVerification(v:View,phoneNumber: String) {
        otpLayoutCL = v.findViewById(R.id.otpLayoutCL)
       viewModel.isLoading.postValue(LoaderStatus.loading)
        // Force reCAPTCHA flow
        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true)
        val auth = PhoneAuthProvider.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        mAuth.setLanguageCode("eg")
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto-retrieval or instant verification is successful.
            // Proceed with credential.
            val code = credential.smsCode

            signInWithPhoneAuthCredential(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Verification failed.
            // Display error message to the user.
            Log.d("mTag","is failed msg = ${e.message!!}")
            showSnackbar(e.message!!)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            storedVerificationId = verificationId
            verifyToken = token


//
            otpLayoutCL.visibility = View.VISIBLE
            viewModel.isLoading.postValue(LoaderStatus.success)
            showSnackbar("OTP Sent your Mobile Successfully")

        }
    }





    override fun initObservers() {
        //resend code
        viewModel.resendOtpLiveData.observe(this, Observer {
            if (it != null) {
                otpId = it.otp_id
                showSnackbar(it.message!!)
                firstNumberED.setText("")
                secondNumberED.setText("")
                thirdNumberED.setText("")
                fourthNumberED.setText("")
                fifthNumberED.setText("")
                lastNumberED.setText("")
                firstNumberED.requestFocus()

            }
        })
        viewModel.validateOtpLiveData.observe(this, Observer {

            if (it != null) {
                if (sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_SIGNUP)) {
                    otpinterface.onGotoMainActivity()
                } else {
                    otpinterface.onVerifyOtp()
                }
            } else {
                showSnackbar("error")
            }

        })

    }
    private fun resendVerificationCode(
        phoneNumber: String,
        token: ForceResendingToken
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            requireActivity(),  // Activity (for callback binding)
            callbacks,  // OnVerificationStateChangedCallbacks
            token
        ) // ForceResendingToken from callbacks
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun completeVerification(code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId,code)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Verification successful.
                        // Proceed with login.
//                    otpinterface.onGotoMainActivity()
                        if (validation()) {
                            //Do ValidateOtp
                            val isOtpCode = "123456"
                            val main = JSONObject()
                            try {
                                main.put("id", otpId)
                                main.put("otp", isOtpCode)
                                Log.d("mTag","id =$otpId , otp = $isOtpCode ")
                                val jsonParser = JsonParser()
                                val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                                viewModel.doValidateOtp(gsonObject)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        } else {
                            showSnackbar(errorMsg)
                        }
                    } else {
                        // Verification failed.
                        // Display error message to the user.

                        if (validation()) {
                            //Do ValidateOtp
                            val isOtpCode = "987456"
                            val main = JSONObject()
                            try {
                                main.put("id", otpId)
                                main.put("otp", isOtpCode)
                                Log.d("mTag","id =$otpId , otp = $isOtpCode ")
                                val jsonParser = JsonParser()
                                val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                                viewModel.doValidateOtp(gsonObject)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        } else {
                            showSnackbar(errorMsg)
                        }

                    }
                }
        }catch (e:Exception){
            e.printStackTrace()

        }

    }


    override fun onErrorCalled(it: String?) {
        if (it != null)
            showSnackbar(it)
    }

    private fun validation(): Boolean {

        var formOk = true

        if (!FormValidator.requiredField(otpCode, 6)) {
            formOk = false
            errorMsg = getString(R.string.invalid_otp)

        }
        return formOk
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LaunchStore) {
            otpinterface = context as LaunchStore
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            otpId: String?,
            mobileNumber: String) =
            GetOtpFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.OTP_ID, otpId)
                    putString(MyCommunityAppConstants.MOBILE_NUMBER, mobileNumber)
                }
            }
    }

}