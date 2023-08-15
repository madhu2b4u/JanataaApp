package com.malkinfo.janataaapp.managers.utils

interface LaunchStore {
    fun loadLoginFragment()
    fun loadGetOtpFragment(
        otpId: String?,
        mobileNumber: String
    )
    fun loadProfileDetailsFragment()
    fun loadIdentityCardFragment(fromSignUp: Boolean)
    fun loadFeatureSelectionFragment()
    fun loadMainActivity()
    fun onLoginClicked(
        otpId: String?,
        mobileNumber: String,
    )
    fun onVerifyOtp()
    fun onGotoMainActivity()
    fun profileUpdateClicked(isFromSignUp: Boolean)
    fun onNextClicked()
    fun onFeatureClicked()
}