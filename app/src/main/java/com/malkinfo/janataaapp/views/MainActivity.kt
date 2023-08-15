package com.malkinfo.janataaapp.views

import MyCommunityApp
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.BottomTabItem
import com.malkinfo.janataaapp.models.firebaseuser.FireUser
import com.malkinfo.janataaapp.viewmodels.BaseViewModel
import com.malkinfo.janataaapp.viewmodels.UserViewModel
import com.malkinfo.janataaapp.views.launch.GetUserDetailsFragment
import com.malkinfo.janataaapp.views.main.*
import com.malkinfo.janataaapp.views.main.Profile.*
import com.malkinfo.janataaapp.views.main.community.*
import com.malkinfo.janataaapp.views.main.home.HomeFragment
import com.malkinfo.janataaapp.views.main.home.StatusFragment
import com.malkinfo.janataaapp.views.main.home.UserStatusFragment
import com.malkinfo.janataaapp.views.main.home.WriteStoryPostFragment
import com.malkinfo.janataaapp.views.main.matrimony.*

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * set interfaces
 * ------------------------------------------
 */
class MainActivity : BottomNavigationActivity(),CommunityStore{

    private var isFromFragment: String? = null
    private var fragment: Fragment? = null
    private var fragmentManager: FragmentManager? = supportFragmentManager
    private lateinit var tabRV :RecyclerView

    var database: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null

    private val profileViewModel: BaseViewModel by lazy {
        ViewModelProvider(this).get(BaseViewModel::class.java)
    }
    private val editProfileViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    //private var navController: NavController? = null
    override fun tabItemSelected(position: Int) {
        when (position) {
            0 -> {
                if (fragment !is CommunityGroupFragment) {
                    loadHomeFragment()
                }
            }
            1 -> {
                if (fragment !is MatrimonyFragment) {
                    loadMatrimonyFragment()
                }
            }
            2 -> {
                if (fragment !is CommunityFragment) {
                    loadCommunityFragment()
                }
            }
            3 -> {
                if (fragment !is BloodGroupFragment) {
                    loadBloodGroupFragment()
                }
            }
            4 -> {
                if (fragment !is ProfileFragment) {
                    loadProfileFragment()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_main)
        tabRV = findViewById(R.id.tabRV)
        initBottomNavigation(tabRV, getAllTabList())
        initUI()
        addingFireData()
        Log.d("mTag","i am onCreate")
    }

    private fun addingFireData() {
        val user = MyCommunityApp.getUser(this)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val uid = auth!!.uid
        val is_id = user!!._id
        val phone = user.mobile
        val name = user.full_name
        val token = sharedPrefManager.getPreference(MyCommunityAppConstants.AUTH_TOKEN)
        val user_photo_url = user.profile_url
        val isFireuser = FireUser(uid = uid,
            _id = is_id,name= name,
            phoneNumber= phone,status = "Online",
            token = token,
            user_photo_url
        )
        database!!.reference
            .child(MyCommunityAppConstants.FIRE_USER_DATA)
            .child(is_id!!)
            .setValue(isFireuser)
            .addOnSuccessListener {
                Log.d("mTag","Add the Data Success")
            }

    }


    override fun onResume() {
        super.onResume()
        Log.d("mTag"," i am onResume Online")
        val currentUserId = MyCommunityApp.getUser(this)!!._id
        database!!.reference.child(MyCommunityAppConstants.FIRE_USER_DATA)
            .child(currentUserId!!).child("status").setValue("Online")
    }

    //Generating tab item
    override fun getAllTabList(): ArrayList<BottomTabItem> {
        val tabItems = ArrayList<BottomTabItem>()
        tabItems.add(
            BottomTabItem(
                R.drawable.ic_home,
                R.drawable.ic_home_act,
                ""
            )
        )
        tabItems.add(
            BottomTabItem(
                R.drawable.couple_inactive,
                R.drawable.couple,
                ""
            )
        )
        tabItems.add(
            BottomTabItem(
                R.drawable.chat_inactive,
                R.drawable.chat,
                ""
            )
        )
        tabItems.add(
            BottomTabItem(
                R.drawable.blood_inactive,
                R.drawable.bloodgroup,
                ""
            )
        )

        tabItems.add(
            BottomTabItem(
                R.drawable.user_inactive,
                R.drawable.user,
                ""
            )
        )

        return tabItems
    }

    override fun initUI() {
        /* navController = Navigation.findNavController(this, R.id.mainContainer)

         NavigationUI.setupWithNavController(bottomNavigationView, navController!!)*/
        //loadMatrimonyFragment()
        val isUri = intent.data
        if (isUri != null){
            if (SharedPrefManager.getInstance(this)
                    .getBooleanPreference(MyCommunityAppConstants.IS_SIGNUP)){
                val params = isUri.pathSegments
                val postId = params.get(params.size -1)
                loadViewPostFragment(postId)
            }else{
                loadHomeFragment()
            }

        }else{
            loadHomeFragment()
        }

        /*bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.itemIconTintList = null*/
    }


    /*private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.matrimonyFragment -> {
                    loadMatrimonyFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.communityFragment -> {
                    loadCommunityFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bloodGroupFragment -> {
                    loadBloodGroupFragment()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.profileFragment -> {
                    loadProfileFragment()
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }*/

    override fun loadMatrimonyFragment() {
        fragment = MatrimonyFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as MatrimonyFragment)?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadHomeFragment() {
        fragment = HomeFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as HomeFragment)?.disallowAddToBackStack()
            ?.commit()
    }
    override fun loadStatusFragment(groupPostId: String?) {
        fragment = StatusFragment.newInstance(groupPostId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as StatusFragment)?.addToBackStack(null)
            ?.commit()
    }
    override fun loadUserStatusFragment(groupPostId: String?) {
        fragment = UserStatusFragment.newInstance(groupPostId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as UserStatusFragment)?.addToBackStack(null)
            ?.commit()
    }

    override fun loadCommunityFragment() {
        fragment = CommunityFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as CommunityFragment)?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadBloodGroupFragment() {
        fragment = BloodGroupFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as BloodGroupFragment)?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadProfileFragment() {
        fragment = ProfileFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ProfileFragment)?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadCommunityGroupFragment(id: String, name: String) {
        fragment = CommunityGroupFragment.newInstance(id, name)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as CommunityGroupFragment)?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadEditPostFragment(
        groupIds: ArrayList<String>?,
        description: String?,
        postImageUrl: ArrayList<String>?,
        postId: String?,
        isEdit: Boolean
    ) {
        fragment = WritePostFragment.newInstance(null,
            groupIds,
            description,
            postImageUrl,
            postId,
            isEdit
        )
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as WritePostFragment)?.disallowAddToBackStack()
            ?.commit()
    }

    override fun loadWritePostFragment(groupId: String, postImageUrl: ArrayList<String>) {

        fragment = WritePostFragment.newInstance(groupId, null,"",
            postImageUrl,
            "",
            false)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as WritePostFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }
    override fun loadWriteStoryPostFragment(groupId: String,
                                            postImageUrl: ArrayList<String>,
                                            storyImageUrl:String
    ) {

        fragment = WriteStoryPostFragment.newInstance(groupId, "", postImageUrl, "", false,storyImageUrl)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as WriteStoryPostFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadViewPostFragment(groupPostId: String?) {
        fragment = ViewPostFragment.newInstance(groupPostId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ViewPostFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadViewProfileFragment(profileId: String?, groupId: String?) {
        fragment = ViewProfileFragment.newInstance(profileId, groupId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ViewProfileFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadViewAllFollowersFragment(id: String?, groupId: String?, profileName: String?) {
        fragment = ViewAllFollowersFragment.newInstance(id, groupId, profileName)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ViewAllFollowersFragment)
            ?.addToBackStack("YES")
            ?.commit()

    }

    override fun loadGroupMembersFragment(groupId: String?) {
        fragment = GroupMembersFragment.newInstance(groupId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as GroupMembersFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadReportFragment(profileId: String?,groupId:String?) {
        fragment = ReportFragment.newInstance(profileId,groupId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ReportFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadGroupMeetingFragment(groupId: String?) {
        fragment = GroupMeetingFragment.newInstance(groupId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as GroupMeetingFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadNotificationFragment() {
        fragment = NotificationFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as NotificationFragment)
            ?.addToBackStack("YES")
            ?.commit()

    }

    override fun loadFindPerfectMatchFragment() {
        fragment = FindPerfectMatchFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as FindPerfectMatchFragment)
            ?.disallowAddToBackStack()
            ?.commit()
    }


    override fun loadPartnerPreferenceFragment(isUpdate: Boolean) {

        fragment = PartnerPreferencesFragment.newInstance(isUpdate)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as PartnerPreferencesFragment)
            ?.disallowAddToBackStack()
            ?.commit()

    }


    override fun loadMatrimonyFilterFragment(filterType: String) {
        fragment = MatrimonyFilterFragment.newInstance(filterType)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as MatrimonyFilterFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadPartnerDetailsFragment(
        id: String?,
        profileUrl: String?,
        fullName: String?
    ) {
        fragment = PartnerDetailsFragment.newInstance(id, profileUrl, fullName)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as PartnerDetailsFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadChatListFragment() {
        fragment = ChatListFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ChatListFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadMessageFragment(
        receiverId: String?,
        profileUrl: String?,
        fullName: String?
    ) {
        fragment = MessageFragment.newInstance(receiverId, profileUrl, fullName)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as MessageFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadEditProfileFragment() {

        fragment = EditProfileFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as EditProfileFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadIdentityCardFragment(isFromSignUp: Boolean) {
        fragment = GetUserDetailsFragment.newInstance(isFromSignUp)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as GetUserDetailsFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadProfileSettingsFragment(title: String) {
        fragment = ProfileSettingsFragment.newInstance(title)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ProfileSettingsFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadBlockedProfileFragment() {
        fragment = BlockedProfileFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as BlockedProfileFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadShortListProfileFragment() {
        fragment = ShortlistFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ShortlistFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadViewAllPostFragment(profileId: String?, groupId: String?) {
        fragment = ViewAllPostFragment.newInstance(profileId, groupId)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as ViewAllPostFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }

    override fun loadEditMatrimonyProfileFragment() {
        fragment = EditMatrimonyProfileFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as EditMatrimonyProfileFragment)
            ?.addToBackStack("YES")
            ?.commit()
    }


    override fun onWritePostClicked(groupId: String?, postImageUrl: ArrayList<String>?) {
        loadWritePostFragment(groupId!!, postImageUrl!!)

    }

    override fun onPostItemClicked(groupPostId: String?) {
        loadViewPostFragment(groupPostId)
    }

    override fun onProfileClicked(userId: String?, groupId: String?) {
        loadViewProfileFragment(userId, groupId)
    }

    override fun onOtherProfileClicked(profileId: String?, groupId: String?) {
        loadViewProfileFragment(profileId, groupId)
    }

    override fun onReportPostClicked(groupPostId: String) {
        loadReportFragment(null, groupPostId)
    }

    override fun onWriteStoryPostClicked(groupId: String?, postImageUrl: ArrayList<String>?,
                                         storyImageUrl:String) {
        loadWriteStoryPostFragment(groupId!!,postImageUrl!!,storyImageUrl)
    }

    override fun onViewMorePostClicked(profileId: String?, groupId: String?) {
        loadViewAllPostFragment(profileId, groupId)
    }

    override fun onGroupMeetingClicked(groupId: String?) {

        loadGroupMeetingFragment(groupId)
    }

    override fun onNotificationClicked() {
        loadNotificationFragment()
    }

    override fun onViewAllGroupMembersClicked(groupId: String?) {
        loadGroupMembersFragment(groupId)
    }

    override fun onEditPostClicked(
        groupIds: ArrayList<String>?,
        description: String?,
        postImageUrl: ArrayList<String>?,
        postId: String?,
        isEdit: Boolean
    ) {
        loadEditPostFragment(groupIds!!, description, postImageUrl, postId, isEdit)
    }
    override fun onEditPostStatusClicked(
        groupId: String?,
        description: String?,
        postImageUrl: ArrayList<String>?,
        postId: String?,
        isEdit: Boolean,
        storyImageUrl:String
    ) {
        loadEditPostStatusFragment(groupId!!, description, postImageUrl, postId, isEdit, storyImageUrl)
    }
    override  fun loadEditPostStatusFragment(groupId: String,
                                             description: String?,
                                             postImageUrl: ArrayList<String>?,
                                             postId: String?,
                                             isEdit: Boolean,
                                             storyImageUrl:String
    ){

        fragment = WriteStoryPostFragment.newInstance(
            groupId,
            description,
            postImageUrl,
            postId,
            isEdit,
            storyImageUrl
        )
        fragmentManager?.beginTransaction()
            ?.replace(R.id.mainContainer, fragment as WriteStoryPostFragment)?.disallowAddToBackStack()
            ?.commit()

    }

    override fun onViewAllFollowersClicked(
        profileId: String?,
        groupId: String?,
        profileName: String?
    ) {
        loadViewAllFollowersFragment(profileId, groupId, profileName)
    }

    override fun onFindPerfectMatchClicked(isUpdate: Boolean) {
        loadPartnerPreferenceFragment(isUpdate)
    }

    override fun openPartnerPreferenceFragment(isUpdate: Boolean) {
        loadPartnerPreferenceFragment(isUpdate)
    }

    override fun openFindPerfectMatchFragment() {
        loadFindPerfectMatchFragment()
    }

    override fun onFilterClicked(filterType: String) {
        loadMatrimonyFilterFragment(filterType)
    }

    override fun onPartnerProfileClicked(
        receiverId: String,
        profileUrl: String?,
        fullName: String?
    ) {
        loadPartnerDetailsFragment(receiverId, profileUrl, fullName)
    }

    override fun onChatClicked() {
        loadChatListFragment()
    }

    override fun onSaveDetailsClicked() {
        loadMatrimonyFragment()
    }

    override fun onOpenChatClicked(
        receiverId: String?,
        profileUrl: String?,
        fullName: String?
    ) {
        loadMessageFragment(receiverId, profileUrl, fullName)
    }

    override fun onViewProfileClicked(
        msgReceiverId: String?,
        msgReceiverProfile: String?,
        msgReceiverName: String?
    ) {
        loadPartnerDetailsFragment(msgReceiverId, msgReceiverProfile, msgReceiverName)
    }

    override fun onReportProfileClicked(profileId: String?) {
        loadReportFragment(profileId!!,null)
    }

    override fun onEditProfileClicked() {
        loadEditProfileFragment()
    }

    override fun onIdentityCardClicked(isFromSignUp: Boolean) {
        loadIdentityCardFragment(isFromSignUp)
    }

    override fun onIndividualSettingsClicked(title: String) {
        loadProfileSettingsFragment(title)
    }

    override fun onViewShortListProfilesClicked() {
        loadShortListProfileFragment()
    }

    override fun onViewBlockedProfilesClicked() {
        loadBlockedProfileFragment()
    }

    override fun onEditMatrimonyProfileClicked() {
        loadEditMatrimonyProfileFragment()
    }

    override fun onEditPartnerPreferenceClicked(isUpdate: Boolean) {
        loadPartnerPreferenceFragment(isUpdate)
    }

    override fun onCommunityGroupItemClicked(groupId: String, groupName: String) {
        loadCommunityGroupFragment(groupId, groupName)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (fragment is MatrimonyFilterFragment) {
            viewModelStore.clear()
        }
        if (fragment is BlockedProfileFragment) {
            viewModelStore.clear()
        }
        if (fragment is MessageFragment) {
            viewModelStore.clear()
        }

        if (fragment is ViewProfileFragment) {
            viewModelStore.clear()
        }

    }

    override fun initObservers() {

    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun onPause() {
        super.onPause()
        Log.d("mTag","I am onPause Active")
        val currentUserId = MyCommunityApp.getUser(this)!!._id
        database!!.reference.child(MyCommunityAppConstants.FIRE_USER_DATA)
            .child(currentUserId!!).child("status").setValue("Offline")
    }


    override fun onDestroy() {
        super.onDestroy()
        val currentUserId = MyCommunityApp.getUser(this)!!._id
        database!!.reference.child(MyCommunityAppConstants.FIRE_USER_DATA)
            .child(currentUserId!!).child("status").setValue("Offline")

    }

    override fun onItemSubmitted() {
        //Item Submitted
        //Toast.makeText(this, "Finished", Toast.LENGTH_LONG).show()
        fragmentManager!!.popBackStack()
    }

    override fun onBack() {
        fragmentManager!!.popBackStack()
    }

    override fun onClickStatus(groupPostId: String?) {
        loadStatusFragment(groupPostId)
    }
    override fun onClickUserStatus(groupPostId: String?) {
        loadUserStatusFragment(groupPostId)
    }

    override fun onSelectFragment(postion:Int) {
        if (postion==0){
            loadStatusFragment(null)
        }else{
            mTabRecyclerAdapter!!.setSelectedPos(postion)
        }

    }


}