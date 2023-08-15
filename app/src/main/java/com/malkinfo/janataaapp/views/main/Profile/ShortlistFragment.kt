package com.malkinfo.janataaapp.views.main.Profile

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
import com.malkinfo.janataaapp.adapters.homeadapter.ShortListProfileAdapter
import com.malkinfo.janataaapp.helpers.DividerItemDecorator
import com.malkinfo.janataaapp.models.Matrimony.ShortlistProfileItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import java.util.ArrayList


class ShortlistFragment : MyBaseFragment() {

    private val shortListProfileModel: ArrayList<ShortlistProfileItem> = ArrayList()
    private lateinit var shortListProfileAdapter: ShortListProfileAdapter

    /**set Id*/
    private lateinit var backIV:ImageView
    private lateinit var shortListProfileRV:RecyclerView
    /**=================================================*/


    companion object {
        @JvmStatic
        fun newInstance() =
            ShortlistFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private val shortlistProfileViewModel: MatrimonyViewModel by lazy {
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
        return inflater.inflate(R.layout.fragment_shortlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniId(view)
        setUpLoader(shortlistProfileViewModel)
        initViews(view)
    }
    private fun iniId(v:View){
        backIV = v.findViewById(R.id.backIV)
        shortListProfileRV = v.findViewById(R.id.shortListProfileRV)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        shortlistProfileViewModel.shortlistProfileLiveData.observe(this, Observer {
            if(it!=null){
                if(it.shortlisted_user!!.any()){
                    shortListProfileModel.clear()
                    shortListProfileModel.addAll(it.shortlisted_user!!)
                    shortListProfileAdapter.notifyDataSetChanged()
                }
            }
        })

        shortlistProfileViewModel.removeShortListLiveData.observe(this, Observer {
            if(it!=null){
                showSnackbar(it.message!!)
            }
        })
    }


    private fun initViews(view: View) {
        shortlistProfileViewModel.getShortlistProfile()
        backIV.setOnClickListener {
            requireActivity().onBackPressed()
        }

        shortListProfileAdapter = ShortListProfileAdapter(view.context, shortListProfileModel)
        shortListProfileRV.layoutManager = LinearLayoutManager(context)
        shortListProfileRV.adapter = shortListProfileAdapter
        shortListProfileRV.addItemDecoration(
            DividerItemDecorator(
                activity?.applicationContext!!,
                showFirstDivider = true,
                showLastDivider = false
            )
        )


        shortListProfileAdapter.onRemoveShortlistClicked={pos->
            shortlistProfileViewModel.doRemoveShortlist(shortListProfileModel[pos]._id!!)
            shortListProfileModel[pos].is_shortlisted = !shortListProfileModel[pos].is_shortlisted!!
            shortListProfileModel.removeAt(pos)
            shortListProfileAdapter.notifyItemRemoved(pos)
            shortListProfileAdapter.notifyDataSetChanged()

        }
    }

}