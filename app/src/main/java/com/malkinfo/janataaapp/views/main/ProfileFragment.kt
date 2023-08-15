package com.malkinfo.janataaapp.views.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.viewmodels.BaseViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import com.malkinfo.janataaapp.views.launch.LaunchActivity
import de.hdodenhof.circleimageview.CircleImageView

import org.json.JSONException


class ProfileFragment : MyBaseFragment() {

    private lateinit var profileInterface: CommunityStore

    /**set id*/
    private lateinit var profileIV: CircleImageView
    private lateinit var profileNameTV:TextView
    private lateinit var mobileNumberTV:TextView
    private lateinit var editProfileCL:ConstraintLayout
    private lateinit var idCardCL:ConstraintLayout
    private lateinit var communitySettingCL:ConstraintLayout
    private lateinit var matrimonySettingCL:ConstraintLayout
    private lateinit var bloodGroupSettingCL:ConstraintLayout
    private lateinit var view3:View
    private lateinit var ratingCL:ConstraintLayout
    private lateinit var shareAppCL:ConstraintLayout
    private lateinit var deleteUserCL:ConstraintLayout
    private lateinit var logoutCL:ConstraintLayout
    private lateinit var versionTV:TextView
    private lateinit var privacyLL: LinearLayout
    private lateinit var termsconditionsLL: LinearLayout

    private val profileViewModel: BaseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BaseViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intinsId(view)
        setUpLoader(profileViewModel)
        initViews(view)
    }
    private fun intinsId(v:View){
        profileIV = v.findViewById(R.id.profileIV)
        profileNameTV = v.findViewById(R.id.profileNameTV)
        mobileNumberTV = v.findViewById(R.id.mobileNumberTV)
        editProfileCL = v.findViewById(R.id.editProfileCL)
        idCardCL = v.findViewById(R.id.idCardCL)
        communitySettingCL = v.findViewById(R.id.communitySettingCL)
        matrimonySettingCL = v.findViewById(R.id.matrimonySettingCL)
        bloodGroupSettingCL = v.findViewById(R.id.bloodGroupSettingCL)
        view3 = v.findViewById(R.id.view3)
        ratingCL = v.findViewById(R.id.ratingCL)
        shareAppCL = v.findViewById(R.id.shareAppCL)
        deleteUserCL = v.findViewById(R.id.deleteUsersCL)
        logoutCL = v.findViewById(R.id.logoutCL)
        versionTV = v.findViewById(R.id.versionTV)
        privacyLL = v.findViewById(R.id.privacyLL)
        termsconditionsLL = v.findViewById(R.id.termsconditionsLL)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        profileViewModel.deactivateLiveData.observe(this, Observer {
            if(it!=null){
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_LOGGEDIN,false)
                sharedPrefManager.clearPreference()
                //requireActivity().finish()
                loadLaunchActivity()
            }
        })
        profileViewModel.deleteLiveDataUser.observe(this, Observer {
            if(it!=null){
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_LOGGEDIN,false)
                sharedPrefManager.clearPreference()
                //requireActivity().finish()
                loadLaunchActivity()
            }
        })
    }

    private fun initViews(view: View) {


        if(MyCommunityApp.getUser(requireActivity())!!.profile_url.isNullOrEmpty()) {
            profileIV.load(R.drawable.profile_iv)
        }
        else{
            profileIV.load(MyCommunityApp.getUser(requireActivity())!!.profile_url)
        }

        profileNameTV.text=MyCommunityApp.getUser(requireActivity())!!.full_name
        mobileNumberTV.text=MyCommunityApp.getUser(requireActivity())!!.mobile

        editProfileCL.setOnClickListener {
            profileInterface.onEditProfileClicked()
        }

        idCardCL.setOnClickListener {
            profileInterface.onIdentityCardClicked(false)
        }


        if (sharedPrefManager
                .getBooleanPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE)
        ) {
            communitySettingCL.visibility=VISIBLE

        }
        else{
            communitySettingCL.visibility= GONE
        }
         if (sharedPrefManager
                .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE)
        ) {
            matrimonySettingCL.visibility=VISIBLE

        }else{
             matrimonySettingCL.visibility= GONE
         }
         if (sharedPrefManager
                .getBooleanPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE)
        ) {
            bloodGroupSettingCL.visibility=VISIBLE

        }else{
             bloodGroupSettingCL.visibility= GONE
         }

        if ((!sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE)&&(!sharedPrefManager
                .getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE)&&(!sharedPrefManager
                .getBooleanPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE))))){
            view3.visibility= GONE
        }else{
            view3.visibility=VISIBLE
        }

        communitySettingCL.setOnClickListener {
            profileInterface.onIndividualSettingsClicked("COMMUNITY_SETTINGS")
        }

        matrimonySettingCL.setOnClickListener {
            profileInterface.onIndividualSettingsClicked("MATRIMONY_SETTINGS")
        }

        bloodGroupSettingCL.setOnClickListener {
            profileInterface.onIndividualSettingsClicked("BLOODGROUP_SETTINGS")
        }

        ratingCL.setOnClickListener {}

        shareAppCL.setOnClickListener {

        }
        privacyLL.setOnClickListener {
            val url = "https://adminjanataa.blogspot.com/2023/06/jantaa-application-privacy-policy.html"

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        termsconditionsLL.setOnClickListener {
            val url = "https://adminjanataa.blogspot.com/2023/06/jantaa-application-terms-conditions.html"

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }



        deleteUserCL.setOnClickListener {
            //Do Deactivate
           // val main = JSONObject()
            try {
                //main.put("status","Deactive" )
               // val jsonParser = JsonParser()
               // val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                showDeleteAlert()

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        logoutCL.setOnClickListener {
            showConfirmation(getString(R.string.no),
                getString(R.string.yes),
                "",
                getString(R.string.logout_alert),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    sharedPrefManager.setPreference(MyCommunityAppConstants.IS_LOGGEDIN,false)
                    //requireActivity().finish()
                    sharedPrefManager.clearPreference()
                   loadLaunchActivity()
                })
        }

        val  version = requireActivity().packageManager.getPackageInfo(
            requireActivity().packageName,
            0
        ).versionName
        versionTV.text = getString(R.string.version) + version

    }

    private fun loadLaunchActivity() {
        val intent = Intent(requireActivity(), LaunchActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showDeleteAlert() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.delete_user_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()

                profileViewModel.doDeleteUSerById(MyCommunityApp.getUser(requireActivity())!!._id!!)

            })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            profileInterface = context as CommunityStore
        }
    }
}