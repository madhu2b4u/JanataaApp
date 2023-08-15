package com.malkinfo.janataaapp.views.main.community

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.AllPostAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.ProfilePostModel
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment

class ViewAllPostFragment : MyBaseFragment() {


    private var currentPosition: Int? = null
    var profileId: String? = null
    var groupId: String? = null
    private var allPostModel: ArrayList<ProfilePostModel> =ArrayList()
    private  lateinit var allPostAdapter: AllPostAdapter
    private lateinit var allPostInterface : CommunityStore
    /**set ID*/
    private lateinit var allPostRV:RecyclerView
    private lateinit var allPostBackIV:ImageView
    var page = 1
    var strPageLimit = 10

    companion object {

        @JvmStatic
        fun newInstance(profileId: String?, groupId: String?) =
            ViewAllPostFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.PROFILE_ID, profileId)
                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                }
            }
    }


    private val viewAllPostCommunityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(this).get(CommunityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profileId = it.getString(MyCommunityAppConstants.PROFILE_ID)
            groupId = it.getString(MyCommunityAppConstants.GROUP_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsId(view)
        setUpLoader(viewAllPostCommunityViewModel)
        initViews(view)
    }
    private fun initsId(v:View){
        allPostRV = v.findViewById(R.id.allPostRV)
        allPostBackIV = v.findViewById(R.id.allPostBackIV)
    }

    override fun initObservers() {
        viewAllPostCommunityViewModel.profilePostsLiveData.observe(this, Observer {
            if(it!=null){
                if(it.posts!!.any()) {
                    allPostModel.clear()
                    allPostModel.addAll(it.posts!!)
                }
            }
        })

        viewAllPostCommunityViewModel.allPostLikePostLiveData.observe(this, Observer {
            showSnackbar(it.message!!)

        })

        viewAllPostCommunityViewModel.viewAllPostDeletePostLiveData.observe(this, Observer {
            if (it != null) {
                showSnackbar(it.message!!)
               // viewAllPostCommunityViewModel.doGetAllPost(groupId!!, profileId!!)
                allPostAdapter.notifyDataSetChanged()
            }
        })
    }


    private fun initViews(view: View) {
        viewAllPostCommunityViewModel.doGetAllPost(groupId!!,profileId!!)

        allPostRV.layoutManager= LinearLayoutManager(context)
        allPostAdapter = AllPostAdapter(view.context,allPostModel)
        allPostRV.adapter=allPostAdapter
        allPostAdapter.notifyDataSetChanged()

        allPostAdapter.onFavouriteClicked = { pos ->
            currentPosition = pos
            viewAllPostCommunityViewModel.doAllPostLikePost(allPostModel[pos]._id!!)
            allPostModel[pos].is_liked = !allPostModel[pos].is_liked!!

            if (allPostModel.get(currentPosition!!).is_liked!!) {
                allPostModel.get(currentPosition!!).likes_count =
                    allPostModel.get(currentPosition!!).likes_count!! + 1
                allPostAdapter.notifyDataSetChanged()
            } else {
                allPostModel.get(currentPosition!!).likes_count =
                    allPostModel.get(currentPosition!!).likes_count!! - 1

                allPostAdapter.notifyDataSetChanged()
            }
        }

        allPostAdapter.onItemClicked = { pos ->
            allPostInterface.onPostItemClicked(allPostModel[pos]._id)
        }
        allPostAdapter.onMoreOptionClicked = { pos, view ->

            if (MyCommunityApp.getUser(requireActivity())!!._id.equals(profileId)) {
                openPopupMenu(pos, view)
            } else {
                openReportPopupMenu(pos, view)
            }

        }

        allPostAdapter.onShareClicked={pos->

            //createDynamicLink(profilePostModel[pos]._id, profilePostModel[pos].description)

        }


        allPostBackIV.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }


    private fun openReportPopupMenu(pos: Int, view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_report ->
                    allPostInterface.onReportPostClicked(allPostModel[pos]._id!!)
            }
            true
        })
        popupMenu.show()

    }
    private fun openPopupMenu(pos: Int, view: View) {

        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menuInflater.inflate(R.menu.user_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit ->
                    allPostInterface.onEditPostClicked(
                        allPostModel[pos].group_id,
                        allPostModel[pos].description,
                        allPostModel[pos].image_url,allPostModel[pos]._id,
                        true
                    )

                R.id.action_delete ->
                    openDeleteAlertDialog(pos)
            }
            true
        })
        popupMenu.show()
    }

    private fun openDeleteAlertDialog(pos: Int) {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.delete_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                viewAllPostCommunityViewModel.doViewAllPostDelete(allPostModel[pos]._id!!)
            })

    }

//    private fun createDynamicLink(id: String?, description: String?) {
//        FirebaseDynamicLinks.getInstance().createDynamicLink()
//            .setLink(Uri.parse("https://eniniyakavithai.page.link/" + id))
//            .setDomainUriPrefix("https://eniniyakavithai.page.link")
//            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
//            .buildShortDynamicLink()
//            .addOnSuccessListener { command ->
//                val shortLink = command.shortLink
//                val flowchartLink = command.previewLink
//                val shareIntent = Intent()
//                shareIntent.action = Intent.ACTION_SEND_MULTIPLE
//                shareIntent.putExtra(
//                    Intent.EXTRA_SUBJECT,
//                    resources.getString(R.string.app_name)
//                )
//                shareIntent.putExtra(
//                    Intent.EXTRA_TEXT,
//                    description + "\n\n" + shortLink.toString() + "\n\n\n" + "இந்த செயலியை பெற\nhttps://bit.ly/3cCK8Qq"
//                )
//                shareIntent.type = "text/plain"
//
//                startActivity(Intent.createChooser(shareIntent, "Share Community"))
//                Log.i(
//                    TAG,
//                    "createDynamicLink: $shortLink ---> $flowchartLink"
//                )
//            }.addOnFailureListener { command ->
//                Log.i(TAG, "createDynamicLink: " + command.message)
//            }
//    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunityStore){
            allPostInterface= context as CommunityStore
        }
    }

}