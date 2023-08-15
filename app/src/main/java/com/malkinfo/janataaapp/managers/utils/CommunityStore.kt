package com.malkinfo.janataaapp.managers.utils

import android.view.View
import com.malkinfo.janataaapp.models.BottomTabItem

/**
 * ------------------------------------------
 * Created by Farida Shekh on 08-04-2023.
 * ------------------------------------------
 */

interface CommunityStore {

    fun getAllTabList(): ArrayList<BottomTabItem>
    fun initUI()
    fun loadMatrimonyFragment()
    fun loadHomeFragment()
    fun loadStatusFragment(groupPostId:String?)
    fun loadCommunityFragment()
    fun loadBloodGroupFragment()
    fun loadProfileFragment()
    fun loadCommunityGroupFragment(id: String, name: String)
    fun loadEditPostFragment(
        groupIds: ArrayList<String>?,
        description: String?,
        postImageUrl: ArrayList<String>?,
        postId: String?,
        isEdit: Boolean
    )
    fun loadWritePostFragment(groupId: String, postImageUrl: ArrayList<String>)
    fun loadWriteStoryPostFragment(groupId: String, postImageUrl: ArrayList<String>,storyImageUrl:String)
    fun loadViewPostFragment(groupPostId: String?)
    fun loadViewProfileFragment(profileId: String?, groupId: String?)
    fun loadViewAllFollowersFragment(id: String?, groupId: String?, profileName: String?)
    fun loadGroupMembersFragment(groupId: String?)
    fun loadReportFragment(profileId: String?,groupId:String?)
    fun loadGroupMeetingFragment(groupId: String?)
    fun loadNotificationFragment()
    fun loadFindPerfectMatchFragment()
    fun loadPartnerPreferenceFragment(isUpdate: Boolean)
    fun loadMatrimonyFilterFragment(filterType: String)
    fun loadPartnerDetailsFragment(
        id: String?,
        profileUrl: String?,
        fullName: String?
    )
    fun loadChatListFragment()
    fun loadMessageFragment(
        receiverId: String?,
        profileUrl: String?,
        fullName: String?
    )
    fun loadEditProfileFragment()
    fun loadIdentityCardFragment(isFromSignUp: Boolean)
    fun loadProfileSettingsFragment(title: String)
    fun loadBlockedProfileFragment()
    fun loadShortListProfileFragment()
    fun loadViewAllPostFragment(profileId: String?, groupId: String?)
    fun loadEditMatrimonyProfileFragment()
    fun loadUserStatusFragment(groupPostId: String?)
    fun onWritePostClicked(groupId: String?, postImageUrl: ArrayList<String>?)
    fun onPostItemClicked(groupPostId: String?)
    fun onProfileClicked(userId: String?, groupId: String?)
    fun onOtherProfileClicked(profileId: String?, groupId: String?)
    fun onReportPostClicked(groupPostId: String)
    fun onWriteStoryPostClicked(groupId: String?, postImageUrl: ArrayList<String>?,storyImageUrl:String)
    fun onViewMorePostClicked(profileId: String?, groupId: String?)
    fun onGroupMeetingClicked(groupId: String?)
    fun onNotificationClicked()
    fun onViewAllGroupMembersClicked(groupId: String?)
    fun onEditPostClicked(
        groupIds: ArrayList<String>?,
        description: String?,
        postImageUrl: ArrayList<String>?,
        postId: String?,
        isEdit: Boolean
    )
    fun onEditPostStatusClicked(
        groupId: String?,
        description: String?,
        postImageUrl: ArrayList<String>?,
        postId: String?,
        isEdit: Boolean,
        storyImageUrl:String
    )
    fun loadEditPostStatusFragment(groupId: String,
                                   description: String?,
                                   postImageUrl: ArrayList<String>?,
                                   postId: String?,
                                   isEdit: Boolean, storyImageUrl:String)
    fun onViewAllFollowersClicked(
        profileId: String?,
        groupId: String?,
        profileName: String?
    )
    fun onFindPerfectMatchClicked(isUpdate: Boolean)
    fun openPartnerPreferenceFragment(isUpdate: Boolean)
    fun openFindPerfectMatchFragment()
    fun onFilterClicked(filterType: String)
    fun onPartnerProfileClicked(
        receiverId: String,
        profileUrl: String?,
        fullName: String?
    )
    fun onChatClicked()
    fun onSaveDetailsClicked()
    fun onOpenChatClicked(
        receiverId: String?,
        profileUrl: String?,
        fullName: String?
    )
    fun onViewProfileClicked(
        msgReceiverId: String?,
        msgReceiverProfile: String?,
        msgReceiverName: String?
    )
    fun onReportProfileClicked(profileId: String?)
    fun onEditProfileClicked()
    fun onIdentityCardClicked(isFromSignUp: Boolean)
    fun onIndividualSettingsClicked(title: String)
    fun onViewShortListProfilesClicked()
    fun onViewBlockedProfilesClicked()
    fun onEditMatrimonyProfileClicked()
    fun onEditPartnerPreferenceClicked(isUpdate: Boolean)
    fun onCommunityGroupItemClicked(groupId: String, groupName: String)
    fun onItemSubmitted()
    fun onBack()
    fun onClickStatus(groupPostId: String?)
    fun onClickUserStatus(groupPostId: String?)
    fun onSelectFragment(postion:Int)
}