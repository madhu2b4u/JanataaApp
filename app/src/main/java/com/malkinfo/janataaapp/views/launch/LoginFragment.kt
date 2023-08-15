package com.malkinfo.janataaapp.views.launch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malkinfo.janataaapp.R
import com.google.firebase.messaging.FirebaseMessaging

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.utils.LaunchStore
import com.malkinfo.janataaapp.models.firebaseuser.FireUser
import com.malkinfo.janataaapp.models.firenotification.FirebaseNotificationData
import com.malkinfo.janataaapp.models.firenotification.FirebasePushNotification
import com.malkinfo.janataaapp.utitlis.firenoti.FirebaseServiceNotify
import com.malkinfo.janataaapp.viewmodels.LaunchViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONException
import org.json.JSONObject


class LoginFragment : MyBaseFragment() {
    private lateinit var errorMsg: String
    private lateinit var mobileNumber: String
    private lateinit var loginInterface: LaunchStore
    private lateinit var sendOtpBT : MaterialButton
    private lateinit var phoneNumberED: EditText
    private  var user_id :String? = null
    var database: FirebaseDatabase? = null
    var mDatabase: DatabaseReference? = null
    var auth: FirebaseAuth? = null
    var token :String? = null


    private val viewModel: LaunchViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LaunchViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader(viewModel)
        initViews(view)
    }

    private fun initViews(v:View) {
        sendOtpBT = v.findViewById(R.id.sendOtpBT)
        phoneNumberED = v.findViewById(R.id.phoneNumberED)
        mobileNumber=phoneNumberED.text.toString()
        database = FirebaseDatabase.getInstance()
        mDatabase = database!!.getReference(MyCommunityAppConstants.FIRE_USER_DATA)
        auth = FirebaseAuth.getInstance()

        sendOtpBT.setOnClickListener {
            val mobNum = phoneNumberED.text.toString()
            if (mobNum.startsWith("0") || mobNum.startsWith("+91")){
                phoneNumberED.error = "Please remove Country Code"

            }else{
                mobileNumber=phoneNumberED.text.toString()
                verifyMobile()
            }
        }

    }
    private fun verifyMobile(){
        if (validation()) {
            //Do Login
            val main = JSONObject()
            try {
                main.put("mobile",mobileNumber)
                val jsonParser = JsonParser()
                val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                viewModel.doLogin(gsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else {
            showSnackbar(errorMsg)
        }
    }


    override fun initObservers() {
        viewModel.renewTokenLiveData.observe(this, Observer {
            if (it != null){
                if (it.token != null){

                    val main = JSONObject()
                    main.put("status","Active" )
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    viewModel.doActivate(user_id!!,gsonObject)
                }else{
                   // Log.d("mTag","token is null")
                }

        }

        })

        viewModel.loginLiveData.observe(this, Observer {

            if (it != null) {
                //showSnackbar(it.message!!)
               loginInterface.onLoginClicked(it.otp_id, mobileNumber)

            }
        })
        var fireToken = ""
        FirebaseServiceNotify.sharedPref = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            fireToken = token
            FirebaseServiceNotify.token = token
        }.addOnFailureListener { exception ->
            // Handle any errors that occur while retrieving the token
            // Log or display an error message to the user
        }
        viewModel.activateLiveData.observe(this, Observer {
            FirebasePushNotification(
                FirebaseNotificationData("Active Account",
                    "Your account has been activated by the admin, now you can access your account"),
                fireToken
            ).also {
                viewModel.sendFireNotification(it)
                showActiveAc()
            }
        })
    }


    override fun onErrorCalled(it: String?) {
        showErrorDialog(it!!)

    }
    private fun showActiveAc(){
        AlertDialog.Builder(requireActivity())
            .setTitle("Activated Account")
            .setCancelable(false)
            .setMessage(
                "Your account has been activated by the admin, now you can access your account"
            )
            .setPositiveButton("ok"){dismiss,_->
                phoneNumberED.setText("")
                dismiss.dismiss()
            }
            .create()
            .show()
    }
    private fun showErrorDialog(msg:String){
        database!!.reference.child(MyCommunityAppConstants.FIRE_USER_DATA).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snapshot1 in snapshot.children) {
                    val fireUser: FireUser? = snapshot1.getValue(FireUser::class.java)

                    if (fireUser!!.phoneNumber.equals(mobileNumber)) {
                        user_id = fireUser._id
                        token = fireUser.token
                       Log.d("mTag","is user Id = ${snapshot1.value}")
//                        }
                    }else{
                        Log.d("mTag"," i am else uid = ${FirebaseAuth.getInstance().uid}")
                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })

        Log.d("mTag","msg = $msg")
        SharedPrefManager.getInstance(requireActivity()).setPreference(MyCommunityAppConstants.AUTH_TOKEN,token)
        if (msg.toLowerCase() == "this account was deactivated by user. so kindly contact the admin".toLowerCase()){
            AlertDialog.Builder(requireActivity())
                .setTitle("Deactivated User")
                .setCancelable(false)
                .setMessage(
                    "$msg\n "
                )
                .setPositiveButton("ok"){dismiss,_->

                    if (user_id != null){

                        val main = JSONObject()
                        main.put("status","Active" )
                        val jsonParser = JsonParser()
                        val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                        viewModel.doActivate(user_id!!,gsonObject)
                    }

                    dismiss.dismiss()
                }
                .setNegativeButton("Cancel"){dismis,_->
                    dismis.dismiss()

                }
                .create()
                .show()
        }


    }


    private fun validation(): Boolean {

        var formOk = true

        if (!FormValidator.requiredField(phoneNumberED.text.toString(), 10)) {
            formOk = false
            errorMsg = getString(R.string.invalid_mobile_number)
        }
        return formOk
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LaunchStore) {
            loginInterface = context as LaunchStore
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }



}