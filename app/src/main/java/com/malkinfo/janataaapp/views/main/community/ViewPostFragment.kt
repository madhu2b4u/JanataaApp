package com.malkinfo.janataaapp.views.main.community

import MyCommunityApp
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.CommentAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.DateHelper
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.CommentModel
import com.malkinfo.janataaapp.models.community.GroupPostModel
import com.malkinfo.janataaapp.utitlis.SeeMoreTextView
import com.malkinfo.janataaapp.utitlis.ShareUtils
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.tasks.asDeferred
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit


class ViewPostFragment : MyBaseFragment() {
    private lateinit var comment: String
    private lateinit var strPostTime: String
    var groupPostId: String? = null
    private lateinit var viewPostInterface: CommunityStore
    private lateinit var commentAdapter: CommentAdapter
    private var commentModel: ArrayList<CommentModel> = ArrayList()
    private var viewPostModel: GroupPostModel? = null

    private var totalLimit: Int? = null
    var page = 1
    var strPageLimit = 10

    /**set Id*/
    private lateinit var likesCountTV:TextView
    private lateinit var commentsCountTV:TextView
    private lateinit var writeCommentED:EditText
    private lateinit var noCommentFoundTV:TextView
    private lateinit var postUserIV: CircleImageView
    private lateinit var moreOptionsIV:ImageView
    private lateinit var postUserTV:TextView
    private lateinit var postTV:SeeMoreTextView
    private lateinit var viewsCountTV:TextView
    private lateinit var viewPostFavCB:CheckBox
    private lateinit var postTimeTV:TextView
    private lateinit var post_ViewBackIV:ImageView
    private lateinit var addCommentBT:ImageView
    private lateinit var commentsRV:RecyclerView
    private lateinit var ivShareViewPost:ImageView


    private lateinit var listener : CommunityStore



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            viewPostInterface = context as CommunityStore
        }
        if(context is CommunityStore){
            listener = context as CommunityStore
        }
    }



    private val viewPostCommunityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                groupPostId = it.getString(MyCommunityAppConstants.GROUP_POST_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return inflater.inflate(R.layout.fragment_view_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsIds(view)
        setUpLoader(viewPostCommunityViewModel)
        initViews(view)
    }
    private fun initsIds(v:View){
        likesCountTV = v.findViewById(R.id.likesCountTV)
        commentsCountTV = v.findViewById(R.id.commentsCountTV)
        writeCommentED = v.findViewById(R.id.writeCommentED)
        noCommentFoundTV = v.findViewById(R.id.noCommentFoundTV)
        postUserIV = v.findViewById(R.id.postUserIV)
        moreOptionsIV = v.findViewById(R.id.moreOptionsIV)
        postUserTV = v.findViewById(R.id.postUserTV)
        postTV = v.findViewById(R.id.postTV)
        viewsCountTV = v.findViewById(R.id.viewsCountTV)
        viewPostFavCB = v.findViewById(R.id.viewPostFavCB)
        postTimeTV = v.findViewById(R.id.postTimeTV)
        post_ViewBackIV = v.findViewById(R.id.post_ViewBackIV)
        addCommentBT = v.findViewById(R.id.addCommentBT)
        commentsRV = v.findViewById(R.id.commentsRV)
        ivShareViewPost = v.findViewById(R.id.ivShareViewPost)

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

        viewPostCommunityViewModel.viewPostLikeGroupPostLiveData.observe(
            this,
            Observer {
                //showSnackbar(it.message!!)
                if (viewPostModel!!.is_liked!!) {
                    viewPostModel!!.likes_count = viewPostModel!!.likes_count!! + 1
                } else {
                    viewPostModel!!.likes_count = viewPostModel!!.likes_count!! - 1
                }
                likesCountTV.text = viewPostModel!!.likes_count.toString() + "\t" + "Likes"
            })

        viewPostCommunityViewModel.commentLiveData.observe(this, Observer {
            if (it != null) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                }
                if (it.comments!!.any()) {
                    if(page==1){
                        commentModel.clear()
                        commentModel.addAll(it.comments!!)
                        initComments()
                        commentAdapter.notifyDataSetChanged()
                    } else {
                        commentModel.addAll(it.comments!!)
                        commentAdapter.notifyDataSetChanged()
                    }

                }
            }
        })

        viewPostCommunityViewModel.deleteCommentLiveData.observe(this, Observer {
            if (it != null) {
                //showSnackbar(it.message!!)
                viewPostModel!!.comments_count = viewPostModel!!.comments_count!! - 1
                commentsCountTV.text = viewPostModel!!.comments_count.toString() + "\t" + "Comments"
            }
        })

        viewPostCommunityViewModel.addCommentLiveData.observe(this, Observer {
            if (it != null) {
              //  showSnackbar(it.message!!)
                viewPostModel!!.comments_count = viewPostModel!!.comments_count!! + 1
                commentsCountTV.text = viewPostModel!!.comments_count.toString() + "\t" + "Comments"
                writeCommentED.setText("")
                noCommentFoundTV.visibility = GONE
                viewPostCommunityViewModel.doGetComments(groupPostId!! ,page.toString() ,strPageLimit.toString())
            }
        })
    }


    private fun setData(viewPostModel: GroupPostModel) {

        if (viewPostModel.user_id != null) {
            if (viewPostModel.user_id!!.profile_url.isNullOrEmpty()) {
                postUserIV.load(R.drawable.profile_iv)
            } else {
                postUserIV.load(viewPostModel.user_id!!.profile_url)
            }
            if (MyCommunityApp.getUser(requireActivity())!!._id.equals(viewPostModel.user_id!!._id)) {
                moreOptionsIV.visibility = GONE
            } else {
                moreOptionsIV.visibility = VISIBLE
            }

            postUserTV.text = viewPostModel.user_id!!.full_name
            val isDesp = viewPostModel.description
            postTV.setContent(isDesp)
            postTV.setTextMaxLength(3)

            commentsCountTV.text = viewPostModel.comments_count.toString() + "\t" + "Comments"
            likesCountTV.text = viewPostModel.likes_count.toString() + "\t" + "Likes"
            viewsCountTV.text = viewPostModel.views_count.toString() + "\t" + "Views"
            viewPostFavCB.isChecked = viewPostModel.is_liked!!
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
                    postTimeTV.text = strPostTime
                }

                minute < 60 -> {
                    strPostTime = if (minute == 1L) {
                        "$minute minute ago"
                    } else {
                        "$minute minutes ago"
                    }
                    postTimeTV.text = strPostTime
                }

                hour < 24 -> {
                    strPostTime = if (hour == 1L) {
                        "$hour hour ago"
                    } else {
                        "$hour hours ago"
                    }
                    postTimeTV.text = strPostTime
                }

                else -> {
                    postTimeTV.text = DateHelper.getFullFormatDate(time)
                }
            }
        }

        ivShareViewPost.setOnClickListener {
            //Toast.makeText(context,"I am click share post", Toast.LENGTH_SHORT).show()
            if (viewPostModel.user_id != null){
                createDynamicLink(viewPostModel._id,
                    viewPostModel.user_id!!.profile_url,
                    viewPostModel.user_id!!.full_name,
                    viewPostModel.description
                )
            }


        }

    }

    private fun initViews(view: View) {

        viewPostCommunityViewModel.doGetViewPost(groupPostId!!)
        viewPostCommunityViewModel.doGetComments(groupPostId!!,page.toString() ,strPageLimit.toString())

        post_ViewBackIV.setOnClickListener {
          listener.onBack()
            requireActivity().viewModelStore.clear()
        }

        moreOptionsIV.setOnClickListener {
            reportPopupMenu(view, groupPostId!!)
        }

        viewPostFavCB.setOnClickListener {
            viewPostCommunityViewModel.doViewPostLikeGroupPost(groupPostId!!)
            viewPostModel!!.is_liked = !viewPostModel!!.is_liked!!
        }

        addCommentBT.setOnClickListener {

            if (writeCommentED.text.isNotEmpty()) {
                //Do add comment
                val main = JSONObject()
                try {
                    comment = writeCommentED.text.toString()
                    main.put("comment", comment)
                    main.put("post_id", groupPostId)
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    viewPostCommunityViewModel.doAddComment(gsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun initComments() {
/*if (commentModel.any()) {
            noCommentFoundTV.visibility = GONE
        } else {
            noCommentFoundTV.visibility = VISIBLE
        }*/

        commentAdapter = CommentAdapter(requireActivity(), commentModel,totalLimit)
        commentsRV.layoutManager = LinearLayoutManager(activity)
        commentsRV.adapter = commentAdapter

        commentsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!commentsRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / strPageLimit
                    if (page <= limit) {
                        page += 1
                        viewPostCommunityViewModel.doGetComments(groupPostId!! ,page.toString() ,strPageLimit.toString())
                    }
                }
            }
        })

        commentAdapter.onDeleteClicked = { pos ->
            showConfirmation(getString(R.string.no),
                getString(R.string.yes),
                "",
                getString(R.string.comment_alert),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    viewPostCommunityViewModel.doDeleteComment(commentModel[pos]._id!!)
                    commentModel.removeAt(pos)
                    commentAdapter.notifyItemRemoved(pos)
                    commentAdapter.notifyDataSetChanged()
                })

        }
        adapterOv()



    }

    fun adapterOv(){
        commentAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (commentAdapter.getItemCount() == 0) {
                    commentsRV.visibility = View.GONE
                    noCommentFoundTV.visibility = View.VISIBLE
                } else {
                    commentsRV.visibility = View.VISIBLE
                    noCommentFoundTV.visibility = View.GONE
                }
            }
        })
    }

    private fun reportPopupMenu(view: View, groupPostId: String) {
        val popupMenu = PopupMenu(view.context, moreOptionsIV)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_report ->
                    viewPostInterface.onReportPostClicked(groupPostId!!)
            }
            true
        })
        popupMenu.show()
    }

    private fun createDynamicLink(
        id: String?,
        userProfileImg:String?,
        profileName:String?,
        description: String?) {

       val dynamicLin =  FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://janataaapp.page.link/" +id))
            .setDomainUriPrefix("https://janataaapp.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()
            .addOnSuccessListener { command ->

                val shortLink = command.shortLink
                val flowchartLink = command.previewLink
                val descrip = "See this Post..."+"\n===================\n"+
                        description +"\n====================="+ "\n\nJoin Community House Now...\n" + shortLink.toString()+"\n\n\n\n"

                ShareUtils.sharePost(requireActivity(),userProfileImg,profileName,description,descrip)
               // Toast.makeText(context,"I am click share FirebaseDynamicLinks",Toast.LENGTH_SHORT).show()
/*//                Log.i(
//                    "mTag",
//                    "createDynamicLink: $shortLink ---> $flowchartLink"
//                )*/
            }.addOnFailureListener { command ->


            }
           .addOnCompleteListener {task->
           if (task.isSuccessful){
               val shortDynamicLink = task.result
              // Log.d("mTag","is Link success ${shortDynamicLink}")
           }else{
             //  Log.d("mTag","is Link exception${task.exception}")
           }

           }
        //Log.d("mTag","is Link success ${dynamicLin.asDeferred()}")
    }


    companion object {

        @JvmStatic
        fun newInstance(groupPostId: String?) =
            ViewPostFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.GROUP_POST_ID, groupPostId)
                }
            }
    }



}