package com.malkinfo.janataaapp.views.main.home

import MyCommunityApp
import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.allpost.StatusSeenData
import com.malkinfo.janataaapp.models.community.GroupPostModel
import com.malkinfo.janataaapp.utitlis.StoryStore
import com.malkinfo.janataaapp.utitlis.statuspost.OnStatusSwipeListener
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.*
import java.util.concurrent.TimeUnit
import com.malkinfo.janataaapp.R


/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */

class StatusFragment :MyBaseFragment(), StoryStore {
   // private lateinit var statusAdapter: UserStatusAdapter
   private var allUserStatusModel: ArrayList<GroupPostModel> = ArrayList()
    private var groupPostId:String? = ""
    /**set ID*/
    //private lateinit var addStatusIV: ImageView
   // private lateinit var statusRV: RecyclerView
   // private lateinit var noDataStatus: TextView
    private lateinit var statusBackIV:ImageView
    private lateinit var statusImgIV:ImageView
    private lateinit var storiesSV: StoriesProgressView
    private lateinit var discpTV:TextView
    private lateinit var pubImgCV: CircleImageView
    private lateinit var pubNameTV:TextView
    private lateinit var pulished_atTV:TextView
    private lateinit var statusLayoutRL: RelativeLayout
    private lateinit var videoFrams:FrameLayout
    private lateinit var videoStoryVV:VideoView

    /**==============================================*/

    private var totalLimit: Int? = null
    var page = 1
    var strPageLimit = 10
    private var viewPostModel: GroupPostModel? = null
    private var statusPostModel:ArrayList<GroupPostModel> = ArrayList()
    private lateinit var listener : CommunityStore
    private var position = 0
    private lateinit var strPostTime: String
    private var mCurrentPosition = 0
    private var seeUserModel: ArrayList<StatusSeenData> = ArrayList()

    var database: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    private val viewPostCommunityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunityStore){
            listener = context as CommunityStore
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupPostId =it.getString(MyCommunityAppConstants.GROUP_ID)
            Log.d("mTag","selectPos = $groupPostId")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsIDs(view)
        setUpLoader(viewPostCommunityViewModel)
        initViews(view)
    }

    override fun onErrorCalled(it: String?) {}

    override fun initObservers() {
        viewPostCommunityViewModel.viewPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.post != null) {
                    viewPostModel = it.post!!
                    setData(viewPostModel!!)
                }
            }
        })

        viewPostCommunityViewModel.groupPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                    Log.d("mTag","totalLimit = ${totalLimit}")
                    if (page == 1){
                        setStatusAdapter()
                    }
                }
                if (it.posts!!.any()) {
                    if (page == 1) {
                        addUserPost(it.posts!!)
                        Log.d("mTag","it.posts!! userPostModel = ${statusPostModel.size}")
                        //userPostAdapter.notifyDataSetChanged()
                    } else {
                        addUserPost(it.posts!!)
                    //userPostAdapter.notifyDataSetChanged()
                    }
                    //  noPostFoundTV.visibility= View.GONE
                }else{
                    //noPostFoundTV.visibility= View.VISIBLE
                    //  it.posts!!.clear()
                    //userPostModel.clear()
                    // userPostAdapter.notifyDataSetChanged()
                }
            }
        })



    }

    private fun addUserPost(posts: ArrayList<GroupPostModel>) {
        for (post_item in posts){
            if (postContains(allUserStatusModel,post_item._id)){
                Log.d("mTag","User status already add")
            }else{
                if (MyCommunityApp
                        .getUser(requireActivity())!!._id
                    == post_item.user_id!!._id){
                    Log.d("mTag","This user post")

                }else{
                    allUserStatusModel.add(post_item)
                }
            }
        }

    }
    private fun setVideoPath(urlPath: String?, videoPostViewVV:VideoView) {
        // Show the "Buffering..." message while the video loads.
        val uri = Uri.parse(urlPath)
        videoPostViewVV.visibility = View.VISIBLE
        if (uri != null) {
            videoPostViewVV.setVideoURI(uri)
        }
        val mediaController = MediaController(requireActivity())
        videoPostViewVV.setMediaController(mediaController)
        mediaController.setAnchorView(videoPostViewVV)
        // Listener for onPrepared() event (runs after the media is prepared).
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(urlPath)
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeInMillisec = time!!.toLong()
        retriever.release()

        videoPostViewVV.setOnPreparedListener(
            object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mediaPlayer: MediaPlayer?) {

                    // Hide buffering message.
                    videoPostViewVV.visibility = View.VISIBLE

                    // Restore saved position, if available.
                    if (mCurrentPosition > 0) {
                        videoPostViewVV.seekTo(mCurrentPosition)
                    } else {
                        // Skipping to 1 shows the first frame of the video.
                        videoPostViewVV.seekTo(1)
                    }
                    storiesSV.setStoryDuration(timeInMillisec);
                    storiesSV.setStoriesCount(1);
                    storiesSV.setStoryDuration(timeInMillisec)
                    // Start playing!
                    videoPostViewVV.start()
                    storiesSV.startStories();
                }
            })

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        videoPostViewVV.setOnCompletionListener(
            object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mediaPlayer: MediaPlayer?) {
                    Toast.makeText(
                        requireActivity(),
                        "Video Complete",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Return the video position to the start.
                    videoPostViewVV.seekTo(0)
                }
            })
    }

    private fun setData(viewPostModel: GroupPostModel) {
        if (MyCommunityApp.getUser(requireActivity())!!
                ._id == viewPostModel.user_id!!._id){
            Log.d("mTag","This User Status Post")
        }else{
            allUserStatusModel.add(viewPostModel)
        }
        viewPostCommunityViewModel.getGroupPosts(MyCommunityAppConstants.STATUS_STORY_GROUP_ID,page.toString(), strPageLimit.toString())
    }

    private fun initsIDs(v:View){

        //addStatusIV = v.findViewById(R.id.addStatusIV)
        //statusRV = v.findViewById(R.id.statusRV)
       // noDataStatus = v.findViewById(R.id.noDataStatus)
        statusBackIV = v.findViewById(R.id.statusBackIV)
        statusImgIV = v.findViewById(R.id.statusImgIV)
        storiesSV = v.findViewById(R.id.storiesSV)
        discpTV = v.findViewById(R.id.discpTV)
        pubImgCV = v.findViewById(R.id.pulisherImgCIV)
        pubNameTV = v.findViewById(R.id.pulishNameTV)
        pulished_atTV = v.findViewById(R.id.pulished_atTV)
        statusLayoutRL = v.findViewById(R.id.statusLayoutRL)
        videoFrams = v.findViewById(R.id.videoFrams)
        videoStoryVV = v.findViewById(R.id.videoStoryVV)
        /**set Back*/
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        mDatabase = database!!.getReference(MyCommunityAppConstants.StatuSeen)

    }
    private fun initViews(view: View){

        if (groupPostId == null){
            viewPostCommunityViewModel.getGroupPosts(MyCommunityAppConstants.STATUS_STORY_GROUP_ID,page.toString(), strPageLimit.toString())

        }else{
           viewPostCommunityViewModel.doGetViewPost(groupPostId!!)
           // viewPostCommunityViewModel.doGetComments(groupPostId!!,page.toString() ,strPageLimit.toString())
        }

        statusBackIV.setOnClickListener {
            listener.onBack()
            requireActivity().viewModelStore.clear()
        }
        onTouchListener(view)


    }
//    fun adapterOv(){
//        statusAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onChanged() {
//                super.onChanged()
//                if (statusAdapter.getItemCount() == 0) {
//                    statusRV.visibility = View.GONE
//                    noDataStatus.visibility = View.VISIBLE
//                } else {
//                    statusRV.visibility = View.VISIBLE
//                    noDataStatus.visibility = View.GONE
//                }
//            }
//        })
//    }
    private fun setStatusAdapter(){
    val statusIndex: ListIterator<GroupPostModel> = allUserStatusModel.listIterator()
    Log.d("mTag","I am setStatusAdapter")
     addStatus(position)
    storiesSV.setStoriesListener(object : StoriesProgressView.StoriesListener {
        override fun onNext() {}

        override fun onPrev() {}

        override fun onComplete() {
            if (position == (allUserStatusModel.size -1)){
                listener.onBack()
                requireActivity().viewModelStore.clear()
            }else{
                ++position
                addStatus(position)
            }

        }
    })

}
    fun notifySeenUser(post_id:String){
        val user = MyCommunityApp.getUser(requireActivity())

        val currentTime = Calendar.getInstance().timeInMillis
        Log.d("mTag","current time = ${currentTime} ")

        val is_user_id = user!!._id
        val name = user.full_name
        val profileImg = user.profile_url

      //  val user_photo_url = user.profile_url
        val seenUser = StatusSeenData(
            post_id = post_id,
            user_id = is_user_id!!,
            userName = name!!,
            userProfileImg = profileImg,
            seentime =currentTime)
        database!!.reference
            .child(MyCommunityAppConstants.StatuSeen)
            .child(post_id)
            .child(is_user_id)
            .setValue(seenUser)
            .addOnSuccessListener {
                Log.d("mTag","Add the Data Success")
            }
        seeUserModel.add(seenUser)

    }

    fun addStatus(isPos :Int){
        try {
            Log.d("mTag","i am addStatus try")
            val statusPoL = allUserStatusModel[isPos]
            videoStoryVV.stopPlayback()

            pubNameTV.text = statusPoL.user_id!!.full_name
            for (postImt in statusPoL.image_url!!){
                if (postImt.endsWith(".mp4")){
                    statusImgIV.visibility = View.GONE
                    videoFrams.visibility = View.VISIBLE
                    setVideoPath(postImt,videoStoryVV)
                }else{
                    videoFrams.visibility = View.GONE
                    statusImgIV.visibility = View.VISIBLE
                    statusImgIV.load(postImt)
                    storiesSV.setStoryDuration(10000L);
                    storiesSV.setStoriesCount(1);
                    storiesSV.setStoryDuration(10000L)
                    storiesSV.startStories();
                }
            }

            if (statusPoL.user_id!!.profile_url != null){
                pubImgCV.load(statusPoL.user_id!!.profile_url )
            }else{
                pubImgCV.load(R.drawable.profile_iv)
            }

            discpTV.text = statusPoL.description
//            storiesSV.setStoryDuration(10000L);
//
//            storiesSV.setStoriesCount(1);
//            storiesSV.setStoryDuration(10000L)

            val time: Long = statusPoL.published_at!!.toLong()
            val currentDate = Date()
            val dateDiff = currentDate.time - time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)

            when {
                second < 60 -> {
                    strPostTime = if (second == 1L) {
                        "$second second ago"
                    } else {
                        "$second seconds ago"
                    }
                    pulished_atTV.text = strPostTime
                }
                minute < 60 -> {
                    strPostTime = if (minute == 1L) {
                        "$minute minute ago"
                    } else {
                        "$minute minutes ago"
                    }
                    pulished_atTV.text = strPostTime
                }
                hour < 24 -> {
                    strPostTime = if (hour == 1L) {
                        "$hour hour ago"
                    } else {
                        "$hour hours ago"
                    }
                    pulished_atTV.text = strPostTime
                }
                else -> {
                    pulished_atTV.text = DateHelper.getFullFormatDate(time)
                }
            }


            if (statusUserContains(seeUserModel,statusPoL.user_id!!._id!!)){
                Log.d("mTag","User Already Log")
            }else{
                notifySeenUser(statusPoL._id!!)
            }



        }catch (e:Exception){
            e.printStackTrace()
            Log.d("mTag","excep = ${e.message}")
        }



    }

    fun onTouchListener (view: View){
        view.setOnTouchListener(object : OnStatusSwipeListener(requireActivity()){

            override fun onSwipeTop() {
                super.onSwipeTop()
                if (videoStoryVV.isPlaying){
                    videoStoryVV.stopPlayback()
                }
                if (position == (allUserStatusModel.size -1)){
                    listener.onBack()
                    requireActivity().viewModelStore.clear()
                }else{
                    ++position
                    addStatus(position)
                }
                //Toast.makeText(requireActivity(),"On Swipe Top",Toast.LENGTH_SHORT).show()

            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                if (videoStoryVV.isPlaying){
                    videoStoryVV.stopPlayback()
                }

                if (position == -1){
                    listener.onBack()
                    requireActivity().viewModelStore.clear()
                }else{
                    --position
                    addStatus(position)
                }
                //Toast.makeText(requireActivity(),"On Swipe Bottom",Toast.LENGTH_SHORT).show()
            }

        })
    }
//    fun seeStatus(post_id:String){
//        val main = JSONObject()
//        try {
//            main.put("comment", "Seen post")
//            main.put("post_id",post_id)
//            val jsonParser = JsonParser()
//            val gsonObject = jsonParser.parse(main.toString()) as JsonObject
//            viewPostCommunityViewModel.doAddComment(gsonObject)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }

    fun postContains(list: ArrayList<GroupPostModel>, name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }
    fun statusUserContains(list: ArrayList<StatusSeenData>,userId:String): Boolean {
        for (item in list) {
            if (item.user_id.equals(userId)) {
                return true
            }
        }
        return false
    }
    companion object {
        @JvmStatic
        fun newInstance(groupPostId: String?) =
            StatusFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.GROUP_ID,groupPostId)

                }
            }
    }

    override fun getNewStory() {
        TODO("Not yet implemented")
    }

}