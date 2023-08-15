package com.malkinfo.janataaapp.views.launch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.google.android.material.button.MaterialButton
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.LaunchStore
import com.malkinfo.janataaapp.models.launch.User
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView




class GetUserDetailsFragment : MyBaseFragment() {

    private lateinit var identityCardInterface: LaunchStore
    var profileImageUrl: String? = null
    var registrationNumber: String? = null
    var name: String? = null
    var caste: String? = null
    var mobileNumber: String? = null
    var bloodGroup: String? = null
    var address: String? = null
    var isFromSignUp: Boolean? = null
    lateinit var user: User
    /**set id*/
    private lateinit var identityCardIV: CircleImageView
    private lateinit var registrationNumberTV:TextView
    private lateinit var registrationNameTV:TextView
    private lateinit var casteLogoIV: ImageView
    private lateinit var registerCasteTV:TextView
    private lateinit var mobileTV:TextView
    private lateinit var bloodGroupTV:TextView
    private lateinit var addressTV:TextView
    private lateinit var idHeaderTV:TextView
    private lateinit var detailsBackIV:ImageView
    private lateinit var idTitleTV:TextView
    private lateinit var nextBT:MaterialButton
    private lateinit var idCardHeaderTV:TextView
    companion object {
        @JvmStatic
        fun newInstance(fromSignUp: Boolean) =
            GetUserDetailsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(MyCommunityAppConstants.IS_FROM_SIGN_UP, fromSignUp)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if (arguments != null) {
                isFromSignUp = it.getBoolean(MyCommunityAppConstants.IS_FROM_SIGN_UP)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {


    }

    private fun initViews(v:View) {
        /**call all id*/
        identityCardIV = v.findViewById(R.id.identityCardIV)
        registrationNumberTV = v.findViewById(R.id.registrationNumberTV)
        registrationNameTV = v.findViewById(R.id.registrationNameTV)
        casteLogoIV = v.findViewById(R.id.casteLogoIV)
        registerCasteTV = v.findViewById(R.id.registerCasteTV)
        mobileTV = v.findViewById(R.id.mobileTV)
        bloodGroupTV = v.findViewById(R.id.bloodGroupTV)
        addressTV = v.findViewById(R.id.addressTV)
        idHeaderTV = v.findViewById(R.id.idHeaderTV)
        detailsBackIV = v.findViewById(R.id.detailsBackIV)
        idTitleTV = v.findViewById(R.id.idTitleTV)
        nextBT = v.findViewById(R.id.nextBT)
        idCardHeaderTV  = v.findViewById(R.id.idCardHeaderTV)

        user=MyCommunityApp.getUser(requireActivity())!!
        if( user.profile_url.isNullOrEmpty()){
         identityCardIV.load(R.drawable.profile_iv)
        }else {
            identityCardIV.load(user.profile_url)
        }
        registrationNumberTV.text = user.registration_number
        registrationNameTV.text = user.full_name
        casteLogoIV.load(MyCommunityApp.getUser(requireActivity())!!.caste!!.image_url)
        registerCasteTV.text = user.caste!!.caste
        mobileTV.text = user.mobile
        bloodGroupTV.text = user.blood_group
        addressTV.text = user.address+","+"\n"+user.village!!.village+","+"\n"+user.district!!.district+","+"\n"+user.state!!.state+"-"+"\n"+user.pincode+"."

        if (isFromSignUp!!) {

            idHeaderTV.visibility = VISIBLE
            idTitleTV.visibility = VISIBLE
            nextBT.visibility = VISIBLE
            detailsBackIV.visibility = GONE
            v.findViewById<TextView>(R.id.idCardHeaderTV).visibility = GONE
            
        } else {
            idHeaderTV.visibility = GONE
            idTitleTV.visibility = GONE
            nextBT.visibility = GONE
            detailsBackIV.visibility = VISIBLE
            idCardHeaderTV.visibility = VISIBLE

        }

        nextBT.setOnClickListener {
            identityCardInterface.onNextClicked()
        }

        detailsBackIV.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LaunchStore) {
            identityCardInterface = context as LaunchStore
        }
    }


}