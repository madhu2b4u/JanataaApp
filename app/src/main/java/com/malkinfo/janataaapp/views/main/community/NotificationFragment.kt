package com.malkinfo.janataaapp.views.main.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.NotificationAdapter
import com.malkinfo.janataaapp.models.community.NotificationModel



class NotificationFragment : Fragment() {

    private var recentNotificationModel: ArrayList<NotificationModel> = ArrayList()
    private lateinit var recentNotificationAdapter: NotificationAdapter
    private var earlierNotificationModel: ArrayList<NotificationModel> = ArrayList()
    private lateinit var earlierNotificationAdapter: NotificationAdapter
    /**set id*/
    private lateinit var backIV:ImageView
    private lateinit var recentNotificationRV:RecyclerView
    private lateinit var earlierNotificationRV:RecyclerView


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
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsID(view)
        initViews(view)
    }
    private fun initsID(v:View){
        backIV = v.findViewById(R.id.backIV)
        recentNotificationRV = v.findViewById(R.id.recentNotificationRV)
        earlierNotificationRV = v.findViewById(R.id.earlierNotificationRV)
    }

    private fun initViews(view: View) {

        backIV.setOnClickListener {
            requireActivity().onBackPressed()
        }

        addRecentNotifications()
        addEarlierNotifications()

        recentNotificationAdapter = NotificationAdapter(requireActivity(), recentNotificationModel)
        recentNotificationRV.layoutManager = LinearLayoutManager(context)
        recentNotificationRV.adapter = recentNotificationAdapter
        recentNotificationAdapter.notifyDataSetChanged()


        earlierNotificationAdapter = NotificationAdapter(requireActivity(), earlierNotificationModel)
        earlierNotificationRV.layoutManager = LinearLayoutManager(context)
        earlierNotificationRV.adapter = earlierNotificationAdapter
        earlierNotificationAdapter.notifyDataSetChanged()


    }

    private fun addEarlierNotifications() {
        earlierNotificationModel.clear()
        earlierNotificationModel.add(
            NotificationModel(
                "Username",
                "liked your post.",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
        earlierNotificationModel.add(
            NotificationModel(
                "Username",
                "commented your post.",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
        earlierNotificationModel.add(
            NotificationModel(
                "Username",
                "shared your post..",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
        earlierNotificationModel.add(
            NotificationModel(
                "Username",
                "started following you",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
    }

    private fun addRecentNotifications() {
        recentNotificationModel.clear()
        recentNotificationModel.add(
            NotificationModel(
                "Username",
                "liked your post.",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
        recentNotificationModel.add(
            NotificationModel(
                "Username",
                "commented your post.",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
        recentNotificationModel.add(
            NotificationModel(
                "Username",
                "shared your post..",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
        recentNotificationModel.add(
            NotificationModel(
                "Username",
                "started following you",
                "Jun 01 at 11:59 AM",
                R.drawable.profile_iv
            )
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NotificationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}