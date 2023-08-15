package com.malkinfo.janataaapp.views.launch

import MyCommunityApp
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.viewmodels.LaunchViewModel
import com.malkinfo.janataaapp.views.MainActivity
import com.malkinfo.janataaapp.views.base.MyAppCompatActivity
import org.json.JSONException
import org.json.JSONObject


class SplashActivity : MyAppCompatActivity() {

    private val viewModel: LaunchViewModel by lazy {
        ViewModelProvider(this).get(LaunchViewModel::class.java)
    }
    var fireDataIns: FirebaseDatabase? = null
    var database: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpLoader(viewModel)
        setContentView(R.layout.activity_splash)
        Firebase.initialize(context = this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

        initUI()

    }



    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)

    }


    override fun initObservers() {
        viewModel.renewTokenLiveData.observe(this, Observer {

            if (SharedPrefManager.getInstance(this)
                    .getBooleanPreference(MyCommunityAppConstants.IS_SIGNUP)) {

                fireDataIns = FirebaseDatabase.getInstance()
                database = fireDataIns!!.getReference(MyCommunityAppConstants.FIRE_USER_DATA)
                val user_id = MyCommunityApp.getUser(this)!!._id

                SharedPrefManager.getInstance(this).setPreference(MyCommunityAppConstants.AUTH_TOKEN,it.token)
                database!!.child(user_id!!).child("token").setValue(it.token)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this,LaunchActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun initUI() {
        Handler().postDelayed({

            if (SharedPrefManager.getInstance(this)
                    .getBooleanPreference(MyCommunityAppConstants.IS_LOGGEDIN)) {

                val main = JSONObject()
                try {

                    main.put(
                        "token",sharedPrefManager.getPreference(MyCommunityAppConstants.REFRESH_TOKEN)
                    )


                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject

                    viewModel.doGetRefreshToken(gsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                val intent = Intent(this,LaunchActivity::class.java)
                startActivity(intent)
                finish()
            }
        },WAIT_TIME)
    }


}