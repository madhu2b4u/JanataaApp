package com.malkinfo.janataaapp.views.main.community

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.ProfileAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.FollowerModel
import com.malkinfo.janataaapp.models.community.ProfilePostModel
import com.malkinfo.janataaapp.models.launch.User
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment


class ViewProfileFragment : MyBaseFragment(), ProfileAdapter.OnUserProfileItemClicked {

    private var tempPage: Int = 0
    private var followerCount: Int? = null
    private var totalLimit: Int? = null
    private var currentPosition: Int? = null
    var profileId: String? = null
    var groupId: String? = null
    private var profileFollowerModel: ArrayList<FollowerModel> = ArrayList()
    private var profileDetails: User? = null
    private lateinit var viewProfileInterface: CommunityStore
    private var profilePostModel: ArrayList<ProfilePostModel> = ArrayList()
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var listener: GotoCommunityGroup
    var page = 1
    var strPageLimit = 10
    /**set Id*/
    private lateinit var profileRV:RecyclerView

    interface GotoCommunityGroup {
        fun onBack()
    }

    companion object {
        @JvmStatic
        fun newInstance(profileId: String?, groupId: String?) =
            ViewProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.PROFILE_ID, profileId)
                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                }
            }
    }

    private val viewProfileCommunityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                profileId = it.getString(MyCommunityAppConstants.PROFILE_ID)
                groupId = it.getString(MyCommunityAppConstants.GROUP_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIds(view)
        setUpLoader(viewProfileCommunityViewModel)
        doGetUserDetails()
    }

    private fun initIds(v:View){
        profileRV = v.findViewById(R.id.profileRV)
    }
    private fun doGetUserDetails() {
        page=1
        profileFollowerModel.clear()
        profilePostModel.clear()
        viewProfileCommunityViewModel.doGetUserDetails(profileId!!,groupId!!,page.toString(),strPageLimit.toString())
    }


    override fun initObservers() {

        viewProfileCommunityViewModel.userDetailsLiveData.observe(this, Observer {
            if (it != null) {
                profileDetails = it.user!!

                if (it.followersCount != null) {
                    followerCount = it.followersCount
                }

                if (it.total_count != null) {
                    totalLimit = it.total_count
                    if (page == 1) {
                        initViews()
                    }
                }

                if (it.followers!!.any()) {
                    profileFollowerModel.clear()
                    profileFollowerModel.addAll(it.followers!!)
                    profileAdapter.notifyDataSetChanged()
                }

                if (it.posts!!.any()) {
                    if (page == 1) {
                        profilePostModel.clear()
                        profilePostModel.addAll(it.posts!!)
                        profileAdapter.notifyDataSetChanged()
                    } else {
                        if (tempPage != page) {
                            profilePostModel.addAll(it.posts!!)
                            profileAdapter.notifyDataSetChanged()
                            tempPage = page
                        }
                    }
                } else {
                    it.posts!!.clear()
                    profilePostModel.clear()
                    profileAdapter.notifyDataSetChanged()
                }
            }
        })

        /*viewProfileCommunityViewModel.viewProfileLikePostLiveData.observe(this, Observer {
               // showSnackbar(it.message!!)
            })*/

        viewProfileCommunityViewModel.deletePostLiveData.observe(this, Observer {
            if (it != null) {
                //showSnackbar(it.message!!)
                viewProfileCommunityViewModel.doGetUserDetails(profileId!!, groupId!!,page.toString(),strPageLimit.toString())

            }
        })

        viewProfileCommunityViewModel.followLiveData.observe(this, Observer {
            if (it != null) {
                //showSnackbar(it.message!!)
                profileAdapter.notifyDataSetChanged()
            }
        })

    }

    private fun initViews() {

        profileRV.layoutManager = LinearLayoutManager(requireContext())
        profileAdapter = ProfileAdapter(requireActivity(), profileDetails!!, profileFollowerModel, profilePostModel, followerCount!!, totalLimit!!,this)
        profileRV.adapter = profileAdapter

        profileAdapter.onFollowClicked = {
            viewProfileCommunityViewModel.doFollow(profileDetails!!._id!!)
            profileDetails!!.is_following = !profileDetails!!.is_following!!
        }

        profileAdapter.onViewAllFollowerClicked = {
            viewProfileInterface.onViewAllFollowersClicked(
                profileDetails!!._id,
                groupId,
                profileDetails!!.full_name
            )
        }

        profileAdapter.onBackClicked = {
            listener.onBack()
            requireActivity().viewModelStore.clear()
        }

    }

    private fun openReportPopupMenu(pos: Int, view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_report ->
                    viewProfileInterface.onReportPostClicked(profilePostModel[pos]._id!!)
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
                    viewProfileInterface.onEditPostClicked(
                        profilePostModel[pos].group_id,
                        profilePostModel[pos].description,
                        profilePostModel[pos].image_url, profilePostModel[pos]._id,
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
                viewProfileCommunityViewModel.doDeletePost(profilePostModel[pos]._id!!)
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


    override fun onErrorCalled(it: String?) {
        it?.let {
            showSnackbar(it)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CommunityStore) {

            viewProfileInterface = context as CommunityStore
        }

        if (context is GotoCommunityGroup) {
            listener = context as GotoCommunityGroup
        }
    }



    override fun onFavClicked(pos: Int) {
        currentPosition = pos
        viewProfileCommunityViewModel.doViewProfileLikePost(profilePostModel[pos]._id!!)
        profilePostModel[pos].is_liked = !profilePostModel[pos].is_liked!!


        if (profilePostModel.get(currentPosition!!).is_liked!!) {
            profilePostModel.get(currentPosition!!).likes_count =
                profilePostModel.get(currentPosition!!).likes_count!! + 1
            profileAdapter.notifyDataSetChanged()
        } else {
            profilePostModel.get(currentPosition!!).likes_count =
                profilePostModel.get(currentPosition!!).likes_count!! - 1
            profileAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(pos: Int) {
        viewProfileInterface.onPostItemClicked(profilePostModel[pos]._id)
    }

    override fun onMoreOptionClicked(pos: Int, view: View) {
        openPopupMenu(pos, view)
    }

    override fun onOtherUserMoreOptionClicked(pos: Int, view: View) {
        openReportPopupMenu(pos, view)
    }

    override fun onShareClicked(pos: Any?) {
        //createDynamicLink(profilePostModel[pos]._id, profilePostModel[pos].description)
    }


    override fun onViewMorePostClicked() {
        val limit: Int = totalLimit!! / strPageLimit
        if (page <= limit) {
            page += 1
            viewProfileCommunityViewModel.doGetUserDetails(profileId!!, groupId!!,page.toString(),strPageLimit.toString())
        }
    }

    override fun onListScroll() {
       /* val limit: Int = totalLimit!! / strPageLimit
        if (page <= limit) {
            page += 1
            viewProfileCommunityViewModel.doGetUserDetails(profileId!!, groupId!!,page.toString(),strPageLimit.toString())
        }*/
    }


}