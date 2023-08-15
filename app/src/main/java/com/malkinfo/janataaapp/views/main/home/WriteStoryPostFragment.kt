package com.malkinfo.janataaapp.views.main.home

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.helpers.FormValidator
import com.malkinfo.janataaapp.managers.utils.CommunityStore
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


/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */

class WriteStoryPostFragment : MyBaseFragment()
//    View.OnClickListener, EmojiBSFragment.EmojiListener
           {

    /**set id*/
    var imagePhotoEditBack: ImageView? = null
   // var imagePhotoEditUndo: ImageView? = null
   // var imagePhotoEditRedo: ImageView? = null
   // var imagePhotoEditCrop: ImageView? = null
   // var imagePhotoEditSticker: ImageView? = null
   // var imagePhotoEditText: ImageView? = null
  //  var imagePhotoEditPaint: ImageView? = null
    //var colorPickerView: VerticalSlideColorPicker? = null
  //  var photoEditorView: PhotoEditorView? = null
    //var cropImg: CropImage? = null
   private lateinit var fabPhotoDone: FloatingActionButton
   // private var mPhotoEditor: PhotoEditor? = null
    private lateinit var dspcET:EditText
   // private var mEmojiBSFragment: EmojiBSFragment? = null
   // private lateinit var fab_crop_done: FloatingActionButton
    private lateinit var postLL: LinearLayout
    private lateinit var statusImgIV:ImageView
    private lateinit var videFram:FrameLayout
    private lateinit var statusVidV:VideoView
/**================================================================================*/
    private var mSelectedColor = 0
    private lateinit var listener:CommunityStore
   // private var storyImageUrls:String? =null
    private lateinit var errorMsg: String
    /**set post*/
    var isEdit = false
    private var imageUrlModel: ArrayList<String> = ArrayList()
    private lateinit var post: String
    var groupId: String? = null
    var file: File? = null
    var uploadPhoto = false
   // private var PostImgBitmap:Bitmap? = null
               private val mCurrentPosition = 0


    private val communityViewModel: CommunityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CommunityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_status_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader(communityViewModel)
        initId(view)
    }

    private fun initId(v: View) {
        imagePhotoEditBack = v.findViewById(R.id.img_photo_edit_back)
//        imagePhotoEditUndo = v.findViewById(R.id.img_photo_edit_undo)
//        imagePhotoEditRedo = v.findViewById(R.id.img_photo_edit_redo)
//        imagePhotoEditCrop = v.findViewById(R.id.img_photo_edit_crop)
//        imagePhotoEditSticker = v.findViewById(R.id.img_photo_edit_stickers)
//        imagePhotoEditText = v.findViewById(R.id.img_photo_edit_text)
//        imagePhotoEditPaint = v.findViewById(R.id.img_photo_edit_paint)
//        photoEditorView = v.findViewById(R.id.photo_editor_view)
//        fab_crop_done = v.findViewById(R.id.fab_crop_done)
        postLL = v.findViewById(R.id.postLL)
        statusImgIV = v.findViewById(R.id.statusImgIV)
        videFram = v.findViewById(R.id.videFram)
        statusVidV = v.findViewById(R.id.statusVidV)
        //cropImg = v.findViewById(R.id.cropImge)
        fabPhotoDone = v.findViewById(R.id.fab_photo_done)
        dspcET = v.findViewById(R.id.dspcET)
       // mEmojiBSFragment = EmojiBSFragment()
//        mPhotoEditor = PhotoEditor.Builder(requireActivity(), photoEditorView)
//            .setPinchTextScalable(true)
//            .build()
//        colorPickerView = v.findViewById(R.id.color_picker_view)
//        colorPickerView!!.setOnColorChangeListener(
//            object : VerticalSlideColorPicker.OnColorChangeListener {
//                override fun onColorChange(selectedColor: Int) {
//                    mSelectedColor = selectedColor
//                    if (colorPickerView!!.visibility === View.VISIBLE) {
//                        imagePhotoEditPaint!!.setBackgroundColor(selectedColor)
//                        mPhotoEditor!!.setBrushColor(selectedColor)
//                    }
//                }
//            })
        imagePhotoEditBack!!.setOnClickListener { listener.onBack() }
//        imagePhotoEditUndo!!.setOnClickListener(this)
//        imagePhotoEditRedo!!.setOnClickListener(this)
//        imagePhotoEditCrop!!.setOnClickListener(this)
//        imagePhotoEditSticker!!.setOnClickListener(this)
//        imagePhotoEditText!!.setOnClickListener(this)
//        imagePhotoEditPaint!!.setOnClickListener(this)
        //fabPhotoDone!!.setOnClickListener(this)
//        mEmojiBSFragment!!.setEmojiListener(this)
//        fab_crop_done.setOnClickListener(this)
//
//
//
//            //photoEditorView!!.source.setImageBitmap(PostImgBitmap)
//
//           // cropImg!!.getBitmapImage(PostImgBitmap!!)
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        fabPhotoDone.setOnClickListener {
            addStoryPost()
        }


    }
               private fun setVideoPath(uri: Uri?, videoPostViewVV:VideoView) {
                   // Show the "Buffering..." message while the video loads.
                   videoPostViewVV.visibility = View.VISIBLE
                   if (uri != null) {
                       videoPostViewVV.setVideoURI(uri)
                   }
                   val mediaController = MediaController(requireActivity())
                   videoPostViewVV.setMediaController(mediaController)
                   mediaController.setAnchorView(videoPostViewVV)
                   // Listener for onPrepared() event (runs after the media is prepared).
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

                               // Start playing!
                               videoPostViewVV.start()
                           }
                       })

                   // Listener for onCompletion() event (runs after media has finished
                   // playing).
                   videoPostViewVV.setOnCompletionListener(
                       object : MediaPlayer.OnCompletionListener {
                           override fun onCompletion(mediaPlayer: MediaPlayer?) {
//                               Toast.makeText(
//                                   requireActivity(),
//                                   "Video Complete",
//                                   Toast.LENGTH_SHORT
//                               ).show()

                               // Return the video position to the start.
                               videoPostViewVV.seekTo(0)
                           }
                       })
               }
    override fun onErrorCalled(it: String?) {
        showSnackbar(it!!)
    }

    private fun validation(): Boolean {
        var formOk = true

        if (!FormValidator.requiredField(dspcET.text.toString(), 1)) {
            formOk = false
            errorMsg = getString(R.string.write_something)

        }
        return formOk
    }

    override fun initObservers() {
        Log.d("mTag","i am initObservers")
        communityViewModel.addPostLiveData.observe(this, Observer {
            if (it != null) {
                imageUrlModel.clear()
                //  showSnackbar(it.message!!)
                listener.onItemSubmitted()
                viewModelStore.clear()
            }
        })
        communityViewModel.postImageLiveData.observe(this, Observer {
            Log.d("mTag","i am in postImageLiveData")
            if (it != null) {
                if (uploadPhoto) {
                    Log.d("mTag","img upload")
                    for (i in 0 until it.media_file!!.size) {
                        val fileUrl = it.media_file!![i].fileurl

                        if (fileUrl!!.endsWith(".mp4")){
                            videFram.visibility = View.VISIBLE
                            statusImgIV.visibility = View.GONE

                            setVideoPath(Uri.parse(fileUrl),statusVidV)

                        }else{
                            videFram.visibility = View.GONE
                            statusImgIV.visibility = View.VISIBLE
                          //  val imageUri = Uri.parse(fileUrl)
                         //   PostImgBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),imageUri)
                            statusImgIV.load(fileUrl)
                        }

                        imageUrlModel.add(fileUrl)
                        fabPhotoDone.visibility = View.VISIBLE
                        dspcET.visibility = View.VISIBLE
                    }
                    //addStoryPost()
                }
            }
            Log.d("mTag","it = $it")
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (arguments != null) {
                groupId = it.getString(MyCommunityAppConstants.GROUP_ID)
             //   groupName = it.getString(MyCommunityAppConstants.GROUP_NAME)
               isEdit= it.getBoolean(MyCommunityAppConstants.IS_EDIT)
             val storyImageUrls = it.getString(MyCommunityAppConstants.STORYIMGURL)
                uploadFile(File(storyImageUrls))
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunityStore) {
            listener = context as CommunityStore
        }
    }

//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.img_photo_edit_back -> {listener.onBack()}
//            R.id.img_photo_edit_undo -> mPhotoEditor!!.undo()
//            R.id.img_photo_edit_redo -> mPhotoEditor!!.redo()
//            R.id.img_photo_edit_crop -> {
//                photoEditorView!!.visibility = View.GONE
//                cropImg!!.visibility = View.VISIBLE
//                fabPhotoDone!!.visibility = View.GONE
//                fab_crop_done.visibility = View.VISIBLE
//                postLL.visibility = View.GONE
//            }
//            R.id.img_photo_edit_stickers->{
//                mEmojiBSFragment!!.show(childFragmentManager, mEmojiBSFragment!!.getTag())
//            }
//
//            R.id.img_photo_edit_text -> {
//                ShowBrush(false)
//                /**
//                 * To Open Edit Text Fragment
//                 */
//                val textEditorDialogFragment = TextEditorDialogFragment.show(childFragmentManager,requireActivity())
//                textEditorDialogFragment.setOnTextEditorListener(object : TextEditorDialogFragment.TextEditor {
//                    override fun onDone(inputText: String?, colorCode: Int) {
//                        mPhotoEditor!!.addText(inputText, colorCode)
//
//                    }
//                })
//            }
//            R.id.img_photo_edit_paint -> if (colorPickerView!!.visibility === View.VISIBLE) {
//                ShowBrush(false)
//            } else {
//                ShowBrush(true)
//            }
//            R.id.fab_photo_done ->{
//                //val drawable = photoEditorView!!.source.drawable as BitmapDrawable
//                //postImgUri(drawable.bitmap)
//                saveImage()
//
//            }
//            R.id.fab_crop_done->{
//                photoEditorView!!.source.setImageBitmap(cropImg!!.test())
//                cropImg!!.visibility = View.GONE
//                photoEditorView!!.visibility = View.VISIBLE
//                fabPhotoDone!!.visibility = View.VISIBLE
//                fab_crop_done.visibility = View.GONE
//                postLL.visibility = View.VISIBLE
//            }
//        }
//    }

    /**
     * allow and not allow paint edit
     *
     * @param enableBrush -true ( show color picker and allowed to paint edit in image )
     * -false ( hide color picker and not allowed to paint edit in image
     */
//    private fun ShowBrush(enableBrush: Boolean) {
//        if (enableBrush) {
//            mPhotoEditor!!.brushColor = mSelectedColor
//            imagePhotoEditPaint!!.setBackgroundColor(mSelectedColor)
//            mPhotoEditor!!.setBrushDrawingMode(true)
//            colorPickerView!!.visibility = View.VISIBLE
//        } else {
//            imagePhotoEditPaint!!.setBackgroundColor(getResources().getColor(android.R.color.transparent))
//            mPhotoEditor!!.setBrushDrawingMode(false)
//            colorPickerView!!.visibility = View.INVISIBLE
//        }
//    }

    /**
     * Method to save the edited image
     */
//    private fun saveImage() {
//        Log.d("mTag","I am in saveImage methodh")
//
//        //showLoading("Saving...")
//        val fileName = "${System.currentTimeMillis()}.png"
//        val path = requireActivity().getDir(MyCommunityAppConstants.DIR_ALL_IMAGE,Context.MODE_PRIVATE)
//                try {
//                    file = File(path,fileName)
//                    if (ActivityCompat.checkSelfPermission(
//                            requireActivity(),
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return
//                    }
//                    mPhotoEditor!!.saveAsFile(file!!.absolutePath, object : OnSaveListener {
//                        override fun onSuccess(imagePath: String) {
//                            Log.e("mTag", "Image Saved Successfully")
//                            val imageUrl = Uri.fromFile(File(imagePath))
//                            postImgUri(imageUrl)
//
//                        }
//
//                        override fun onFailure(exception: java.lang.Exception) {
//                            Log.e("mTag", "Failed to save Image")
//                        }
//                    })
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.d("mTag","error = ${e.message}")
//                }
//        }



//    fun postImgUri(imgUrl:Uri){
//        Log.d("mTag","I am postImgUri")
//       val postImgBitmaps = MediaStore.Images.Media.getBitmap(
//            requireActivity().applicationContext.contentResolver,
//            imgUrl
//        )
//
//        file = File(requireActivity().cacheDir.toString() + postImgBitmaps.toString())
//
//        try {
//            file!!.createNewFile()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val bitmap: Bitmap = postImgBitmaps
//
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
//        val bitmapdata = bos.toByteArray()
//
//        if (postImgBitmaps != null) {
//            uploadFile(file!!)
//        }
//
//        var fos: FileOutputStream? = null
//        try {
//            fos = FileOutputStream(file)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        try {
//            fos!!.write(bitmapdata)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        try {
//            fos!!.flush()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        try {
//            fos!!.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
    private fun uploadFile(isfile: File) {
        //uploadImage
        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), isfile)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", isfile.getName(), requestFile)
        Log.d("mTag","body = $body")
        Log.d("mTag","file = $isfile")
        communityViewModel.postImage(body)
        uploadPhoto=true
        Log.d("mTag","upload = $uploadPhoto")

    }

    private fun addStoryPost(){
        if (validation()) {

            //Do updatePost
            if (isEdit) {
               Log.d("mTag","it not exit")
            } else {
                val main = JSONObject()
                val array = JSONArray()
                for (i in imageUrlModel) {
                    array.put(i)
                }
                try {
                    post = dspcET.text.toString()
                    main.put("description", post)
                    main.put("image_url", array)
                    main.put("group_id", groupId)
                    val jsonParser = JsonParser()
                    val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                    communityViewModel.doAddPost(gsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            showSnackbar(errorMsg)
        }
    }

//    override fun onEmojiClick(emojiUnicode: String?) {
//        //mPhotoEditor!!.addEmoji(emojiUnicode)
//        ///mTxtCurrentTool!!.setText(ja.burhanrashid52.photoeditor.R.string.label_emoji)
//    }

    companion object {

        @JvmStatic
        fun newInstance(groupId: String,
                        description: String?,
                        postImageUrl: ArrayList<String>?,
                        postId: String?,
                        isEdit: Boolean,
                        storyImageUrl:String
        ) =
            WriteStoryPostFragment().apply {
                arguments = Bundle().apply {

                    putString(MyCommunityAppConstants.GROUP_ID, groupId)
                    putString(MyCommunityAppConstants.POST_DESCRIPTION, description)
                   if (postImageUrl != null){
                       putString(
                           MyCommunityAppConstants.POST_IMAGE_URL,
                           JsonUtils.toJson(postImageUrl)
                       )
                   }
                    putString(MyCommunityAppConstants.POST_ID, postId)
                    putBoolean(MyCommunityAppConstants.IS_EDIT, isEdit)
                    putString(MyCommunityAppConstants.STORYIMGURL,storyImageUrl)
                }
            }
        const val READ_WRITE_STORAGE = 52
    }
}