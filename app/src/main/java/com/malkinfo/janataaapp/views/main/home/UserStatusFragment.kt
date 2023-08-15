package com.malkinfo.janataaapp.views.main.home

import android.content.Context
import android.content.DialogInterface
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.allpost.StatusSeenData
import com.malkinfo.janataaapp.models.community.GroupPostModel
import com.malkinfo.janataaapp.utitlis.SeeMoreTextView
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class UserStatusFragment : MyBaseFragment() {
    private var groupPostId:String? = ""

    private lateinit var strPostTime: String
    private var mCurrentPosition = 0
    /**set id*/
    private lateinit var userPulishNameTV : TextView
    private lateinit var userPulished_atTV : TextView
    private lateinit var userPulisherImgCIV : CircleImageView
    private lateinit var eidtMenu : ImageView
    private lateinit var userstoriesSV : StoriesProgressView
    private lateinit var statusCount : TextView
    private lateinit var viewStatusLL : LinearLayout
    private lateinit var userDiscpTV:SeeMoreTextView
    private lateinit var userVideoFrams: FrameLayout
    private lateinit var userVideoStoryVV : VideoView
    private lateinit var userStatusImgIV :ImageView

    private var viewPostModel: GroupPostModel? = null
    private lateinit var listener : CommunityStore

    var statusImg :String = ""
    /**seen the user*/
    private var seeUserModel: ArrayList<StatusSeenData> = ArrayList()
    private var seenCount:String? = null

    private val viewPostCommunityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }
    var database: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null


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
        return inflater.inflate(R.layout.fragment_user_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader(viewPostCommunityViewModel)
        inive(view)

    }

    private fun inive(v: View) {
        //seenCount = "0 Viewers..."
        viewPostCommunityViewModel.doGetViewPost(groupPostId!!)

        userPulishNameTV = v.findViewById(R.id.userPulishNameTV)
        userPulished_atTV = v.findViewById(R.id.userPulished_atTV)
        userPulisherImgCIV = v.findViewById(R.id.userPulisherImgCIV)
        eidtMenu = v.findViewById(R.id.eidtMenu)
        userstoriesSV = v.findViewById(R.id.userstoriesSV)
        statusCount = v.findViewById(R.id.statusCount)
        viewStatusLL = v.findViewById(R.id.viewStatusLL)
        userDiscpTV = v.findViewById(R.id.userDiscpTV)
        userVideoFrams = v.findViewById(R.id.userVideoFrams)
        userVideoStoryVV = v.findViewById(R.id.userVideoStoryVV)
        userStatusImgIV = v.findViewById(R.id.userStatusImgIV)
        Log.d("mTag","status group Post id = $groupPostId")
        onTouchListener(v)
    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        viewPostCommunityViewModel.viewPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.post != null) {
                    viewPostModel = it.post!!
                    setData(viewPostModel!!)
                }
            }
        })
        viewPostCommunityViewModel.statusSeeLiveData.observe(this, Observer {
            if (it != null){
                setViewSeePost(it)
                seenCount = "${it.size} Viewers..."
                statusCount.text = seenCount

            }
        })


        viewPostCommunityViewModel.deleteCommentLiveData.observe(this, Observer {
            if (it != null) {
                //showSnackbar(it.message!!)
            }
        })
    }

    private fun setViewSeePost(seePostList: ArrayList<StatusSeenData>) {
        for (seeStatus in seePostList){
            if (postContains(seeUserModel,seeStatus.user_id!!)){
                Log.d("mTag","User Already Log")
            }else{
                seeUserModel.add(seeStatus)
            }
        }

    }

    fun postContains(list: ArrayList<StatusSeenData>,userId:String): Boolean {
        for (item in list) {
            if (item.user_id.equals(userId)) {
                return true
            }
        }
        return false
    }

    private fun setData(viewPostModel: GroupPostModel) {
        viewPostCommunityViewModel.getStatusLiveList(viewPostModel._id!!)

        if (viewPostModel.user_id!!.profile_url.isNullOrEmpty()) {
            userPulisherImgCIV.load(R.drawable.profile_iv)
        } else {
            userPulisherImgCIV.load(viewPostModel.user_id!!.profile_url)
        }
        if (MyCommunityApp.getUser(requireActivity())!!._id.equals(viewPostModel.user_id!!._id)) {
            eidtMenu.visibility = View.VISIBLE
        } else {
            eidtMenu.visibility = View.GONE
        }
        val isDesp = viewPostModel.description
        userDiscpTV.setContent(isDesp)
        userDiscpTV.setTextMaxLength(3)

        for (statImg in viewPostModel.image_url!!){
            if (viewPostModel.image_url!!.size == 1){
                statusImg = statImg
            }

        }
        if (statusImg.endsWith(".mp4")){
            userStatusImgIV.visibility = View.GONE
            userVideoFrams.visibility = View.VISIBLE
            setVideoPath(statusImg,userVideoStoryVV)
        }else{
            userVideoFrams.visibility = View.GONE
            userStatusImgIV.visibility = View.VISIBLE
            userStatusImgIV.load(statusImg)
            userstoriesSV.setStoryDuration(10000L);

            userstoriesSV.setStoriesCount(1);
            userstoriesSV.setStoryDuration(10000L)
            userstoriesSV.startStories();
        }

        userPulishNameTV.text = viewPostModel.user_id!!.full_name

        val time: Long = viewPostModel.published_at!!.toLong()


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
                userPulished_atTV.text = strPostTime
            }
            minute < 60 -> {
                strPostTime = if (minute == 1L) {
                    "$minute minute ago"
                } else {
                    "$minute minutes ago"
                }
                userPulished_atTV.text = strPostTime
            }
            hour < 24 -> {
                strPostTime = if (hour == 1L) {
                    "$hour hour ago"
                } else {
                    "$hour hours ago"
                }
                userPulished_atTV.text = strPostTime
            }
            else -> {
                userPulished_atTV.text = DateHelper.getFullFormatDate(time)
            }
        }


        userstoriesSV.setStoriesListener(object : StoriesProgressView.StoriesListener {
            override fun onNext() {}

            override fun onPrev() {}

            override fun onComplete() {
                listener.onBack()
                requireActivity().viewModelStore.clear()
            }
        })

        eidtMenu.setOnClickListener { openPopupMenu(it) }
        viewStatusLL.setOnClickListener {
            userstoriesSV.pause()
            userVideoStoryVV.pause()
            openBottomSheet()

        }




    }
    private fun openBottomSheet(){
        val addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance(
            seenCount!!,seeUserModel,userstoriesSV,userVideoStoryVV,mCurrentPosition
        )
        addPhotoBottomDialogFragment.show(
            childFragmentManager,TAG
        )
    }
    fun onTouchListener(v:View){
       v.setOnTouchListener(object : OnTouchListener {
           override fun onTouch(v: View?, event: MotionEvent?): Boolean {
               when (event!!.action) {
                   MotionEvent.ACTION_DOWN -> {
                       userstoriesSV.pause()
                       if (userVideoStoryVV.isPlaying){
                           userVideoStoryVV.pause()
                           userVideoStoryVV.pause()
                           mCurrentPosition = userVideoStoryVV.currentPosition
                           return false
                       }

                   }
                   MotionEvent.ACTION_UP -> {
                       userVideoStoryVV.resume()
                       if (!userVideoStoryVV.isPlaying){
                           userVideoStoryVV.seekTo(mCurrentPosition)
                           userVideoStoryVV.start()
                           userVideoStoryVV.resume()
                           return false
                       }

                   }
               }
               return false
           }
       })
    }
    private fun openPopupMenu(view: View) {

        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menuInflater.inflate(R.menu.user_status_delete, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_user_story_delete ->
                    openDeleteAlertDialog()
            }
            true
        })
        popupMenu.show()
    }
    private fun setVideoPath(urlPath:String?, videoPostViewVV:VideoView) {
        // Show the "Buffering..." message while the video loads.
        val uri = Uri.parse(urlPath)
        videoPostViewVV.visibility = View.VISIBLE
        if (uri != null) {
            videoPostViewVV.setVideoURI(uri)
        }
//        val mediaController = MediaController(requireActivity())
//        videoPostViewVV.setMediaController(mediaController)
//        mediaController.setAnchorView(videoPostViewVV)
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
                    userstoriesSV.setStoryDuration(timeInMillisec);

                    userstoriesSV.setStoriesCount(1);
                    userstoriesSV.setStoryDuration(timeInMillisec)

                    // Start playing!
                    videoPostViewVV.start()
                    userstoriesSV.startStories();
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
    private fun openDeleteAlertDialog() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.delete_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                viewPostCommunityViewModel.doDeletePost(viewPostModel!!._id!!)
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupPostId =it.getString(MyCommunityAppConstants.GROUP_ID)
            Log.d("mTag","selectPos = $groupPostId")
        }
    }

    companion object {
        const val TAG_ACTION = "ActionBottomDialog"
        @JvmStatic
        fun newInstance(groupPostId: String?) =
            UserStatusFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.GROUP_ID,groupPostId)

                }
            }
    }
}