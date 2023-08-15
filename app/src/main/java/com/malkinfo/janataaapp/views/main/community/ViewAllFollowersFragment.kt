package com.malkinfo.janataaapp.views.main.community

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.FollowerAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.FollowerModel
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import java.io.IOException

class ViewAllFollowersFragment : MyBaseFragment() {

    private var isSearch: Boolean? = null
    private var followerModel: ArrayList<FollowerModel> = ArrayList()
    private var duplicateFollowerModel: ArrayList<FollowerModel> = ArrayList()
    private lateinit var followerAdapter: FollowerAdapter
    var profileId: String? = null
    var groupId: String? = null
    var profileName: String? = null
    private lateinit var viewAllFollowerInterface: CommunityStore

    private var totalLimit: Int? = null
    var page = 1
    var pageLimit = 10

    /**set ID*/
    private lateinit var titleNameTV:TextView
    private lateinit var followersRV:RecyclerView
    private lateinit var searchFollowerED:EditText
    private lateinit var searchIV:ImageView
    private lateinit var follBackIV:ImageView

    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance(id: String?, groupId: String?, profileName: String?) =
            ViewAllFollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.PROFILE_ID, id)
                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                    putString(MyCommunityAppConstants.PROFILE_NAME, profileName)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                groupId = it.getString(MyCommunityAppConstants.GROUP_ID)
                profileId = it.getString(MyCommunityAppConstants.PROFILE_ID)
                profileName = it.getString(MyCommunityAppConstants.PROFILE_NAME)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsId(view)
        setUpLoader(communityViewModel)
        doGetFollowers()
    }
    private fun initsId(v:View){
        titleNameTV = v.findViewById(R.id.titleNameTV)
        followersRV = v.findViewById(R.id.followersRV)
        searchFollowerED = v.findViewById(R.id.searchFollowerED)
        searchIV = v.findViewById(R.id.searchIV)
        follBackIV = v.findViewById(R.id.follBackIV)
    }

    private fun doGetFollowers() {
        communityViewModel.doGetFollowers(profileId!!,page.toString(),pageLimit.toString())
    }



    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {

        communityViewModel.followerLiveData.observe(this, Observer {
            if (it != null) {
                if(it.followers_count!=null){
                    totalLimit= it.followers_count
                }
                if (it.followers!!.any()) {
                    if(page==1){
                        initViews()
                        followerModel.clear()
                        followerModel.addAll(it.followers!!)
                        followerAdapter.notifyDataSetChanged()
                        duplicateFollowerModel.clear()
                        duplicateFollowerModel.addAll(followerModel)
                    }else{
                        followerModel.addAll(it.followers!!)
                        followerAdapter.notifyDataSetChanged()
                    }
                }
            }
        })



        communityViewModel.searchFollowerLiveData.observe(this, Observer {
            if (it != null) {
                if (it.followers!!.any()) {
                    followerModel.clear()
                    followerModel.addAll(it.followers!!)
                    followerAdapter.notifyDataSetChanged()
                }
            }
        })


    }


    private fun initViews() {

        titleNameTV.text = profileName

        isSearch = false

        followerAdapter = FollowerAdapter(requireActivity(), followerModel,totalLimit)
        followersRV.layoutManager = LinearLayoutManager(requireActivity())
        followersRV.adapter = followerAdapter
        followerAdapter.notifyDataSetChanged()

        followersRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!followersRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / pageLimit
                    if (page <= limit) {
                        page += 1
                        communityViewModel.doGetFollowers(profileId!!,page.toString(),pageLimit.toString())

                    }
                }
            }
        })

        followerAdapter.onViewFollowersProfileClicked = { pos ->
            viewAllFollowerInterface.onProfileClicked(followerModel[pos]._id, groupId)
        }


        searchFollowerED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    if (s!!.isNotEmpty()) {
                        searchIV.setColorFilter(context!!.resources.getColor(R.color.textBlue))
                    } else {
                        searchIV.setColorFilter(context!!.resources.getColor(R.color.textGrey))
                        page=1
                        isSearch = false
                        followerModel.clear()
                        followerModel.addAll(duplicateFollowerModel)
                        followerAdapter.notifyDataSetChanged()
                    }
                } catch (e: IOException) {

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        searchFollowerED.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm: InputMethodManager =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchFollowerED.windowToken, 0)

                communityViewModel.doSearchFollowers(
                    searchFollowerED.text.toString(),
                    profileId!!,
                    "followers",
                    groupId!!
                )
                isSearch = true
                true
            } else {
                false
            }
        }

        follBackIV.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            viewAllFollowerInterface = context as CommunityStore
        }
    }
}