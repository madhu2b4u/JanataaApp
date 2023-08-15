package com.malkinfo.janataaapp.views.main.matrimony

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.adapters.ChatImageAdapter
import com.malkinfo.janataaapp.adapters.MessageAdapter
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.managers.utils.CommunityStore
import com.malkinfo.janataaapp.models.Matrimony.MessageListItem
import com.malkinfo.janataaapp.models.base.FileUploadItem
import com.malkinfo.janataaapp.viewmodels.MatrimonyViewModel
import com.malkinfo.janataaapp.views.base.MyBaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class MessageFragment : MyBaseFragment() {
    private var tempList: ArrayList<MessageListItem> = ArrayList()
    var uploadPhoto = false
    var imageBitmap: Bitmap? = null
    var file: File? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    protected val REQUEST_CODE_GALLERY = 2
    private val STORAGE_CAMERA_PERMISSION_CODE = 34
    private lateinit var messageInterface: MessageInterface
    private lateinit var messageAdapter: MessageAdapter
    private var messageList: ArrayList<MessageListItem> = ArrayList()
    var msgReceiverId: String? = null
    var msgReceiverName: String? = null
    var msgReceiverProfile: String? = null
    var imageUrl: String? = null
    var type: String? = null
    var message: String? = null
    private lateinit var chatImageAdapter: ChatImageAdapter
    private var chatImageList: ArrayList<FileUploadItem> = ArrayList()
    private var imageUrlList: ArrayList<String> = ArrayList()
    private var uriArray: ArrayList<Uri> = ArrayList()
    private  lateinit var listener : CommunityStore
    private var totalLimit: Int? = null
    var page = 1
    var pageLimit = 10

    /**set ID*/
    private lateinit var messageListRV:RecyclerView
    private lateinit var chatImagesRV:RecyclerView
    private lateinit var attachIV:ImageView
    private lateinit var writeMessageED:EditText
    private lateinit var moreOptionsIV:ImageView
    private lateinit var messageProfileIV: CircleImageView
    private lateinit var messageUserTV:TextView
    private lateinit var sendIV:ImageView
    private lateinit var mesageBackIV:ImageView
/**================================================================================*/


    private val messageViewModel: MatrimonyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MatrimonyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            msgReceiverId = requireArguments().getString(MyCommunityAppConstants.MSG_RECEIVER_ID)
            msgReceiverName =
                requireArguments().getString(MyCommunityAppConstants.MSG_RECEIVER_NAME)
            msgReceiverProfile =
                requireArguments().getString(MyCommunityAppConstants.MSG_RECEIVER_PROFILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        //requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsID(view)
        setUpLoader(messageViewModel)
        initViews(view)
    }
    private fun initsID(v:View){
        messageListRV = v.findViewById(R.id.messageListRV)
        chatImagesRV = v.findViewById(R.id.chatImagesRV)
        attachIV = v.findViewById(R.id.attachIV)
        writeMessageED = v.findViewById(R.id.writeMessageED)
        moreOptionsIV = v.findViewById(R.id.moreOptionsIV)
        messageProfileIV = v.findViewById(R.id.messageProfileIV)
        messageUserTV = v.findViewById(R.id.messageUserTV)
        sendIV = v.findViewById(R.id.sendIV)
        mesageBackIV = v.findViewById(R.id.mesageBackIV)

    }

    override fun onResume() {
        super.onResume()
        page=1
        messageList.clear()
        messageViewModel.getMessageList(msgReceiverId!!,page.toString(),pageLimit.toString())
    }

    override fun onErrorCalled(it: String?) {
        if (it != null) {
            showSnackbar(it)
        }
    }

    override fun initObservers() {
        messageViewModel.messageListLiveData.observe(this, Observer {
            if(isAdded) {
                if (it != null) {
                    if (it.total_count != null) {
                        totalLimit = it.total_count
                    }
                    if (it.messages!!.any()) {
                        if (page == 1) {
                            messageList.clear()
                            tempList.clear()
                            initChatMessage()
                            addMessageintoList(it.messages!!)
                            messageListRV.post(Runnable
                            {
                                messageListRV.smoothScrollToPosition(messageAdapter.itemCount - 1)
                            })
                        } else {
                            addMessageintoList(it.messages!!)
                        }

                    }
                }
            }
        })

        messageViewModel.uploadChatImageLiveData.observe(this, Observer {
            if (it != null) {
                if (uploadPhoto) {
                    if (it.media_file!!.any()) {



                        chatImageList.addAll(it.media_file!!)
                        chatImagesRV.visibility = VISIBLE
                        chatImageAdapter.notifyDataSetChanged()



                        /*val getType = it.media_file!![0].type
                         type = getType!!.substringBefore("/")*/

                        for (i in 0 until it.media_file!!.size) {
                            imageUrl = it.media_file!![i].fileurl
                            imageUrlList.add(imageUrl!!)
                        }

                        if (imageUrlList.size < 4) {
                            attachIV.visibility = VISIBLE
                        } else {
                            attachIV.visibility = GONE
                        }

                        uriArray.clear()
                    }
                }
            }
        })

        messageViewModel.sendMessageLiveData.observe(this, Observer {
            if (it != null) {
                messageViewModel.getMessageList(msgReceiverId!!,page.toString(),pageLimit.toString())
                imageUrlList.clear()
                chatImageList.clear()
                attachIV.visibility = VISIBLE
                chatImagesRV.visibility = GONE
                writeMessageED.setText("")
                /*messageListRV.post(Runnable
                {
                    messageListRV.smoothScrollToPosition(messageAdapter.itemCount - 1)
                })*/
            }
        })

        messageViewModel.blockMatrimonyProfileLiveData.observe(this, Observer {
            if(it!=null){
                showSnackbar(it.message!!)
                listener.onBack()
                requireActivity().viewModelStore.clear()
            }
        })

        messageViewModel.clearChatImageLiveData.observe(this, Observer {
            if(it!=null){
                listener.onBack()
                requireActivity().viewModelStore.clear()
            }
        })
    }

    private fun addMessageintoList(messages: ArrayList<MessageListItem>) {
        tempList.clear()
        if(page>1){
            tempList.addAll(messageList)
            messageList.clear()
            messageList.addAll(messages)
            messageList.addAll(tempList)

            messageListRV.smoothScrollToPosition(messages.size)
        }else {
            messageList.addAll(messages)
        }
        messageAdapter.notifyDataSetChanged()
    }


    private fun initViews(view: View) {


        moreOptionsIV.setOnClickListener {
            openPopupMenu()
        }

        messageProfileIV.load(msgReceiverProfile)
        messageUserTV.text = msgReceiverName


        chatImagesRV.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        chatImageAdapter = ChatImageAdapter(view.context, chatImageList)
        chatImagesRV.adapter = chatImageAdapter
        chatImageAdapter.notifyDataSetChanged()

        chatImageAdapter.onDeleteMediaClicked = { pos ->
            imageUrlList.removeAt(pos)
            chatImageList.removeAt(pos)
            chatImageAdapter.notifyItemRemoved(pos)
            chatImageAdapter.notifyDataSetChanged()
        }
        attachIV.setOnClickListener {
            requestPermission()
        }

        sendIV.setOnClickListener {

            //do send message
            if (writeMessageED.text.toString().isNotEmpty()) {
                message = writeMessageED.text.toString()
                if (imageUrlList.any()) {
                    type = "message with media"
                }
            } else {
                message = ""
                if (imageUrlList.any()) {
                    type = "media"
                }
            }


            val main = JSONObject()
            val array = JSONArray()

            try {
                if (imageUrlList.isNullOrEmpty()) {
                    main.put("message", message)
                    main.put("message_to", msgReceiverId)

                } else {
                    for (i in imageUrlList) {
                        array.put(i)
                    }
                    main.put("message", message)
                    main.put("message_to", msgReceiverId)
                    main.put("media", array)
                    main.put("content_type", type)
                }


                val jsonParser = JsonParser()
                val gsonObject = jsonParser.parse(main.toString()) as JsonObject
                messageViewModel.doSendMessage(gsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        mesageBackIV.setOnClickListener {
            listener.onBack()
            requireActivity().viewModelStore.clear()
        }

    }

    private fun initChatMessage() {

        messageListRV.layoutManager = LinearLayoutManager(activity)
        messageAdapter = MessageAdapter(requireActivity(), messageList,totalLimit)
        messageListRV.adapter = messageAdapter



        messageListRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy<0) {
                    if (!messageListRV.canScrollVertically(-1)) {
                        val limit: Int = totalLimit!! / pageLimit
                        if (page <= limit) {
                            page += 1
                            messageViewModel.getMessageList(
                                msgReceiverId!!,
                                page.toString(),
                                pageLimit.toString()
                            )
                        }
                    }
                }
            }
        })

    }

    private fun requestPermission() {

        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showSelector()
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

    private fun showSelector() {
        val options = arrayOf<CharSequence>("Camera", "Photo Library", "Cancel")

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Document Image Source: ")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Camera") {

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
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
            if (requestCode == REQUEST_CODE_GALLERY) {
                gallery(data)
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                camera(data)
            }
        }
    }

    private fun gallery(data: Intent) {
        if (data.clipData != null) {
            val cout = data.clipData!!.itemCount
            val imageCount = chatImageList.size + cout
            if (imageCount <= 4) {
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

                    val requestBody: RequestBody =
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                    fileParts[i] =
                        MultipartBody.Part.createFormData("file", file!!.name, requestBody)

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
                    messageViewModel.uploadChatMultipleImage(fileParts)
                    uploadPhoto = true
                }
            } else {
                showSnackbar("You can upload maximum 4 images")
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
                uploadFile()
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

    private fun camera(data: Intent) {
        imageBitmap = data!!.extras!!["data"] as Bitmap?
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
            uploadFile()
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

    private fun uploadFile() {
        //uploadImage
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file!!.getName(), requestFile)
        messageViewModel.uploadChatImage(body)
        uploadPhoto = true
    }

    private fun openPopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), moreOptionsIV)
        popupMenu.menuInflater.inflate(R.menu.message_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_viewProfile ->
                    messageInterface.onViewProfileClicked(msgReceiverId,msgReceiverProfile,msgReceiverName)
                R.id.action_block
                -> openBlockAlertDialog()
                R.id.action_clear_chat
                -> {
                    openClearChatAlertDialog()

                }
                R.id.action_report_profile
                -> messageInterface.onReportProfileClicked(msgReceiverId)
            }
            true
        })
        popupMenu.show()
    }

    private fun openClearChatAlertDialog() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.clear_chat_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                messageViewModel.doClearChat(msgReceiverId!!)
                messageList.clear()
                messageAdapter.notifyDataSetChanged()
            })
    }

    private fun openBlockAlertDialog() {
        showConfirmation(getString(R.string.no),
            getString(R.string.yes),
            "",
            getString(R.string.block_alert),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                messageViewModel.doBlockMatrimonyProfile(msgReceiverId!!)
            })
    }


    interface MessageInterface {

        fun onViewProfileClicked(
            msgReceiverId: String?,
            msgReceiverProfile: String?,
            msgReceiverName: String?
        )
        fun onReportProfileClicked(msgReceiverId: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageInterface) {
            messageInterface = context as MessageInterface
        }
        if(context is CommunityStore){
            listener = context as CommunityStore
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(
            receiverId: String?,
            profileUrl: String?,
            fullName: String?
        ) =
            MessageFragment().apply {
                arguments = Bundle().apply {
                    putString(MyCommunityAppConstants.MSG_RECEIVER_ID, receiverId)
                    putString(MyCommunityAppConstants.MSG_RECEIVER_NAME, fullName)
                    putString(MyCommunityAppConstants.MSG_RECEIVER_PROFILE, profileUrl)
                }
            }
    }



}