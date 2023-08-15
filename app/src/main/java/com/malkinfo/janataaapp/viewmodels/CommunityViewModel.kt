package com.malkinfo.janataaapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.JsonObject
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.RetrofitManager
import com.malkinfo.janataaapp.models.allpost.StatusSeenData
import com.malkinfo.janataaapp.models.base.FileUploadData
import com.malkinfo.janataaapp.models.community.*
import com.malkinfo.janataaapp.models.community.homemodel.HomeAllGroupPostData
import com.malkinfo.janataaapp.models.community.homemodel.HomeGroupData
import com.malkinfo.janataaapp.models.firebaseuser.FireUser
import com.malkinfo.janataaapp.models.response.Base.BaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class CommunityViewModel(application: Application) : MyBaseViewModel(application) {
    var groupLiveData: MutableLiveData<GroupData> = MutableLiveData<GroupData>()
    var homeAllGroupLiveData: MutableLiveData<HomeGroupData> = MutableLiveData<HomeGroupData>()
    var joinGroupLiveData: MutableLiveData<JoinGroupData> = MutableLiveData<JoinGroupData>()
    var joinGroupSuggestedLiveData: MutableLiveData<JoinGroupData> = MutableLiveData<JoinGroupData>()
    var groupPostLiveData: MutableLiveData<GroupPostData> = MutableLiveData<GroupPostData>()
    var likeGroupPostLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var viewPostLikeGroupPostLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var viewProfileLikePostLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var allPostLikePostLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var groupMembersLiveMembersData: MutableLiveData<GroupMembersData> = MutableLiveData<GroupMembersData>()
    var viewPostLiveData: MutableLiveData<ViewPostData> = MutableLiveData<ViewPostData>()
    var commentLiveData: MutableLiveData<CommentData> = MutableLiveData<CommentData>()
    var addCommentLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var deleteCommentLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var reportPostLiveData: MutableLiveData<ReportData> = MutableLiveData<ReportData>()
    var addPostLiveData: MutableLiveData<WritePostData> = MutableLiveData<WritePostData>()
    var updatePostLiveData: MutableLiveData<WritePostData> = MutableLiveData<WritePostData>()
    var userDetailsLiveData: MutableLiveData<UserDetailsData> = MutableLiveData<UserDetailsData>()
    var followerLiveData: MutableLiveData<FollowerData> = MutableLiveData<FollowerData>()
    var searchFollowerLiveData: MutableLiveData<FollowerData> = MutableLiveData<FollowerData>()
    var searchMembersLiveData: MutableLiveData<GroupPostData> = MutableLiveData<GroupPostData>()
    var deletePostLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var viewAllPostDeletePostLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var followLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var postImageLiveData: MutableLiveData<FileUploadData> = MutableLiveData<FileUploadData>()
    var profilePostsLiveData: MutableLiveData<ProfilePostData> = MutableLiveData<ProfilePostData>()
    var meetingLiveData: MutableLiveData<MeetingData> = MutableLiveData<MeetingData>()
    var homePostLiveData:MutableLiveData<HomeAllGroupPostData> = MutableLiveData()
    var statusPostLiveData : MutableLiveData<HomeAllGroupPostData> = MutableLiveData()
    var userLiveData:MutableLiveData<ArrayList<FireUser>> = MutableLiveData()
    private var allDataBase: DatabaseReference? = null
    var statusSeeLiveData :MutableLiveData<ArrayList<StatusSeenData>> = MutableLiveData()


    /**get User List*/
    fun getUserList() {
        isLoading.postValue(LoaderStatus.loading)
        val fireUserList : ArrayList<FireUser> = ArrayList()
        CoroutineScope(exceptionHandler).launch {
            allDataBase = FirebaseDatabase.getInstance().getReference(MyCommunityAppConstants.FIRE_USER_DATA)
            allDataBase!!.keepSynced(true)
            allDataBase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (fireUserSnapshot in snapshot.children) {
                        val fireUser: FireUser? = fireUserSnapshot.getValue(FireUser::class.java)
                        fireUserList.add(fireUser!!)
                    }
                    userLiveData.postValue(fireUserList)
                }

                override fun onCancelled(error: DatabaseError) {
                   Log.d("mTag","error = ${error.message}")
                }
            })
            isLoading.postValue(LoaderStatus.success)
        }
    }
    fun getStatusLiveList(postId:String) {
        isLoading.postValue(LoaderStatus.loading)
        val seeStatusList : ArrayList<StatusSeenData> = ArrayList()
        CoroutineScope(exceptionHandler).launch {
            allDataBase = FirebaseDatabase.getInstance()
                .getReference(MyCommunityAppConstants.StatuSeen).child(postId)
            allDataBase!!.keepSynced(true)
            allDataBase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (statusSeeSnapshot in snapshot.children) {
                        val statusSee: StatusSeenData? = statusSeeSnapshot.getValue(StatusSeenData::class.java)
                        seeStatusList.add(statusSee!!)
                    }
                    statusSeeLiveData.postValue(seeStatusList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("mTag","error = ${error.message}")
                }
            })
            isLoading.postValue(LoaderStatus.success)
        }
    }
    fun deleteSeeStatusLiveList(postId:String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            allDataBase = FirebaseDatabase.getInstance()
                .getReference(MyCommunityAppConstants.StatuSeen)

            allDataBase!!.child(postId).removeValue()

            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun getGroupList(page: String ,limit: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getCommunityApi().getGroup(page, limit)
            val response = request.await()
            val apiResponse = response.body()!!
            Log.d("mTag","response body = ${response.body().toString()}")

            if (isResponseSuccess(response)) {
                if (apiResponse.groupData?.success!!) {
                    println("-----callapi----")
                    groupLiveData.postValue(apiResponse.groupData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }
    fun getHomeGroupList(page: String ,limit: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getCommunityApi()
                .getHomeAllGroup(page, limit)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.homeAllGroupData?.success!!) {
                    println("-----callapi----")
                    homeAllGroupLiveData.postValue(apiResponse.homeAllGroupData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doJoinGroup(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doJoinGroup(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.joinGroupData?.success!!) {
                    joinGroupLiveData.postValue(apiResponse.joinGroupData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doJoinGroupSuggest(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doJoinGroup(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.joinGroupData?.success!!) {
                    joinGroupSuggestedLiveData.postValue(apiResponse.joinGroupData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getGroupPosts(id: String, page : String, limit : String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGetGroupPosts(id,page,limit)
            val response = request.await()
            val apiResponse = response.body()!!


            if (isResponseSuccess(response)) {
                if (apiResponse.groupPostData?.success!!) {
                    groupPostLiveData.postValue(apiResponse.groupPostData)
                    Log.d("mTag","user status ${apiResponse.groupPostData}")
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getHomePosts(id:String,page: String,limit: String){
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doHomeGroupPost(id,page,limit)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.homeAllGroupPostData?.success!!) {

                    homePostLiveData.postValue(apiResponse.homeAllGroupPostData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }
    fun getStatusPosts(id: String,page: String,limit: String){

        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doHomeGroupPost(id,page,limit)

            val response = request.await()
            val apiResponse = response.body()!!


            if (isResponseSuccess(response)) {
                if (apiResponse.homeAllGroupPostData?.success!!) {
                    statusPostLiveData.postValue(apiResponse.homeAllGroupPostData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")

            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doLikeGroupPost(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGroupPostLike(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    likeGroupPostLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doViewPostLikeGroupPost(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGroupPostLike(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    viewPostLikeGroupPostLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doViewProfileLikePost(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGroupPostLike(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    viewProfileLikePostLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doAllPostLikePost(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGroupPostLike(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    allPostLikePostLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doGetGroupMembers(id: String ,page: String ,limit: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGetAboutGroup(id ,page ,limit )
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.groupMembersData?.success!!) {
                    groupMembersLiveMembersData.postValue(apiResponse.groupMembersData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doGetViewPost(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getCommunityApi().doViewPost(id)

            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.viewPostData?.success!!) {

                    viewPostLiveData.postValue(apiResponse.viewPostData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doGetComments(id: String ,page: String ,limit: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doGetComments(id ,page ,limit)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.commentData?.success!!) {

                    commentLiveData.postValue(apiResponse.commentData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doAddComment(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doAddComment(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.addCommentData?.success!!) {

                    addCommentLiveData.postValue(apiResponse.addCommentData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doDeleteComment(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doDeleteComment(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    deleteCommentLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doReportPost(id: String,gsonObject:JsonObject) {

        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doReportPost(id, gsonObject)
            val response = request.await()

            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.reportData?.success!!) {
                    reportPostLiveData.postValue(apiResponse.reportData)

                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doAddPost( gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doAddPost(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.writePostData?.success!!) {
                    addPostLiveData.postValue(apiResponse.writePostData)

                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doUpdatePost(id: String,gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doUpdatePost(id, gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.writePostData?.success!!) {

                    updatePostLiveData.postValue(apiResponse.writePostData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doGetUserDetails(id: String,groupId:String,page:String,limit:String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doGetProfilePosts(id,groupId,page,limit)

            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.userDetailsData?.success!!) {
                    userDetailsLiveData.postValue(apiResponse.userDetailsData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doGetFollowers(id: String,page: String,limit: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doGetFollowers(id,page,limit)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.followerData?.success!!) {
                    followerLiveData.postValue(apiResponse.followerData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doSearchFollowers(username: String,userId: String, searchType : String , groupId: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi()
                    .doSearchFollowers(username,userId,searchType,groupId)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.followerData?.success!!) {
                    searchFollowerLiveData.postValue(apiResponse.followerData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doSearchMembers(username: String, searchType : String , groupId: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doSearchMembers(username,searchType,groupId)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.groupPostData?.success!!) {
                    searchMembersLiveData.postValue(apiResponse.groupPostData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doDeletePost(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doDeletePost(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    deletePostLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doViewAllPostDelete(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doDeletePost(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    viewAllPostDeletePostLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doFollow(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getCommunityApi().doFollow(id)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    followLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun postMultipleImage(body: Array<MultipartBody.Part?>) {
        isLoading.postValue(LoaderStatus.loading)


        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().multipleFileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!


            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {
                    postImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)

            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun postImage(body: MultipartBody.Part) {

          isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().fileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    postImageLiveData.postValue(apiResponse.fileUploadData)

                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)

                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)

            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doGetAllPost(groupId: String, profileId : String) {
          isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getCommunityApi().doGetAllPost(groupId,profileId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.profilePostData?.success!!) {
                    profilePostsLiveData.postValue(apiResponse.profilePostData)
                } else {
                    errorLiveData.postValue("some error")
                }
            } else {
                errorLiveData.postValue("error")
            }
               isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doGetMeeting(groupId: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getCommunityApi().doGetMeeting(groupId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.meetingData?.success!!) {
                    meetingLiveData.postValue(apiResponse.meetingData)
                } else {
                    errorLiveData.postValue("some error")
                }
            } else {
                errorLiveData.postValue("error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }


}

