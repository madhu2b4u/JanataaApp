package com.malkinfo.janataaapp.views.main.Profile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.views.base.MyBaseFragment



class ProfileSettingsFragment : MyBaseFragment() {

    private lateinit var profileSettingSInterface: CommunityStore

    /**set Id*/
    private lateinit var backIV:ImageView
    private lateinit var headerTV:TextView
    private lateinit var matrimonyEditProfileCL:ConstraintLayout
    private lateinit var matrimonyShortlistedCL:ConstraintLayout
    private lateinit var matrimonyBlockedProfileCL:ConstraintLayout
    private lateinit var matrimonyPartnerPreferenceCL:ConstraintLayout
    private lateinit var view2:View
    private lateinit var view1:View
    private lateinit var view3:View
    private lateinit var view4:View
    private lateinit var removeAccountCL:ConstraintLayout

    companion object {

        @JvmStatic
        fun newInstance(title: String) =
            ProfileSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.SETTINGS_TITLE, title)
                }
            }
    }


    private var strHeaderTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                strHeaderTitle =
                    requireArguments().getString(MyCommunityAppConstants.SETTINGS_TITLE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniId(view)
        initViews(view)
    }
    private fun iniId(v:View){
        backIV = v.findViewById(R.id.backIV)
        headerTV = v.findViewById(R.id.headerTV)
        matrimonyEditProfileCL = v.findViewById(R.id.matrimonyEditProfileCL)
        matrimonyShortlistedCL = v.findViewById(R.id.matrimonyShortlistedCL)
        matrimonyBlockedProfileCL = v.findViewById(R.id.matrimonyBlockedProfileCL)
        matrimonyPartnerPreferenceCL = v.findViewById(R.id.matrimonyPartnerPreferenceCL)
        view2 = v.findViewById(R.id.view2)
        view1 = v.findViewById(R.id.view1)
        view3 = v.findViewById(R.id.view3)
        view4 = v.findViewById(R.id.view4)
        removeAccountCL = v.findViewById(R.id.removeAccountCL)
    }

    override fun onErrorCalled(it: String?) {

    }

    override fun initObservers() {

    }

    private fun initViews(view: View) {

        backIV.setOnClickListener {
            requireActivity().onBackPressed()


        }

        when (strHeaderTitle) {
            "COMMUNITY_SETTINGS" -> {
                headerTV.text = getText(R.string.community_chat_settings)
                matrimonyEditProfileCL.visibility= GONE
                matrimonyShortlistedCL.visibility= GONE
                matrimonyBlockedProfileCL.visibility= GONE
                matrimonyPartnerPreferenceCL.visibility= GONE
                view2.visibility= GONE
                view1.visibility= GONE
                view2.visibility= GONE
                view3.visibility= GONE
                view4.visibility= GONE

                removeAccountCL.setOnClickListener {
                    openRemoveCommunityChatAccountDialog()
                }
            }
            "MATRIMONY_SETTINGS" -> {
                headerTV.text = getText(R.string.matrimony_settings)

                matrimonyShortlistedCL.visibility= VISIBLE
                matrimonyBlockedProfileCL.visibility= VISIBLE

                view2.visibility= VISIBLE
                view3.visibility= VISIBLE
                view4.visibility= VISIBLE

                if(sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_REGISTERED_USER)){
                    matrimonyEditProfileCL.visibility= VISIBLE
                    view1.visibility= VISIBLE
                }else{
                    matrimonyEditProfileCL.visibility= GONE
                    view1.visibility= GONE
                }

                if(sharedPrefManager.getBooleanPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED)){
                    matrimonyPartnerPreferenceCL.visibility= VISIBLE
                    view2.visibility= VISIBLE
                }else{
                    matrimonyPartnerPreferenceCL.visibility= GONE
                    view2.visibility= GONE
                }

                matrimonyEditProfileCL.setOnClickListener {
                    profileSettingSInterface.onEditMatrimonyProfileClicked()
                }

                matrimonyShortlistedCL.setOnClickListener {
                    profileSettingSInterface.onViewShortListProfilesClicked()
                }

                matrimonyBlockedProfileCL.setOnClickListener {
                    profileSettingSInterface.onViewBlockedProfilesClicked()
                }

                removeAccountCL.setOnClickListener {
                    openRemoveMatrimonyAccountDialog()
                }

                matrimonyPartnerPreferenceCL.setOnClickListener {
                    profileSettingSInterface.onEditPartnerPreferenceClicked(true)
                }
            }
            "BLOODGROUP_SETTINGS" -> {
                headerTV.text = getText(R.string.blood_donation_settings)
                matrimonyEditProfileCL.visibility= GONE
                matrimonyShortlistedCL.visibility= GONE
                matrimonyBlockedProfileCL.visibility= GONE
                matrimonyPartnerPreferenceCL.visibility= GONE
                view2.visibility= GONE
                view1.visibility= GONE
                view2.visibility= GONE
                view3.visibility= GONE
                view4.visibility= GONE
                removeAccountCL.setOnClickListener {
                    openRemoveBloodDonationAccountDialog()
                }
            }
        }


    }

    private fun openRemoveMatrimonyAccountDialog() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.matrimony_remove_account_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                showSnackbar(getString(R.string.account_removed))
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE, false)
                requireActivity().onBackPressed()
            })
    }

    private fun openRemoveCommunityChatAccountDialog() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.community_chat_remove_account_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                showSnackbar(getString(R.string.account_removed))
                sharedPrefManager
                    .setPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE, false)
                requireActivity().onBackPressed()
            })
    }

    private fun openRemoveBloodDonationAccountDialog() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.blood_donation_remove_account_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                showSnackbar(getString(R.string.account_removed))
                sharedPrefManager.setPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE, false)
                requireActivity().onBackPressed()
            })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            profileSettingSInterface = context as CommunityStore
        }
    }
}