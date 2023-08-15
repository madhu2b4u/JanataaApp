package com.malkinfo.janataaapp.views.main.community

//import android.app.Activity
//import android.app.Dialog
//import android.media.MediaPlayer
//import android.net.Uri
//import android.view.View
//import android.widget.*
//import coil.api.load
//import com.github.chrisbanes.photoview.PhotoView
//import com.malkinfo.janataaapp.R
//
//class ViewImageDialog(val mActivity: Activity) {
//    private lateinit var isdialog: Dialog
//   // var imageUrl : String ? = null
//    /**set Id*/
//    private lateinit var zoomImageIV: PhotoView
//    private lateinit var mViewBackIV: ImageView
//    private lateinit var myvideoPlay: VideoView
//    private lateinit var videoFramL: FrameLayout
//    private val mCurrentPosition = 0
//
//    fun stowImageSide() {
//        /**set View*/
//        val infalter = mActivity.layoutInflater
//        val dialogView = infalter.inflate(R.layout.post_image_layout, null)
//
//        /**set Dialog*/
//
//        isdialog =  Dialog(mActivity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
//        isdialog.setContentView(dialogView)
//        isdialog.create()
//
//        isdialog.show()
//    }
//
//    fun isDismiss() {
//        isdialog.dismiss()
//    }
//    private fun initUI(imageUrl:String) {
//        //imageUrl = intent.getStringExtra("POST_IMAGE_URL")
//        if (imageUrl.endsWith(".mp4")){
//            zoomImageIV.visibility = View.GONE
//            videoFramL.visibility = View.VISIBLE
//            setVideoPath(Uri.parse(imageUrl),myvideoPlay)
//
//        }else{
//            zoomImageIV.visibility = View.VISIBLE
//            videoFramL.visibility = View.GONE
//            zoomImageIV.load(imageUrl)
//        }
//
//
//        mViewBackIV.setOnClickListener {
//           isDismiss()
//        }
//    }
//    private fun setVideoPath(uri: Uri?, videoPostViewVV:VideoView) {
//        // Show the "Buffering..." message while the video loads.
//        videoPostViewVV.visibility = View.VISIBLE
//        if (uri != null) {
//            videoPostViewVV.setVideoURI(uri)
//        }
//        val mediaController = MediaController(mActivity)
//        videoPostViewVV.setMediaController(mediaController)
//        mediaController.setAnchorView(videoPostViewVV)
//        // Listener for onPrepared() event (runs after the media is prepared).
//        videoPostViewVV.setOnPreparedListener(
//            object : MediaPlayer.OnPreparedListener {
//                override fun onPrepared(mediaPlayer: MediaPlayer?) {
//
//                    // Hide buffering message.
//                    videoPostViewVV.visibility = View.VISIBLE
//
//                    // Restore saved position, if available.
//                    if (mCurrentPosition > 0) {
//                        videoPostViewVV.seekTo(mCurrentPosition)
//                    } else {
//                        // Skipping to 1 shows the first frame of the video.
//                        videoPostViewVV.seekTo(1)
//                    }
//
//                    // Start playing!
//                    videoPostViewVV.start()
//                }
//            })
//
//        // Listener for onCompletion() event (runs after media has finished
//        // playing).
//        videoPostViewVV.setOnCompletionListener(
//            object : MediaPlayer.OnCompletionListener {
//                override fun onCompletion(mediaPlayer: MediaPlayer?) {
//                    Toast.makeText(
//                        mActivity,
//                        "Video Complete",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    // Return the video position to the start.
//                    videoPostViewVV.seekTo(0)
//                }
//            })
//    }
//
//}