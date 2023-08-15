package com.malkinfo.janataaapp.views.main.matrimony

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.ChatAdapter
import com.malkinfo.janataaapp.helpers.DividerItemDecorator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.ChatListItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App ChatListFragment. Here Some Update.
 * ------------------------------------------
 */

class ChatListFragment : MyBaseFragment() {


    private lateinit var chatInterface: CommunityStore
    /**set Id*/
    private lateinit var chatListBackIV:ImageView
    private lateinit var noConversationCL:ConstraintLayout
    private lateinit var chatRV:RecyclerView


    private lateinit var chatAdapter: ChatAdapter
    private var chatListItem: ArrayList<ChatListItem> = ArrayList()

    private val chatViewModel: MatrimonyViewModel by lazy {
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
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniIds(view)
        setUpLoader(chatViewModel)
        getChatList()
    }
    private fun iniIds(v:View){
        chatListBackIV = v.findViewById(R.id.chatListBackIV)
        noConversationCL = v.findViewById(R.id.noConversationCL)
        chatRV = v.findViewById(R.id.chatRV)
    }

    private fun getChatList() {

        chatListItem.clear()
        chatViewModel.getChatList()

        chatListBackIV.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onErrorCalled(it: String?) {
        if(it!=null){
            showSnackbar(it)
        }
    }


    override fun initObservers() {
        chatViewModel.chatListLiveData.observe(this, Observer {
            if(isAdded) {
                if (it != null) {
                    if (it.users!!.any()) {
                        chatListItem.clear()
                        chatListItem.addAll(it.users!!)
                        initViews()
                        chatAdapter.notifyDataSetChanged()
                        noConversationCL.visibility = GONE
                    } else {
                        noConversationCL.visibility = VISIBLE
                        chatRV.visibility = GONE
                    }
                }
            }
        })
    }

    private fun initViews() {

        chatRV.layoutManager = LinearLayoutManager(activity)
        chatAdapter = ChatAdapter(requireActivity(), chatListItem)
        chatRV.adapter = chatAdapter
        chatRV.addItemDecoration(DividerItemDecorator(activity?.applicationContext!!, showFirstDivider = true, showLastDivider = false))

        chatAdapter.onItemClicked = {pos->
            chatInterface.onOpenChatClicked(chatListItem[pos]._id , chatListItem[pos].profile_url , chatListItem[pos].full_name)
        }


    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ChatListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            chatInterface = context as CommunityStore
        }
    }


}