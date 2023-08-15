package com.malkinfo.janataaapp.views.main.home

import MyCommunityApp
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.*
import com.malkinfo.janataaapp.adapters.homeadapter.AllUserAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants.STORY_REQUEST_CODE
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.community.FollowerModel
import com.malkinfo.janataaapp.models.community.GroupModel
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel
import com.malkinfo.janataaapp.models.firebaseuser.FireUser
import com.malkinfo.janataaapp.models.launch.User
import com.malkinfo.janataaapp.utils.ImageUtils
import com.malkinfo.janataaapp.utitlis.ShareUtils
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import okio.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */


class HomeFragment : MyBaseFragment() {

    /**Home Interface varble*/
    private lateinit var homeInterface: CommunityStore

    /**Banner Ad ViewPager image slider*/
    private var sliderItemList:ArrayList<HomePostModel> = ArrayList()
    private lateinit var sliderAdpter: BannerSliderAdapter
    private lateinit var sliderHandle: Handler
    private lateinit var sliderRun:Runnable
    /**============================================*/

    /**community User Data*/
    var usersList :ArrayList<FireUser> = ArrayList()
    private lateinit var allUserAdapter :AllUserAdapter
    private  var mAllUserCount:String = ""
//    /**====================================================*/


    /**set status*/
    private lateinit var topStatusAdapter :TopStatusAdapter

    /**====================================================*/

    /**set ID*/
    private lateinit var vPBannerADIV: ViewPager2
    private lateinit var userStoryRec:RecyclerView
    private lateinit var homeNestNSV:NestedScrollView
    //private lateinit var writeStoryPostCV:CircleImageView
    private lateinit var followersRcV:RecyclerView
    private lateinit var userRcV:RecyclerView
    private lateinit var allUserCount:TextView
    private lateinit var followersCountTV :TextView
    private lateinit var viewAllFollowersTV:TextView
    private lateinit var notctHomeIV:ImageView
    private lateinit var showNotiIV:ImageView
    /**====================================================*/
    private var notify :Boolean = false

    /**get user post*/
    private var totalLimit: Int? = null
    var page = 1
    var strPageLimit = 10

    var groupPage = 1
    /**set follower */
    var followerCount = 0
    private var profileFollowerModel: ArrayList<FollowerModel> = ArrayList()
    private lateinit var profileFollowerAdapter: ProfileFollowerAdapter
    private var profileDetails: User? = null


    private var allGroupIdList:ArrayList<String> = ArrayList()
    private var allUserPostModel: ArrayList<HomePostModel> = ArrayList()
    var storyPostImageUrl: ArrayList<String> = ArrayList()
    /**this check the group joni*/

    var bannerViewcount:Int = 0
    /**====================================================*/
    /**status post*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            homeInterface = context as CommunityStore
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader(communityViewModel)
        getGroupPost()
        initsIDs(view)

    }

    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    private fun initsIDs(v:View){


        vPBannerADIV = v.findViewById(R.id.vPBannerIV)

        userStoryRec = v.findViewById(R.id.userStoryRec)
        homeNestNSV = v.findViewById(R.id.homeNestNSV)
        //writeStoryPostCV = v.findViewById(R.id.writeStoryPostCV)
        followersRcV = v.findViewById(R.id.followerRV)
        userRcV = v.findViewById(R.id.userRcV)
        allUserCount = v.findViewById(R.id.allUserCount)
        followersCountTV = v.findViewById(R.id.followersCountTV)
        viewAllFollowersTV = v.findViewById(R.id.viewAllFollowersTV)
        notctHomeIV = v.findViewById(R.id.notctHomeIV)
        showNotiIV = v.findViewById(R.id.showNotiIV)
        initViews()

    }
    private fun getGroupPost(){
        page = 1
        communityViewModel.getStatusPosts(MyCommunityAppConstants.STATUS_STORY_GROUP_ID,page.toString(),strPageLimit.toString())
        communityViewModel.getHomePosts(MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP,"?","?")
        communityViewModel.doGetUserDetails(MyCommunityApp.getUser(requireActivity())!!._id!!,
            MyCommunityAppConstants.STATUS_STORY_GROUP_ID,"?","?")
        communityViewModel.getUserList()
        Log.d("mTag","I am STATUS_STORY_GROUP_ID = ${MyCommunityAppConstants.STATUS_STORY_GROUP_ID}")
    }

    private fun setStatusList(){
        topStatusAdapter = TopStatusAdapter(requireActivity(),totalLimit)
        val layoutManage = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        Log.d("mTag"," i am setStatusList allUserPostModel ${allUserPostModel.size}")

        userStoryRec.layoutManager = layoutManage
        userStoryRec.adapter = topStatusAdapter

        topStatusAdapter.onItemClicked = { post_id,user_id,position ->
            if (user_id == MyCommunityApp.getUser(requireActivity())!!._id){
                homeInterface.onClickUserStatus(post_id)

            }else{
                homeInterface.onClickStatus(post_id)
            }

        }
        topStatusAdapter.storyDelete = {isPos->
            try {
                communityViewModel.deleteSeeStatusLiveList(allUserPostModel[isPos]._id!!)
                communityViewModel.doDeletePost(allUserPostModel[isPos]._id!!)
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("mTag","delete excep = ${e.message}")
            }

        }
        topStatusAdapter.onWriteStatusClicked = {pos, view->
            writeStoryPost(view)
        }

    }

    /**Banner Ad ViewPager fun call*/
    private fun sliderView(){
        sliderItemList = ArrayList()
        sliderAdpter = BannerSliderAdapter(requireActivity(),vPBannerADIV)
        vPBannerADIV.adapter = sliderAdpter
        vPBannerADIV.clipToPadding = false
        vPBannerADIV.clipChildren = false

        vPBannerADIV.currentItem = 2

        vPBannerADIV.offscreenPageLimit = 3
        vPBannerADIV.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        val composPageTarn = CompositePageTransformer()
        composPageTarn.addTransformer(MarginPageTransformer(40))
        composPageTarn.addTransformer(object : ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                val r: Float = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

        })

        vPBannerADIV.setPageTransformer(composPageTarn)
        sliderHandle = Handler()

        sliderRun = object :Runnable{
            override fun run() {
                vPBannerADIV.setCurrentItem(vPBannerADIV.currentItem + 1)
            }

        }

        vPBannerADIV.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandle.removeCallbacks(sliderRun)
                    sliderHandle.postDelayed(sliderRun, 3000)

                }
            }
        )



    }

    override fun initObservers() {
        communityViewModel.viewPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.post != null) {
                    bannerViewcount = it.post!!.views_count!!
                }
            }
        })

        communityViewModel.userLiveData.observe(this, Observer {
            if (it != null){
                if (it.isNotEmpty()){
                    setUserRec()
                    addAllUser(it)

                }else{
                    allUserCount.text ="Nobody People Online..."
                }

            }
        })
        communityViewModel.homePostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                    if (page == 1){
                        sliderView()
                    }
                }
                if (it.posts!!.any()) {
                    if (page == 1) {
                        sliderItemList.clear()
                        getBannerImg(it.posts!!)


                    } else {
                        getBannerImg(it.posts!!)
                    }
                }
            }
        })
        communityViewModel.userDetailsLiveData.observe(this, Observer {
            if (it != null) {
                profileDetails = it.user!!
                if (it.followersCount != null) {
                    followerCount = it.followersCount!!
                    followersCountTV.text = "${it.followersCount!!} Followers..."
                }

                if (it.total_count != null) {
                    totalLimit = it.total_count
                    if (page == 1) {
                        setFollowerRec()
                    }
                }

                if (it.followers!!.any()) {
                    profileFollowerModel.clear()
                    profileFollowerModel.addAll(it.followers!!)
                    profileFollowerAdapter.notifyDataSetChanged()
                }

            }
        })

        communityViewModel.statusPostLiveData.observe(this, Observer {
            if (it != null) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                    if (page == 1){
                        setStatusList()
                    }
                }
                if (it.posts!!.any()) {
                    if (page == 1) {
                        allUserPostModel.addAll(it.posts!!)
                        Log.d("mTag"," i am statusPostLiveData allUserPostModel = ${allUserPostModel.size}")
                        topStatusAdapter.addStatus(allUserPostModel)

                    } else {
                        allUserPostModel.addAll(it.posts!!)
                        topStatusAdapter.addStatus(allUserPostModel)
                    }
                }
            }
        })

        communityViewModel.deletePostLiveData.observe(this, Observer {
            if (it != null) {
                //showSnackbar(it.message!!)
                communityViewModel.getStatusPosts(MyCommunityAppConstants.STATUS_STORY_GROUP_ID,page.toString(),strPageLimit.toString())

            }
        })
        communityViewModel.homeAllGroupLiveData.observe(this, Observer {
            if (isAdded) {
                if (it.total_count != null) {
                    totalLimit = it.total_count
                }
                if (it.groups!!.any()) {
                    if (groupPage == 1) {
                        groupAddView(it.groups!!)
                        //println("---model size---" + groupModel.size)
                        println("---group size---" + it.groups!!.size)
                    } else {
                        groupAddView(it.groups!!)
                    }
                }
            }
        })
    }

    private fun addAllUser(firebaseUser: ArrayList<FireUser>) {
        for (fireUserItem in firebaseUser){
            if (fireUserItem.status == "Online"){
                if (userContains(usersList,fireUserItem._id)){
                    Log.d("mTag","User Already add")
                }else{
                    if (MyCommunityApp.getUser(requireActivity())!!._id == fireUserItem._id){
                        Log.d("mTag","this own user")
                    }else{
                        usersList.add(fireUserItem)
                    }

                }
            }
        }
        allUserAdapter.notifyDataSetChanged()
        allUserCount.text = "${usersList.size} Peoples Online.."

    }
    fun userContains(userList: ArrayList<FireUser>, name: String?): Boolean {
        for (item in userList) {
            if (item._id.equals(name)) {
                return true
            }
        }
        return false
    }

    private fun setUserRec() {
        allUserAdapter = AllUserAdapter(requireActivity(),usersList)
        val layoutManage = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        Log.d("mTag","allUserPostModel ${usersList.size}")

        userRcV.layoutManager = layoutManage
        userRcV.adapter = allUserAdapter


    }

    private fun setFollowerRec() {
        followersRcV.layoutManager = GridLayoutManager(requireActivity(), 5)
        profileFollowerAdapter = ProfileFollowerAdapter(requireActivity(), profileFollowerModel)
        followersRcV.adapter = profileFollowerAdapter

        viewAllFollowersTV.setOnClickListener {
            homeInterface.onViewAllFollowersClicked(
                profileDetails!!._id,
                MyCommunityAppConstants.STATUS_STORY_GROUP_ID,
                profileDetails!!.full_name
            )
        }

    }

    private fun getBannerImg(posts: ArrayList<HomePostModel>) {
        for (isPost in posts){
            sliderItemList.add(isPost)

        }
        sliderAdpter.addSliderList(sliderItemList)

    }

    override fun onPause() {
        super.onPause()
        requireActivity().viewModelStore.clear()
    }

    private fun groupAddView(groups: ArrayList<GroupModel>) {
        for(groups_item in groups){
            if (groups_item._id == MyCommunityAppConstants.STATUS_STORY_GROUP_ID
                || groups_item._id == MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP
                ||groups_item.is_joined == true){
                allGroupIdList.add(groups_item._id!!)
            }
        }
        getGroupIdPost(allGroupIdList)
    }
    private fun initViews() {
        //writeStoryPostCV.setOnClickListener { writeStoryPost(it) }
        showNotiIV.visibility = View.GONE
        notctHomeIV.visibility = View.GONE
        notctHomeIV.setOnClickListener {
            if (notify){
                notctHomeIV.load(R.drawable.ic_notifications_off)
                showNotiIV.visibility = View.GONE
                notify = false
            }else{
                notctHomeIV.load(R.drawable.notification)
                notify=  true
            }

        }

    }

    private fun sharePost(userProfileImg:String?,profileName:String?,postDescp:String?,msgDeption:String) {
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.share_postcard_item,null)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = layoutParams
        val profileImg = view.findViewById<CircleImageView>(R.id.profileImg)
        val userName = view.findViewById<TextView>(R.id.userName)
        //  val postText = view.findViewById<TextView>(R.id.postText)
        Log.d("mTag","profile img = $userProfileImg")
        val bitmap = getBitmapFromURL(userProfileImg)
        profileImg.setImageBitmap(bitmap)
        userName.text = profileName
        // postText.text = postDescp
        capture(view,msgDeption)

    }
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }

    private fun createDynamicLink(id: String?,userProfileImg:String?,profileName:String?, description: String?) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://jantaaapp.page.link/" + id))
            .setDomainUriPrefix("https://jantaaapp.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink()
            .addOnSuccessListener { command ->
                val shortLink = command.shortLink
                val flowchartLink = command.previewLink
                val descrip = "See this Post..."+"\n===================\n"+
                        description +"\n====================="+ "\n\nJoin Community House Now...\n" + shortLink.toString()+"\n\n\n\n"

                sharePost(userProfileImg,profileName,description,descrip)

                Log.i(
                    TAG,
                    "createDynamicLink: $shortLink ---> $flowchartLink"
                )
            }.addOnFailureListener { command ->
                Log.i(TAG, "createDynamicLink: " + command.message)
            }
    }

    fun capture(view:View,descp:String?) {
        val bitmap = ImageUtils.generateShareImage(view)
        ShareUtils.shareImageToOthers(requireActivity(),descp, bitmap)
    }

    private fun getGroupIdPost(allGroupIdList:ArrayList<String>){
        for (groupId in allGroupIdList){
            communityViewModel.getHomePosts(groupId,page.toString(), strPageLimit.toString())
        }

    }
    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
        //  private const val STORY_REQUEST_CODE = 1
        private const val PICK_REQUEST = 2
        private const val PICK_VIDEO_REQUEST = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    private fun writeStoryPost(view: View){
        if (ContextCompat.checkSelfPermission(view.context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(view.context,Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), MyCommunityAppConstants.STORY_REQUEST_CODE
            )
        } else {
            ShowDialogFile()
            //pickImage()
        }
    }

    private fun ShowDialogFile() {
        val options = arrayOf<CharSequence>("Photo Gallery", "Video", "Cancel")
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select From: ")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Photo Gallery") {
                pickImage()
            } else if (options[item] == "Video") {
                pickVideo()
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun pickVideo() {
        val pickVideoIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickVideoIntent.type = "video/*"
        startActivityForResult(pickVideoIntent, PICK_VIDEO_REQUEST)
    }

    private fun pickImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == PICK_REQUEST) {
                //storyPostImageUrl.add(data!!.data.toString())
                val postUrl = data!!.data.toString()
                homeInterface.loadWriteStoryPostFragment(MyCommunityAppConstants.STATUS_STORY_GROUP_ID,
                    storyPostImageUrl,postUrl
                )
            }else if (requestCode == PICK_VIDEO_REQUEST){

                if (data != null) {
                    val   videoUri = data.getData();
                    val  videoPath = generatePath(videoUri!!,requireActivity());
                    if (videoPath != null){
                        val mp: MediaPlayer = MediaPlayer.create(requireActivity(), Uri.parse(videoPath))
                        val duration = mp.duration
                        mp.release()
                        if (duration / 1000 > 30) {
                            // Show Your Messages
                            showSnackbar("Please select 0:30 below duration")

                        } else {
                            Log.d("mTag","video Path = $videoPath")
                            homeInterface.onWriteStoryPostClicked(MyCommunityAppConstants.STATUS_STORY_GROUP_ID,
                                storyPostImageUrl,videoPath
                            )
                        }


                    }else{
                        Log.d("mTag","video Path null = $videoPath")
                    }

                }
            }
        }
    }
    fun generatePath(uri: Uri,context:Context):String? {
        var filePath: String? = null
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat) {
            filePath = generateFromKitkat(uri,context)
        }
        if (filePath != null) {
            return filePath
        }
        val cursor = context.contentResolver.query(
            uri,
            arrayOf(MediaStore.MediaColumns.DATA),
            null,
            null,
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return filePath ?: uri.path
    }
    private fun generateFromKitkat(uri:Uri,context:Context):String? {
        var filePath:String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val wholeID = DocumentsContract.getDocumentId(uri)
            val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val column = arrayOf<String>(MediaStore.Video.Media.DATA)
            val sel:String = MediaStore.Video.Media._ID + "=?"
            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf<String>(id), null
            )
            val columnIndex: Int = cursor!!.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return filePath
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }
}