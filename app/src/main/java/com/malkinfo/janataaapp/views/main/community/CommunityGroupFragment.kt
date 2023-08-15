package com.malkinfo.janataaapp.views.main.community

import MyCommunityApp
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.GroupMembersAdapter
import com.malkinfo.janataaapp.adapters.GroupPostAdapter
import com.malkinfo.janataaapp.adapters.GroupSuggestionAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.TopSheetBehavior
import com.malkinfo.janataaapp.helpers.TopSheetBehavior.TopSheetCallback
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.GroupMemberModel
import com.malkinfo.janataaapp.models.community.GroupModel
import com.malkinfo.janataaapp.models.community.GroupPostModel
import com.malkinfo.janataaapp.utitlis.ShareUtils
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class CommunityGroupFragment : MyBaseFragment() {

    private var aboutText: String = ""
    private var onTopSheetOpenClicked: Boolean? = true
    private var currentPosition: Int? = null
    var groupId: String? = null
    var groupName: String? = null

    private var groupMembersModel: ArrayList<GroupMemberModel> = ArrayList()
    private var groupSuggestionModel: ArrayList<GroupModel> = ArrayList()
    private var groupPostModel: ArrayList<GroupPostModel> = ArrayList()

    private lateinit var groupMembersAdapter: GroupMembersAdapter
    private lateinit var groupSuggestionAdapter: GroupSuggestionAdapter
    private lateinit var groupPostAdapter: GroupPostAdapter
    private lateinit var communityGroupInterface: CommunityStore
    var postImageUrl: ArrayList<String> = ArrayList()

    /**get All Group*/
    private var groupModel: ArrayList<GroupModel> = ArrayList()

    private var totalLimit: Int? = null
    var page = 1
    var strPageLimit = 10
    /**set id*/
    private lateinit var groupTitleTV:TextView
    private lateinit var noPostFoundTV:TextView
    private lateinit var groupPostRV:RecyclerView
    private lateinit var groupMembersRV:RecyclerView
    private lateinit var otherGroupRV:RecyclerView
    private lateinit var groupTitleLL:LinearLayout
    private lateinit var aboutTV:TextView
    private lateinit var topSheetFL: FrameLayout
    private lateinit var backIV:ImageView
    private lateinit var groupMeetingIV:ImageView
    private lateinit var notificationIV:ImageView
    //private lateinit var arrowUpIV:ImageView
    private lateinit var arrowDownIV:ImageView
    private lateinit var commGroupV:View


    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            communityGroupInterface = context as CommunityStore
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                groupId = it.getString(MyCommunityAppConstants.GROUP_ID)
                groupName = it.getString(MyCommunityAppConstants.GROUP_NAME)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inisIds(view)
        setUpLoader(communityViewModel)
        doGetGroupPost()
    }
    private fun inisIds(v:View){
        groupTitleTV = v.findViewById(R.id.groupTitleTV)
        noPostFoundTV = v.findViewById(R.id.noPostFoundTV)
        groupPostRV = v.findViewById(R.id.groupPostRV)
        groupMembersRV = v.findViewById(R.id.groupMembersRV)
        otherGroupRV = v.findViewById(R.id.otherGroupRV)
        groupTitleLL = v.findViewById(R.id.groupTitleCL)
        aboutTV = v.findViewById(R.id.aboutTV)
        topSheetFL = v.findViewById(R.id.topSheetFL)
        backIV = v.findViewById(R.id.backIV)
        groupMeetingIV = v.findViewById(R.id.groupMeetingIV)
        notificationIV = v.findViewById(R.id.notificationIV)
        arrowDownIV = v.findViewById(R.id.arrowDownIV)
        commGroupV = v.findViewById(R.id.commGroupV)
    }

    private fun doGetGroupPost() {
        groupTitleTV.text = groupName
        page = 1
        groupPostModel.clear()
        communityViewModel.getGroupPosts(groupId!!, page.toString(), strPageLimit.toString())
        communityViewModel.getGroupList("?","?")
    }


    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        communityViewModel.groupLiveData.observe(this, Observer {
            if (isAdded) {
                if (it.total_count != null) {
                    //  totalLimit = it.total_count
                }
                if (it.groups!!.any()) {
                    groupModel.clear()
                    groupAddView(it.groups!!)
                    addSuggestionGroup(it.groups!!)
                    println("---model size---" + groupModel.size)
                    println("---group size---" + it.groups!!.size)
                    Log.d("mTag","group Model Size = ${groupModel.size}")
                }
            }
        })
        communityViewModel.updatePostLiveData.observe(this, Observer {
            if (it != null) {
                Log.d("mTag","msg = ${it.message}")
                communityViewModel.getGroupPosts(groupId!!, page.toString(), strPageLimit.toString())
            }
        })

        communityViewModel.groupPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                    if (page == 1) {
                        initViews()
                    }
                }
                if (it.posts!!.any()) {
                    if (page == 1) {
                        groupPostModel.clear()
                        addGroupPost(it.posts!!)
                        //groupPostModel.addAll(it.posts!!)
                        groupPostAdapter.notifyDataSetChanged()
                    } else {
                        addGroupPost(it.posts!!)
                        //groupPostModel.addAll(it.posts!!)
                        groupPostAdapter.notifyDataSetChanged()
                    }
                    noPostFoundTV.visibility= GONE
                }else{
                    noPostFoundTV.visibility= VISIBLE
                    groupPostModel.clear()
                    groupPostAdapter.notifyDataSetChanged()
                }

                if (it.groups!!.any()) {
                    groupMembersModel.clear()
                    groupMembersModel.addAll(it.members!!)

                }

                if (it.members!!.any()) {
                    //groupSuggestionModel.clear()
                    //addSuggestionGroup(groupModel)

                }

                if (it.about != null) {
                    aboutText = it.about!!
                }
            }
        })

        /*communityViewModel.likeGroupPostLiveData.observe(this, Observer {

            //showSnackbar(it.message!!)

        })*/

    }
    private fun groupAddView(groups: ArrayList<GroupModel>) {
        for(groups_item in groups){
            when(groups_item._id){
                MyCommunityAppConstants.STATUS_STORY_GROUP_ID->{}
                MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP->{}
                else->{
                    if (groupContains(groupModel,groups_item._id)){
                       // Log.d("mTag","This Group is already add")
                    }else{
                        if (groups_item.is_joined == true){
                            groupModel.add(groups_item)
                            //Log.d("mTag","add group list = ${groupModel.size}")
                        }
                    }

                }
            }

        }

    }

    private fun addGroupPost(posts: ArrayList<GroupPostModel>) {
        for (is_post_item in posts){
            if (postContains(groupPostModel,is_post_item._id)){
                //Log.d("mTag","this post already add")
            }else{
                groupPostModel.add(is_post_item)
            }
        }

    }
    fun postContains(list: ArrayList<GroupPostModel>, name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }
    fun groupContains(list: ArrayList<GroupModel>, name: String?): Boolean {
        for (item in list) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }

    private fun addSuggestionGroup(groups: ArrayList<GroupModel>) {
        for (isGroup in groups){
            when(isGroup._id){
                MyCommunityAppConstants.STATUS_STORY_GROUP_ID->{}
                MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP->{}
                else->{
                    if (isGroup._id == groupId){
                       // Log.d("mTag","this is group")
                    }else{
                        groupSuggestionModel.add(isGroup)
                    }

                }
            }
        }


    }


    private fun initViews() {

        groupPostRV.layoutManager = LinearLayoutManager(requireActivity())
        groupPostAdapter = GroupPostAdapter(requireActivity(),groupPostModel,totalLimit)
        groupPostRV.adapter = groupPostAdapter


        groupPostRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!groupPostRV.canScrollVertically(1)) {
                    val limit: Int = totalLimit!! / strPageLimit
                    if (page <= limit) {
                        page += 1
                        communityViewModel.getGroupPosts(
                            groupId!!,
                            page.toString(),
                            strPageLimit.toString()
                        )
                    }
                }
            }
        })

        groupMembersRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        groupMembersAdapter = GroupMembersAdapter(requireActivity(), groupMembersModel)
        groupMembersRV.adapter = groupMembersAdapter
        // groupMembersAdapter.notifyDataSetChanged()

        otherGroupRV.layoutManager = GridLayoutManager(context, 3)
        groupSuggestionAdapter = GroupSuggestionAdapter(requireActivity(), groupSuggestionModel)
        otherGroupRV.adapter = groupSuggestionAdapter
        //  groupSuggestionAdapter.notifyDataSetChanged()

        groupTitleLL.setOnClickListener {
            groupMembersAdapter.notifyDataSetChanged()
            groupSuggestionAdapter.notifyDataSetChanged()
            aboutTV.text = aboutText
            openGroupDetailsTopSheet()
            TopSheetBehavior.from(topSheetFL).state = TopSheetBehavior.STATE_DRAGGING
        }

        backIV.setOnClickListener {
            requireActivity().onBackPressed()
        }

        groupMeetingIV.setOnClickListener {

            communityGroupInterface.onGroupMeetingClicked(groupId)
        }

        notificationIV.setOnClickListener {
            communityGroupInterface.onNotificationClicked()
        }



        groupPostAdapter.onFavouriteClicked = { pos ->
            currentPosition = pos
            communityViewModel.doLikeGroupPost(groupPostModel[pos]._id!!)
            groupPostModel.get(pos).is_liked = !groupPostModel.get(pos).is_liked!!

            if (groupPostModel.get(currentPosition!!).is_liked!!) {

                groupPostModel.get(currentPosition!!).likes_count =
                    groupPostModel.get(currentPosition!!).likes_count!! + 1
                groupPostAdapter.notifyDataSetChanged()
            } else {
                groupPostModel.get(currentPosition!!).likes_count =
                    groupPostModel.get(currentPosition!!).likes_count!! - 1
                groupPostAdapter.notifyDataSetChanged()
            }
        }

        groupPostAdapter.onItemClicked = { pos ->
            communityGroupInterface.onPostItemClicked(groupPostModel[pos]._id)
        }

        groupPostAdapter.onMoreOptionClicked = { pos, view ->
            reportPopupMenu(pos, view)
        }
        groupPostAdapter.onMoreOptionUserClicked = {pos , view ->
            openPopupMenu(pos,view)

        }

        groupPostAdapter.onWritePostClicked = { pos ->
            communityGroupInterface.onWritePostClicked(groupId,postImageUrl)
        }

        groupPostAdapter.onProfileClicked = { pos ->
            communityGroupInterface.onProfileClicked(
                MyCommunityApp.getUser(requireActivity())!!._id,
                groupId
            )
        }

        groupPostAdapter.onOtherProfileClicked = { pos ->
            communityGroupInterface.onOtherProfileClicked(
                groupPostModel[pos].user_id!!._id,
                groupId)
        }

        groupPostAdapter.onShareClicked = { pos ->
             createDynamicLink(groupPostModel[pos]._id,
                 groupPostModel[pos].user_id!!.profile_url,
                 groupPostModel[pos].user_id!!.full_name,
                 groupPostModel[pos].description
             )
        }


        groupSuggestionAdapter.onGroupSuggestionClicked = { pos ->

            if (groupSuggestionModel[pos].is_joined!!) {
                page = 1
                groupId = groupSuggestionModel[pos]._id!!

                communityViewModel.getGroupPosts(groupId!!, page.toString(), strPageLimit.toString())
                groupTitleTV.text = groupSuggestionModel[pos].group
                groupMeetingIV.visibility = VISIBLE
                //  notificationIV.visibility = VISIBLE
                backIV.visibility = VISIBLE
                commGroupV.visibility = VISIBLE


                TopSheetBehavior.from(topSheetFL).state = TopSheetBehavior.STATE_COLLAPSED
            } else {
                onJoinGroupAlert(pos)
            }

        }


    }

    private fun reportPopupMenu(pos: Int, view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_report ->
                    communityGroupInterface.onReportPostClicked(groupPostModel[pos]._id!!)
            }
            true
        })
        popupMenu.show()
    }

    private fun openGroupDetailsTopSheet() {

        TopSheetBehavior.from(topSheetFL).setTopSheetCallback(object : TopSheetCallback() {
            override fun onStateChanged(topSheet: View, newState: Int) {


            }

            override fun onSlide(topSheet: View, slideOffset: Float, isOpening: Boolean?) {

                when {

                    slideOffset == 0f -> {

                        groupMeetingIV.visibility = VISIBLE
                        //    notificationIV.visibility = VISIBLE
                        backIV.visibility = VISIBLE
                        commGroupV.visibility = VISIBLE

                        arrowDownIV.load(R.drawable.arrow)

                    }
                    else -> {
                        arrowDownIV.setOnClickListener {

                            groupMeetingIV.visibility = VISIBLE
                            //   notificationIV.visibility = VISIBLE
                            backIV.visibility = VISIBLE
                            commGroupV.visibility = VISIBLE

                            arrowDownIV.load(R.drawable.arrow)

                            TopSheetBehavior.from(topSheetFL).state = TopSheetBehavior.STATE_COLLAPSED
                        }

                        groupMeetingIV.visibility = INVISIBLE
                        //  notificationIV.visibility = INVISIBLE
                        backIV.visibility = GONE
                        commGroupV.visibility = GONE

                        arrowDownIV.load(R.drawable.arrow_up)
                    }

                }
            }

        })

        groupMembersAdapter.onViewAllClicked = { pos ->
            communityGroupInterface.onViewAllGroupMembersClicked(groupId)
        }

    }

    private fun onJoinGroupAlert(pos: Int) {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.join_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                page = 1
                groupId = groupSuggestionModel[pos]._id!!
                dialogInterface.dismiss()
                communityViewModel.doJoinGroupSuggest(groupId!!)
                communityViewModel.getGroupPosts(
                    groupId!!,
                    page.toString(),
                    strPageLimit.toString()
                )
                groupTitleTV.text = groupSuggestionModel[pos].group
                groupMeetingIV.visibility = VISIBLE
                //  notificationIV.visibility = VISIBLE
                backIV.visibility = VISIBLE
                commGroupV.visibility = VISIBLE
                arrowDownIV.load(R.drawable.arrow)

                TopSheetBehavior.from(topSheetFL).state = TopSheetBehavior.STATE_COLLAPSED
                //communityGroupInterface.onCommunityGroupItemClicked(groupSuggestionModel[pos]._id!!, groupSuggestionModel[pos].group!!)
            })
    }
    private fun deletePost(group_ids:ArrayList<String>, post_id:String) {
        val isListItem :ArrayList<String> = ArrayList()
        for (item in groupModel){
            for (groups in group_ids){
                if (item._id == groups){
                    isListItem.add(item.group!!)
                }
            }

        }
        //   val groupList :Array<String> = isListItem.toTypedArray()
        val checkedItems = BooleanArray(group_ids.size)
        // initialise the alert dialog builder
        val groupDialog = AlertDialog.Builder(requireActivity())

        for (i in group_ids.indices){
            checkedItems[i] = true
        }

    //    val selectedItems :ArrayList<String> = groupIds
        val unSelectedItems :ArrayList<String> = group_ids
        // set the title for the alert dialog
        groupDialog.setTitle("Choose the Group you want to Edit...")
        // set the icon for the alert dialog
        groupDialog.setIcon(R.drawable.janataalogo)

        // now this is the function which sets the alert dialog for multiple item selection ready
        groupDialog.setMultiChoiceItems(
            isListItem.toTypedArray(),
            checkedItems
        ) { dialog: DialogInterface?, which: Int, isChecked: Boolean ->
            checkedItems[which] = isChecked
            //val currentItem = selectedItems[which]
        }

        // alert dialog shouldn't be cancellable
        groupDialog.setCancelable(false)

        // handle the positive button of the dialog
        groupDialog.setPositiveButton(
            "Done"
        ) { dialog: DialogInterface?, which: Int ->
         //   val seletGroupList :ArrayList<GroupModel> = ArrayList()
            val unseletGroupList :ArrayList<String> = ArrayList()

            for (i in checkedItems.indices) {
                if (checkedItems[i]) {
                    //Log.d("mTag","selete group $i")
                }else{
                    unseletGroupList.add(unSelectedItems[i])

                }
            }
            if (unseletGroupList.size == 0){
                showSnackbar("I update post in all group")
                openDeleteAlertDialog(post_id)
            }else{

                deleteSeleteGroups(unseletGroupList,post_id)
                showSnackbar("I am deleting post...")
            }
            dialog!!.dismiss()
        }

        // handle the negative button of the alert dialog
        groupDialog.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface?, which: Int ->
            dialog!!.dismiss()
        }

        // handle the neutral button of the dialog to clear the selected items boolean checkedItem
        groupDialog.setNeutralButton(
            "Clear All"
        ) { dialog: DialogInterface?, which: Int ->
            Arrays.fill(
                checkedItems,
                false
            )
            Toast.makeText(requireActivity(),"Please Select least one group...",
                Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()
        }

        // create the builder
        groupDialog.create()

        // create the alert dialog with the alert dialog builder instance
        val alertDialog = groupDialog.create()
        alertDialog.show()

    }
    private fun deleteSeleteGroups(groups:ArrayList<String>,post_id:String){
            showConfirmation(getString(R.string.no),
                getString(R.string.yes),
                "",
                getString(R.string.delete_alert),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val main = JSONObject()
                    val groupArray = JSONArray()

                    for (i in 0 until groups.size){
                        groupArray.put(groups[i])
                    }

                    try {
                        main.put("group_id",groupArray)
                        val jsonParser = JsonParser()
                        val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                        communityViewModel.doUpdatePost(post_id,gsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                })
    }

    private fun createDynamicLink(
        id: String?,
        userProfileImg:String?,
        profileName:String?,
        description: String?) {

     val dynamicLin = FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(Uri.parse("https://janataaapp.page.link/"+id))
            .setDomainUriPrefix("https://janataaapp.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()
            .addOnSuccessListener { command ->
                val shortLink = command.shortLink
                val flowchartLink = command.previewLink
                val descrip = "See this Post..."+"\n===================\n"+
                        description +"\n====================="+ "\n\nJoin Community House Now...\n" + shortLink.toString()+"\n\n\n\n"

                ShareUtils.sharePost(requireActivity(),
                    userProfileImg,profileName,description,descrip)
               // Toast.makeText(context,"I am click share FirebaseDynamicLinks",Toast.LENGTH_SHORT).show()

//                Log.i(
//                    TAG,
//                    "createDynamicLink: $shortLink ---> $flowchartLink"
//                )
            }.addOnFailureListener { command ->
              //  Log.i(TAG, "createDynamicLink: " + command.message)
            }
       // Log.d("mTag","is Link success ${dynamicLin.isSuccessful}")
    }
    private fun openPopupMenu(pos: Int, view: View) {

        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menuInflater.inflate(R.menu.user_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit ->{
                    communityGroupInterface.onEditPostClicked(
                        groupPostModel[pos].group_id,
                        groupPostModel[pos].description,
                        groupPostModel[pos].image_url,groupPostModel[pos]._id,
                        true
                    )
                }


                R.id.action_delete ->{
                    if (groupPostModel[pos].group_id!!.size == 1){
                        openDeleteAlertDialog(groupPostModel[pos]._id!!)

                    }else{
                        deletePost(groupPostModel[pos].group_id!!,groupPostModel[pos]._id!!)
                    }


                }

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
                communityViewModel.doDeletePost(groupPostModel[pos]._id!!)
            })
    }
    private fun openDeleteAlertDialog(post_id: String) {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.delete_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                communityViewModel.doDeletePost(post_id)
            })
    }

    companion object {

        @JvmStatic
        fun newInstance(id: String, name: String) =
            CommunityGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.GROUP_ID, id)
                    putString(MyCommunityAppConstants.GROUP_NAME, name)
                }
            }
    }

}