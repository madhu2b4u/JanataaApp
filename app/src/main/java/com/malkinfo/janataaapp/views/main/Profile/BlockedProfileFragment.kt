package com.malkinfo.janataaapp.views.main.Profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.BlockedProfileAdapter
import com.malkinfo.janataaapp.helpers.DividerItemDecorator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.BlockedProfileItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import java.util.ArrayList


class BlockedProfileFragment : MyBaseFragment() {

    private val blockedProfileModel: ArrayList<BlockedProfileItem> = ArrayList()
    private lateinit var blockedProfileAdapter: BlockedProfileAdapter
    private lateinit var listener : CommunityStore
    /**set id*/
    private lateinit var backIV:ImageView
    private lateinit var blockedProfileRV:RecyclerView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context  is CommunityStore){
            listener = context as CommunityStore
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            BlockedProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val blockedProfileViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_blocked_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsv(view)
        setUpLoader(blockedProfileViewModel)
        doGetBlockedProfile()

    }
    private fun initsv(v:View){
        backIV = v.findViewById(R.id.backIV)
        blockedProfileRV = v.findViewById(R.id.blockedProfileRV)

    }

    private fun doGetBlockedProfile() {
        blockedProfileViewModel.getBlockedProfile(
            MyCommunityApp.getUser(requireActivity())!!._id!!
        )

        backIV.setOnClickListener {
           listener.onBack()
            requireActivity().viewModelStore.clear()
        }
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        blockedProfileViewModel.blockedProfileLiveData.observe(this, Observer {
            if(it!=null){
                if(it.blocked_list!!.any()){
                    blockedProfileModel.clear()
                    blockedProfileModel.addAll(it.blocked_list!!)
                    initViews()
                    blockedProfileAdapter.notifyDataSetChanged()
                }
            }
        })

       /* blockedProfileViewModel.unBlockProfileLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar(it.message!!)
            }
        })*/
    }


    private fun initViews() {



        blockedProfileRV.layoutManager = LinearLayoutManager(requireActivity())
        blockedProfileAdapter = BlockedProfileAdapter(requireActivity(), blockedProfileModel)
        blockedProfileRV.adapter = blockedProfileAdapter
        blockedProfileRV.addItemDecoration(
            DividerItemDecorator(
                activity?.applicationContext!!,
                showFirstDivider = true,
                showLastDivider = false
            )
        )
        //blockedProfileAdapter.notifyDataSetChanged()

        blockedProfileAdapter.onUnBlockClicked = { pos ->
            blockedProfileViewModel.doUnBlockProfile(blockedProfileModel[pos]._id!!)
            blockedProfileModel.removeAt(pos)
            blockedProfileAdapter.notifyItemRemoved(pos)
            blockedProfileAdapter.notifyDataSetChanged()
        }
    }




}