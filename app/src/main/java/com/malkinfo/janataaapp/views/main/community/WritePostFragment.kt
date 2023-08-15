package com.malkinfo.janataaapp.views.main.community

import MyCommunityApp
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.MediaFromGalleryAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.base.FileUploadItem
import com.malkinfo.janataaapp.models.community.GroupModel
import com.malkinfo.janataaapp.utils.JsonUtils
import com.malkinfo.janataaapp.viewmodels.CommunityViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class WritePostFragment (var group_Ids:ArrayList<String>?): MyBaseFragment() {
    private var uriArray: ArrayList<Uri> = ArrayList()
    private var postId: String? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    protected val REQUEST_CODE_GALLERY = 2
    private val STORAGE_CAMERA_PERMISSION_CODE = 34
    private var postDescription: String? = null
    private lateinit var post: String
    private lateinit var errorMsg: String
    var groupId: String? = null
    var imageUrl: String? = null
    private lateinit var listener: CommunityStore
    private var mediaModel: ArrayList<FileUploadItem> = ArrayList()
    private var imageUrlModel: ArrayList<String> = ArrayList()
    private var updateImageUrlModel: ArrayList<String> = ArrayList()
    private lateinit var galleryAdapter: MediaFromGalleryAdapter
    var isEdit = false
    var uploadPhoto = false
    var imageBitmap: Bitmap? = null
    var file: File? = null

    /**set ID*/
    private lateinit var galleryRV:RecyclerView

    private lateinit var writePostED:EditText
    private lateinit var addPostBT: MaterialButton
    private lateinit var userNameTV:TextView
    private lateinit var hiTV:TextView
    private lateinit var writePostHintTV:TextView
    private lateinit var write_postBackIV:ImageView
    private lateinit var seeAllMediaTV:ImageView
    private lateinit var videoMediaIV:ImageView
    private lateinit var pdfMediaIV:ImageView

    /**========================================================*/
    /**video upload*/
    val REQUEST_PICK_VIDEO: Int = 3
    val REQUEST_PICK_Pdf: Int = 4
    private var videoUri: Uri? = null
    private var videoPath: String? = null

    /**==================================*/
    /**get All Group*/
    private var groupModel: ArrayList<GroupModel> = ArrayList()


    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            isEdit = requireArguments().getBoolean(MyCommunityAppConstants.IS_EDIT)
            groupId = requireArguments().getString(MyCommunityAppConstants.GROUP_ID)
            postDescription = requireArguments().getString(MyCommunityAppConstants.POST_DESCRIPTION)
            postId = requireArguments().getString(MyCommunityAppConstants.POST_ID)
            if (isEdit) {
                updateImageUrlModel = JsonUtils.parseJson(requireArguments().getString(MyCommunityAppConstants.POST_IMAGE_URL)!!)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return inflater.inflate(R.layout.fragment_write_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsIds(view)
        setUpLoader(communityViewModel)
        initViews(view)
    }

    private fun initsIds(v:View){
        galleryRV = v.findViewById(R.id.galleryRV)
        //chooseMediaTagCL = v.findViewById(R.id.chooseMediaTagCL)
        writePostED = v.findViewById(R.id.writePostED)
        addPostBT = v.findViewById(R.id.addPostBT)
        userNameTV = v.findViewById(R.id.userNameTV)
        hiTV = v.findViewById(R.id.hiTV)
        writePostHintTV = v.findViewById(R.id.writePostHintTV)
        write_postBackIV  = v.findViewById(R.id.write_postBackIV)
        seeAllMediaTV = v.findViewById(R.id.seeAllMediaTV)
        videoMediaIV = v.findViewById(R.id.videoMediaIV)
        pdfMediaIV = v.findViewById(R.id.pdfMediaIV)

    }
    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    override fun initObservers() {
        communityViewModel.addPostLiveData.observe(this, Observer {
            if (it != null) {
                imageUrlModel.clear()
                //  showSnackbar(it.message!!)
                listener.onItemSubmitted()

                viewModelStore.clear()
            }
        })

        communityViewModel.updatePostLiveData.observe(this, Observer {
            if (it != null) {
                imageUrlModel.clear()
                updateImageUrlModel.clear()
                //   showSnackbar(it.message!!)
                listener.onItemSubmitted()

                requireActivity().viewModelStore.clear()
            }
        })

        communityViewModel.postImageLiveData.observe(this, Observer {
            if (it != null) {
                if (uploadPhoto) {
                    mediaModel.addAll(it.media_file!!)
                    galleryRV.visibility = VISIBLE
                    galleryAdapter.notifyDataSetChanged()
                    //chooseMediaTagCL.visibility = GONE

                    for (i in 0 until it.media_file!!.size) {
                        imageUrl = it.media_file!![i].fileurl
                        imageUrlModel.add(imageUrl!!)
                    }
                    uriArray.clear()
                }
            }
        })
        communityViewModel.groupLiveData.observe(this, Observer {
            if (isAdded) {
                if (it.total_count != null) {
                    //  totalLimit = it.total_count
                }
                if (it.groups!!.any()) {
                    groupModel.clear()
                    groupAddView(it.groups!!)
                    println("---model size---" + groupModel.size)
                    println("---group size---" + it.groups!!.size)
                    Log.d("mTag","group Model Size = ${groupModel.size}")
                }
            }
        })
    }
    private fun groupAddView(groups: ArrayList<GroupModel>) {
        for(groups_item in groups){
            when(groups_item._id){
                MyCommunityAppConstants.STATUS_STORY_GROUP_ID->{}
                MyCommunityAppConstants.AD_SLIDER_BANNER_GROUP->{}
                else->{
                    if (groups_item.is_joined == true){
                        groupModel.add(groups_item)

                    }


                }
            }

        }

    }
    private fun getVideoFile(){
        val pickVideoIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickVideoIntent.type = "video/*"
        startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO)
    }
    private fun loadPdfPages(filePath: String) {
        try {

            val fileDescriptor =
                ParcelFileDescriptor.open(File(filePath), ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)
            val pageCount = pdfRenderer.pageCount
            val fileParts = arrayOfNulls<MultipartBody.Part>(pageCount)
            for (i in 0 until pageCount) {
                val page = pdfRenderer.openPage(i)

                // Convert PDF page to bitmap
                val imageBitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(imageBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()

                // val pdfPage = PdfPageModel(i,bitmap)
                //pdfPages.add(pdfPage)
                file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())

                try {
                    file!!.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val bos = ByteArrayOutputStream()
                imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
                val bitmapdata = bos.toByteArray()

                val requestBody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                fileParts[i] = MultipartBody.Part.createFormData("file", file!!.name, requestBody)

                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(file)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(bitmapdata)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            pdfRenderer.close()
            if (imageBitmap != null) {
                communityViewModel.postMultipleImage(fileParts)
                uploadPhoto=true
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun openPdfFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent,REQUEST_PICK_Pdf)
    }


    private fun initViews(view: View) {
        communityViewModel.getGroupList("?", "?")

        if (isEdit) {
            imageUrlModel.addAll(updateImageUrlModel)
            for (i in imageUrlModel) {
                mediaModel.add(FileUploadItem(i, ""))
            }

            writePostED.setText(postDescription)
            addPostBT.text = getString(R.string.update)
            userNameTV.text = getString(R.string.editpost)
            galleryRV.visibility = VISIBLE
            hiTV.visibility = GONE
            //chooseMediaTagCL.visibility = GONE
            writePostHintTV.visibility = GONE

        } else {

            addPostBT.text = getString(R.string.post)
            galleryRV.visibility = GONE
            hiTV.visibility = VISIBLE
            writePostHintTV.visibility = VISIBLE
            // chooseMediaTagCL.visibility = VISIBLE
        }


        galleryRV.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        galleryAdapter = MediaFromGalleryAdapter(view.context, mediaModel)
        galleryRV.adapter = galleryAdapter
        galleryAdapter.notifyDataSetChanged()

        galleryAdapter.onAddMoreMediaClicked = {
            requestPermission(view,"Photo")
        }

        galleryAdapter.onDeleteMediaClicked = { pos ->
            imageUrlModel.removeAt(pos)
            mediaModel.removeAt(pos)
            galleryAdapter.notifyItemRemoved(pos)
            galleryAdapter.notifyDataSetChanged()
        }

        write_postBackIV.setOnClickListener {
            requireActivity().onBackPressed()
        }

        userNameTV.text = MyCommunityApp.getUser(view.context)!!.full_name

        seeAllMediaTV.setOnClickListener {
            requestPermission(view,"Photo")
        }

        addPostBT.setOnClickListener {
            if (validation()) {

                //Do updatePost
                if (isEdit) {
                    updatePost()

                } else {

                    seletGroupDialog()
//                    val main = JSONObject()
//                    val array = JSONArray()
//                    for (i in imageUrlModel) {
//                        array.put(i)
//                    }
//
//                    try {
//                        post = writePostED.text.toString()
//                        main.put("description", post)
//                        main.put("image_url", array)
//                        main.put("group_id", groupId)
//                        val jsonParser = JsonParser()
//                        val gsonObject = jsonParser.parse(main.toString()) as JsonObject
//                        communityViewModel.doAddPost(gsonObject)
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
                }
            } else {
                showSnackbar(errorMsg)
            }
        }
        videoMediaIV.setOnClickListener { requestPermission(view,"Video") }
        pdfMediaIV.setOnClickListener { requestPermission(view,"Pdf") }
    }

    private fun updatePost() {
        val isListItem :ArrayList<String> = ArrayList()
        for (item in groupModel){
            for (groups in group_Ids!!){
                if (item._id == groups){
                    isListItem.add(item.group!!)
                }
            }

        }
        //   val groupList :Array<String> = isListItem.toTypedArray()
        val checkedItems = BooleanArray(group_Ids!!.size)
        // initialise the alert dialog builder
        val groupDialog = AlertDialog.Builder(requireActivity())

        for (i in group_Ids!!.indices){
            checkedItems[i] = true
        }

        val selectedItems :ArrayList<String> = group_Ids!!
        val unSelectedItems :ArrayList<String> = group_Ids!!
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
            val seletGroupList :ArrayList<String> = ArrayList()
            val unSeleteGroupList :ArrayList<String> = ArrayList()
            for (i in checkedItems.indices) {
                if (checkedItems[i]) {
                    seletGroupList.add(selectedItems[i])
                    //selectedItems[i]
                    // Log.d("mTag","Selete Item = ${selectedItems[i]}")
                    //  showSnackbar("group size ${seletGroupList.size}...")
                }else{
                    unSeleteGroupList.add(unSelectedItems[i])
                    //Log.d("mTag","Selete Item = ${unSelectedItems[i]}")
                }
            }
            if (seletGroupList.size == group_Ids!!.size){
                //showSnackbar("I update post in all group")
                updateGroupPost()
            }else{
                // showSnackbar("group size ${seletGroupList.size}...")
                newUpdatePostIn(seletGroupList)
                updateNewGroup(unSeleteGroupList)
                // showSnackbar("I creating new post...")
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
    private fun updateNewGroup(groups:ArrayList<String>){
        val main = JSONObject()
        val groupArray = JSONArray()

        for (i in 0 until groups.size){
            groupArray.put(groups[i])
        }

        try {
            main.put("group_id",groupArray)
            val jsonParser = JsonParser()
            val gsonObject = jsonParser.parse(main.toString()) as JsonObject
            communityViewModel.doUpdatePost(postId!!,gsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun updateGroupPost() {
        val main = JSONObject()
        val array = JSONArray()
        for (i in imageUrlModel) {
            array.put(i)
        }

        try {
            post = writePostED.text.toString()
            main.put("description", post)
            main.put("image_url", array)
            val jsonParser = JsonParser()
            val gsonObject = jsonParser.parse(main.toString()) as JsonObject
            communityViewModel.doUpdatePost(postId!!,gsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun seletGroupDialog() {
        val isListItem :ArrayList<String> = ArrayList()
        for (item in groupModel){
            isListItem.add(item.group!!)
        }
        //   val groupList :Array<String> = isListItem.toTypedArray()
        val checkedItems = BooleanArray(groupModel.size)
        // initialise the alert dialog builder
        val groupDialog = AlertDialog.Builder(requireActivity())

        for (i in groupModel.indices){
            if ( groupId == groupModel[i]._id){
                checkedItems [i] = true
            }
        }

        val selectedItems :ArrayList<GroupModel> = groupModel
        // set the title for the alert dialog
        groupDialog.setTitle("Choose Group")
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
            val seletGroupList :ArrayList<GroupModel> = ArrayList()
            for (i in checkedItems.indices) {
                if (checkedItems[i]) {
                    seletGroupList.add(selectedItems[i])
                    selectedItems[i]
//                    Log.d("mTag","Selete Item = ${selectedItems[i].group}")
//                    showSnackbar("group size ${seletGroupList.size}...")
                }
            }
            if (seletGroupList.size == 0){
                showSnackbar("Please Select least one group...")
            }else{
                // showSnackbar("group size ${seletGroupList.size}...")
                postIn(seletGroupList)
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

    private fun postIn(selectGroup:ArrayList<GroupModel>) {

        val main = JSONObject()
        val array = JSONArray()
        val groupArray = JSONArray()
        for (i in imageUrlModel) {
            array.put(i)
        }
        for (i in 0 until selectGroup.size){
            groupArray.put(selectGroup[i]._id)
        }

        try {
            post = writePostED.text.toString()
            main.put("description", post)
            main.put("image_url", array)
            main.put("group_id", groupArray)
            val jsonParser = JsonParser()
            val gsonObject = jsonParser.parse(main.toString()) as JsonObject
            communityViewModel.doAddPost(gsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
    private fun newUpdatePostIn(selectGroup:ArrayList<String>) {

        val main = JSONObject()
        val array = JSONArray()
        val groupArray = JSONArray()
        for (i in imageUrlModel) {
            array.put(i)
        }
        for (i in 0 until selectGroup.size){
            groupArray.put(selectGroup[i])
        }

        try {
            post = writePostED.text.toString()
            main.put("description", post)
            main.put("image_url", array)
            main.put("group_id", groupArray)
            val jsonParser = JsonParser()
            val gsonObject = jsonParser.parse(main.toString()) as JsonObject
            communityViewModel.doAddPost(gsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun requestPermission(view: View,fileType:String) {
        if (ContextCompat.checkSelfPermission(
                view.context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                view.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                view.context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            when(fileType){
                "Photo" ->{
                    showSelector(view)
                }
                "Video" ->{
                    getVideoFile()
                }
                "Pdf"->{
                    openPdfFilePicker()
                }
            }

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                STORAGE_CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun showSelector(view: View) {
        val options = arrayOf<CharSequence>("Camera", "Photo Library", "Cancel")

        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Select Document Image Source: ")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Camera") {

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(view.context.packageManager) != null) {
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                }

            } else if (options[item] == "Photo Library") {

                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    REQUEST_CODE_GALLERY
                )

                /*val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_CODE_GALLERY)*/

            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when(requestCode){
                REQUEST_CODE_GALLERY->{
                    gallery(data)
                }
                REQUEST_IMAGE_CAPTURE->{
                    camera(data)
                }
                REQUEST_PICK_VIDEO->{
                    if (data != null) {
                        videoUri = data.data;
                        videoPath = generatePath(videoUri!!,requireActivity());

                        if (videoPath != null){
                            val mp: MediaPlayer = MediaPlayer.create(requireActivity(), Uri.parse(videoPath))
                            val duration = mp.duration
                            mp.release()
                            if (duration / 1000 > 100) {
                                // Show Your Messages
                                showSnackbar("Please select 1:00 below duration")

                            } else {
                                Log.d("mTag","video Path = $videoPath")
                                val videoFile = File(videoPath)
                                uploadFile(videoFile)
                            }


                        }else{
                            Log.d("mTag","video Path null = $videoPath")
                        }

                    }
                }
                REQUEST_PICK_Pdf->{
                    if (data != null){
                        val pdfUri = data.data
                        //  val pdfFile = File(pdfUri.toString())
                        val pdfPath =getPDFPath(pdfUri)
                        if (pdfPath != null){
                            Log.d("mTag","pdf Path = $pdfPath")
                            loadPdfPages(pdfPath)
                        }else{
                            Log.d("mTag","pdf Path = $pdfPath")
                        }

                    }

                }
            }

        }
    }
    fun getPDFPath(uri: Uri?): String {
        var absolutePath = ""
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri!!)
            val pdfInBytes = ByteArray(inputStream!!.available())
            inputStream.read(pdfInBytes)

            val encodePdf: String =  android.util.Base64.encodeToString(pdfInBytes,android.util.Base64.DEFAULT)
            Toast.makeText(context, "" + encodePdf, Toast.LENGTH_SHORT).show()
            var offset = 0
            var numRead = 0
            while (offset < pdfInBytes.size && inputStream.read(
                    pdfInBytes,
                    offset,
                    pdfInBytes.size - offset
                ).also {
                    numRead = it
                } >= 0
            ) {
                offset += numRead
            }
            var mPath = ""
            mPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM)
                    .toString() + "/" + Calendar.getInstance().getTime() + ".pdf"
            } else {
                (Environment.getExternalStorageDirectory().toString() + "/" + Calendar.getInstance()
                    .getTime()).toString() + ".pdf"
            }
            val pdfFile = File(mPath)
            val op: OutputStream = FileOutputStream(pdfFile)
            op.write(pdfInBytes)
            absolutePath = pdfFile.path
        } catch (ae: Exception) {
            ae.printStackTrace()
        }
        return absolutePath
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

    override fun onStop() {
        super.onStop()
    }


    private fun camera(data: Intent) {
        imageBitmap = data.extras!!["data"] as Bitmap?
        file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())

        try {
            file!!.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val bitmap: Bitmap = this.imageBitmap!!

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

        if (imageBitmap != null) {
            uploadFile(file!!)
        }

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapdata)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            fos!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            fos!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun gallery(data: Intent) {
        if (data.clipData != null) {
            val cout = data.clipData!!.itemCount
            val imageCount = mediaModel.size + cout
            if (imageCount <= 9) {
                for (i in 0 until cout) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    uriArray.add(imageUri)
                }
                val fileParts = arrayOfNulls<MultipartBody.Part>(uriArray.size)
                for (i in 0 until uriArray.size) {
                    imageBitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().applicationContext.contentResolver,
                        uriArray[i]
                    )

                    file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())

                    try {
                        file!!.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val bos = ByteArrayOutputStream()
                    imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
                    val bitmapdata = bos.toByteArray()

                    val requestBody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                    fileParts[i] = MultipartBody.Part.createFormData("file", file!!.name, requestBody)

                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(file)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    try {
                        fos!!.write(bitmapdata)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        fos!!.flush()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        fos!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                if (imageBitmap != null) {
                    communityViewModel.postMultipleImage(fileParts)
                    uploadPhoto=true
                }
            } else {
                showSnackbar("You can upload maximum 10 images")
            }

        } else {
            val imageUri = data.data
            imageBitmap = MediaStore.Images.Media.getBitmap(
                requireActivity().applicationContext.contentResolver,
                imageUri
            )
            file = File(requireActivity().cacheDir.toString() + imageBitmap.toString())

            try {
                file!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val bitmap: Bitmap = this.imageBitmap!!

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            if (imageBitmap != null) {
                uploadFile(file!!)
            }

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                fos!!.write(bitmapdata)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fos!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile(isFile:File) {
        //uploadImage
        Log.d("mTag","I am UploadFile")
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), isFile)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", isFile.getName(),requestFile)
        communityViewModel.postImage(body)
        uploadPhoto=true
    }

    private fun validation(): Boolean {
        var formOk = true

        if (!FormValidator.requiredField(writePostED.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.write_something)

        }
        return formOk
    }

    companion object {

        @JvmStatic
        fun newInstance(groupId: String?,
                        groupIds:ArrayList<String>?, description: String?, postImageUrl: ArrayList<String>?, postId: String?, isEdit: Boolean) =
            WritePostFragment(groupIds).apply {
                arguments = Bundle().apply {

                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                    putString(MyCommunityAppConstants.POST_DESCRIPTION, description)
                    putString(
                        MyCommunityAppConstants.POST_IMAGE_URL,
                        JsonUtils.toJson(postImageUrl!!)
                    )
                    putString(MyCommunityAppConstants.POST_ID, postId)
                    putBoolean(MyCommunityAppConstants.IS_EDIT, isEdit)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore)
            listener = context as CommunityStore
    }



}