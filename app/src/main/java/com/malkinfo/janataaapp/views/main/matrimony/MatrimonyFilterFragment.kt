package com.malkinfo.janataaapp.views.main.matrimony

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.MatchProfileFilterAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.MatchProfileItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment



class MatrimonyFilterFragment : MyBaseFragment()  {


    private var filterType: String? = null
    private var matchProfileFilterModel: ArrayList<MatchProfileItem> = ArrayList()
    private lateinit var matchProfileFilterAdapter: MatchProfileFilterAdapter
    private lateinit var matrimonyFilterInterface: CommunityStore
    private var totalLimit: Int? = null
    var page = 1
    var pageLimit = 10
    private lateinit var listener: CommunityStore
    /**set ID*/
    private lateinit var filterHeadTV:TextView
    private lateinit var matrimonyFilterBackIV:ImageView
    private lateinit var noMatchFoundTV:TextView
    private lateinit var matrimonyFilterRV:RecyclerView



    companion object {

        @JvmStatic
        fun newInstance(filterType: String) =
            MatrimonyFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.IS_MATRIMONY_FILTER_TYPE, filterType)
                }
            }
    }

    private val matrimonyFilterViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filterType =
                it.getString(MyCommunityAppConstants.IS_MATRIMONY_FILTER_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrimony_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsID(view)
        setUpLoader(matrimonyFilterViewModel)
        page = 1
        doGetFilterProfile()
    }
    private fun initsID(v:View){
        filterHeadTV = v.findViewById(R.id.filterHeadTV)
        matrimonyFilterBackIV = v.findViewById(R.id.matrimonyFilterBackIV)
        noMatchFoundTV = v.findViewById(R.id.noMatchFoundTV)
        matrimonyFilterRV = v.findViewById(R.id.matrimonyFilterRV)

    }

    private fun doGetFilterProfile() {

        matrimonyFilterViewModel.doDiscoverMatchFilterProfile(MyCommunityApp.getPartnerPreference(requireActivity())!!._id!!,
            filterType!!,
            page.toString(),
            pageLimit.toString()
        )

        when (filterType) {
            "location" -> {
                filterHeadTV.text = "Preferred Location Matches"
            }
            "occupation" -> {
                filterHeadTV.text = "Preferred Profession Matches"
            }
            "star" -> {
                filterHeadTV.text = "Preferred Star Matches"
            }
            "education" -> {
                filterHeadTV.text = "Preferred Education Matches"
            }
        }

        matrimonyFilterBackIV.setOnClickListener {
            listener.onBack()
            requireActivity().viewModelStore.clear()
        }


    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        matrimonyFilterViewModel.matchProfileFilterLiveData.observe(this, Observer {
            if(isAdded) {
                if (it != null) {
                    if (it.total_count != null) {
                        totalLimit = it.total_count
                        if (page == 1) {
                            initViews()
                        }
                    }
                    if (it.user != null) {
                        if (it.user!!.any()) {
                            if (page == 1) {

                                matchProfileFilterModel.clear()
                                matchProfileFilterModel.addAll(it.user!!)
                                matchProfileFilterAdapter.notifyDataSetChanged()
                            } else {
                                matchProfileFilterModel.addAll(it.user!!)
                                matchProfileFilterAdapter.notifyDataSetChanged()
                            }
                            noMatchFoundTV.visibility = GONE
                        } else {
                            noMatchFoundTV.visibility = VISIBLE
                        }
                    }
                }
            }
        })

        matrimonyFilterViewModel.shortListLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar(it.message!!)
                matchProfileFilterAdapter.notifyDataSetChanged()
            }
        })
        matrimonyFilterViewModel.blockMatchProfileLiveData.observe(this, Observer {
            if (isAdded) {
                if (it != null) {
                 //   showSnackbar("Never Show Again")
                    matchProfileFilterAdapter.notifyDataSetChanged()
                }
            }
        })
    }


    private fun initViews() {


        matchProfileFilterAdapter =
            MatchProfileFilterAdapter(requireActivity(), matchProfileFilterModel, totalLimit)
        matrimonyFilterRV.layoutManager = LinearLayoutManager(requireActivity())
        matrimonyFilterRV.adapter = matchProfileFilterAdapter


        matrimonyFilterRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!matrimonyFilterRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / pageLimit
                    if (page <= limit) {
                        page += 1
                        matrimonyFilterViewModel.doDiscoverMatchFilterProfile(MyCommunityApp.getPartnerPreference(requireActivity())!!._id!!,
                            filterType!!,
                            page.toString(),
                            pageLimit.toString()
                        )

                    }
                }
            }
        })

        matchProfileFilterAdapter.onShortlistClicked = { pos ->
            matrimonyFilterViewModel.doShortlist(matchProfileFilterModel[pos]._id!!)
            matchProfileFilterModel[pos].is_shortlisted =
                !matchProfileFilterModel[pos].is_shortlisted!!

        }

        matchProfileFilterAdapter.onItemClicked = { pos ->
            matrimonyFilterInterface.onPartnerProfileClicked(
                matchProfileFilterModel[pos]._id!!,
                matchProfileFilterModel[pos].profile_url, matchProfileFilterModel[pos].full_name
            )
        }

        matchProfileFilterAdapter.onChatNowClicked = { pos ->
            matrimonyFilterInterface.onOpenChatClicked(
                matchProfileFilterModel[pos]._id,
                matchProfileFilterModel[pos].profile_url, matchProfileFilterModel[pos].full_name
            )
        }

        matchProfileFilterAdapter.onDontShowClicked = { pos ->
            matrimonyFilterViewModel.doBlockMatchProfile(matchProfileFilterModel[pos]._id!!)
            matchProfileFilterModel.removeAt(pos)
            matchProfileFilterAdapter.notifyItemRemoved(pos)
            matchProfileFilterAdapter.notifyDataSetChanged()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            matrimonyFilterInterface = context as CommunityStore
        }
        if (context is CommunityStore) {
            listener = context as CommunityStore
        }
    }



}