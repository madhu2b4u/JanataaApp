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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.GroupMemberListAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.GroupMemberModel
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import java.io.IOException


class GroupMembersFragment : MyBaseFragment() {

    private var isSearch: Boolean? = null
    private var groupMembersModel: ArrayList<GroupMemberModel> = ArrayList()
    private var duplicateGroupMembersModel: ArrayList<GroupMemberModel> = ArrayList()
    private lateinit var groupMemberListAdapter: GroupMemberListAdapter
    var groupId: String? = null
    private lateinit var viewGroupMemberProfileInterface: CommunityStore

    private var totalLimit: Int? = null
    var page = 1
    var pageLimit = 10
    /**set Id*/
    private lateinit var groupMembersRV:RecyclerView
    private lateinit var searchMembersED:EditText
    private lateinit var searchIV:ImageView
    private lateinit var groupMembersBackIV:ImageView

    private val groupMembersCommunityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupId = it.getString(MyCommunityAppConstants.GROUP_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_members, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniId(view)
        setUpLoader(groupMembersCommunityViewModel)
        doGetGroupMembers()
    }
    private fun iniId(v:View){
        groupMembersRV = v.findViewById(R.id.groupMembersRV)
        searchMembersED = v.findViewById(R.id.searchMembersED)
        searchIV = v.findViewById(R.id.searchIV)
        groupMembersBackIV = v.findViewById(R.id.groupMembersBackIV)
    }

    private fun doGetGroupMembers() {
        groupMembersCommunityViewModel.doGetGroupMembers(groupId!!,page.toString(),pageLimit.toString())
    }


    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        groupMembersCommunityViewModel.groupMembersLiveMembersData.observe(this, Observer {
            if (it != null) {

                if(it.total_count!=null){
                    totalLimit = it.total_count
                }
                if (it.members!!.any()) {
                    if(page==1){
                        initViews()
                        groupMembersModel.clear()
                        groupMembersModel.addAll(it.members!!)
                        groupMemberListAdapter.notifyDataSetChanged()
                        duplicateGroupMembersModel.clear()
                        duplicateGroupMembersModel.addAll(groupMembersModel)
                    }else{
                        groupMembersModel.addAll(it.members!!)
                        groupMemberListAdapter.notifyDataSetChanged()
                    }

                }
            }
        })

        groupMembersCommunityViewModel.searchMembersLiveData.observe(this, Observer {
            if (it != null) {
                if (it.members!!.any()) {
                    groupMembersModel.clear()
                    groupMembersModel.addAll(it.members!!)
                    groupMemberListAdapter.notifyDataSetChanged()
                }
            }
        })
    }


    private fun initViews() {

        isSearch = false
        groupMembersRV.layoutManager = LinearLayoutManager(activity)
        groupMemberListAdapter = GroupMemberListAdapter(requireActivity(), groupMembersModel,totalLimit)
        groupMembersRV.adapter = groupMemberListAdapter

        groupMembersRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(isSearch==false) {
                    if (!groupMembersRV.canScrollVertically(1)) {
                        val limit: Int = totalLimit!! / pageLimit
                        if (page <= limit) {
                            page += 1
                            groupMembersCommunityViewModel.doGetGroupMembers(
                                groupId!!,
                                page.toString(),
                                pageLimit.toString()
                            )
                        }
                    }
                }
            }
        })

        groupMemberListAdapter.onViewMemberProfileClicked = { pos ->
            viewGroupMemberProfileInterface.onProfileClicked(groupMembersModel[pos]._id, groupId)
        }

        searchMembersED.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    if (s!!.isNotEmpty()) {
                        searchIV.setColorFilter(context!!.resources.getColor(R.color.textBlue))
                    } else {
                        searchIV.setColorFilter(context!!.resources.getColor(R.color.textGrey))
                        page=1
                        isSearch = false
                        groupMembersModel.clear()
                        groupMembersModel.addAll(duplicateGroupMembersModel)
                        groupMemberListAdapter.notifyDataSetChanged()
                    }
                } catch (e: IOException) {

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        searchMembersED.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm: InputMethodManager =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchMembersED.windowToken, 0)

                groupMembersCommunityViewModel.doSearchMembers(
                    searchMembersED.text.toString(),
                    "members",
                    groupId!!
                )
                isSearch = true
                true
            } else {
                false
            }
        }

        groupMembersBackIV.setOnClickListener {
           requireActivity().onBackPressed()
        }
    }



    companion object {

        @JvmStatic
        fun newInstance(groupId: String?) =
            GroupMembersFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            viewGroupMemberProfileInterface = context as CommunityStore
        }
    }
}