package com.malkinfo.janataaapp.views.launch

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.managers.utils.LaunchStore
import com.malkinfo.janataaapp.views.MainActivity
import com.malkinfo.janataaapp.views.base.MyAppCompatActivity



class LaunchActivity : MyAppCompatActivity(),LaunchStore{
    private var fragment: Fragment? = null
    private var fragmentManager: FragmentManager? = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        initUI()

    }

    private fun initUI() {
        Firebase.initialize(context = this)

        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
      loadLoginFragment()
    }

    override fun initObservers() {
        TODO("Not yet implemented")
    }

    override fun onErrorCalled(it: String?) {
        TODO("Not yet implemented")
    }


    override fun loadLoginFragment() {
        fragment = LoginFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.launchContainer, fragment as LoginFragment)
            ?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadGetOtpFragment(
        otpId: String?,
        mobileNumber: String
    ) {

        fragment = GetOtpFragment.newInstance(otpId,mobileNumber)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.launchContainer, fragment as GetOtpFragment)
            ?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadProfileDetailsFragment() {
        fragment = SignUpFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.launchContainer, fragment as SignUpFragment)
            ?.disallowAddToBackStack()
            ?.commit()
    }
    override fun loadIdentityCardFragment(fromSignUp: Boolean) {
        fragment = GetUserDetailsFragment.newInstance(fromSignUp)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.launchContainer, fragment as GetUserDetailsFragment)
            ?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadFeatureSelectionFragment() {
        fragment = FeatureSelectionFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.launchContainer, fragment as FeatureSelectionFragment)
            ?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginClicked(
        otpId: String?,
        mobileNumber: String,
    ) {
        loadGetOtpFragment(otpId ,mobileNumber)
    }

    override fun onVerifyOtp() {
        loadProfileDetailsFragment()
        //loadMainActivity()
    }

    override fun onGotoMainActivity() {
        loadMainActivity()
    }

    override fun profileUpdateClicked(isFromSignUp: Boolean) {
        loadIdentityCardFragment(isFromSignUp)
    }

    override fun onNextClicked() {
        loadFeatureSelectionFragment()
    }

    override fun onFeatureClicked() {
        loadMainActivity()
    }

}