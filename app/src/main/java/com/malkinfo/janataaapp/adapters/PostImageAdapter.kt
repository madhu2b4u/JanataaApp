package com.malkinfo.janataaapp.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.views.main.community.ViewImageDialogs


class PostImageAdapter (var context: Context, var postImageModel: ArrayList<String>):
        RecyclerView.Adapter<PostImageAdapter.ViewHolder>(){


    var onPostImageClicked : ((Int)-> Unit)? = null
    private lateinit var photoDialog: Dialog
    /**set video */
    private val mCurrentPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostImageAdapter.ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_image_item, null)
        return ViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return postImageModel.size
    }

    override fun onBindViewHolder(holder: PostImageAdapter.ViewHolder, position: Int) {

        if (postImageModel[position].endsWith(".mp4")){
            holder.horizontalIV.visibility = View.GONE
            holder.framVidL.visibility = View.VISIBLE
            setVideoPath(Uri.parse(postImageModel[position]),holder.mVdPostVV)
        }else{
            holder.horizontalIV.visibility = View.VISIBLE
            holder.framVidL.visibility = View.GONE
            holder.horizontalIV.load(postImageModel[position])
        }




    }
    private fun setVideoPath(uri: Uri?,videoPostViewVV:VideoView) {
        // Show the "Buffering..." message while the video loads.
        videoPostViewVV.visibility = View.VISIBLE
        if (uri != null) {
            videoPostViewVV.setVideoURI(uri)
        }
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
                    Toast.makeText(
                        context,
                        "Video Complete",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Return the video position to the start.
                    videoPostViewVV.seekTo(0)
                }
            })
    }

    inner class ViewHolder (view: View):RecyclerView.ViewHolder(view){

        var horizontalIV:ImageView =view.findViewById(R.id.horizontalIV)
        var mVdPostVV :VideoView = view.findViewById(R.id.mVdPostVV)
        var framVidL :FrameLayout = view.findViewById(R.id.framVidL)


        init {
            horizontalIV.setOnClickListener {
               // onPostImageClicked?.invoke(adapterPosition)
//                photoDialog.setContentView(R.layout.post_image_layout)
              /*  photoDialog = Dialog(context)
                photoDialog.setContentView(R.layout.post_image_layout)
                photoDialog.setCanceledOnTouchOutside(true)
                photoDialog.show()
                photoDialog.postZoomIV.load(postImageModel[adapterPosition])*/
                val viewImgDialog = ViewImageDialogs(context as Activity)
                viewImgDialog.stowImageSide(postImageModel,adapterPosition)
                mVdPostVV.pause()

//                val intent = Intent(context, ViewImageActivity::class.java)
//                intent.putExtra("POST_IMAGE_URL",postImageModel[adapterPosition])
//                context.startActivity(intent)
            }
            mVdPostVV.setOnClickListener {
//                val intent = Intent(context, ViewImageActivity::class.java)
//                intent.putExtra("POST_IMAGE_URL",postImageModel[adapterPosition])
//                context.startActivity(intent)
                mVdPostVV.pause()
                val viewImgDialog = ViewImageDialogs(context as Activity)
                viewImgDialog.stowImageSide(postImageModel,adapterPosition)
            }

        }
    }
}