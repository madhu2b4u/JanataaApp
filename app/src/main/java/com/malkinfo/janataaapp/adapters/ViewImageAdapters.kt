package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.chrisbanes.photoview.PhotoView
import com.malkinfo.janataaapp.R

class ViewImageAdapters(var context:Context):
    RecyclerView.Adapter<ViewImageAdapters.ViewImgViewHolder>()
{
    private var viewImgList :ArrayList<String> = ArrayList()


    private val mCurrentPosition = 0

    fun addViewImgList(isViewImgList :ArrayList<String>){
        this.viewImgList = isViewImgList

        notifyDataSetChanged()
    }
        inner class ViewImgViewHolder(var v:View):RecyclerView.ViewHolder(v){
            val zoomImageIV = v.findViewById<PhotoView>(R.id.viewZoomImageIV)
            val myvideoPlay = v.findViewById<VideoView>(R.id.myVideoPlayerVV)
            val videoFramL = v.findViewById<FrameLayout>(R.id.videoFramL)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewImgViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.view_img_layout,parent,false)
        return ViewImgViewHolder(v)
    }

    override fun getItemCount(): Int = viewImgList.size

    override fun onBindViewHolder(holder: ViewImgViewHolder, position: Int) {
       var imageUrl :String  = viewImgList[position]

        if (imageUrl.endsWith(".mp4")){
           holder.zoomImageIV.visibility = View.GONE
            holder.videoFramL.visibility = View.VISIBLE
            setVideoPath(Uri.parse(imageUrl),holder.myvideoPlay)

        }else{
            holder.zoomImageIV.visibility = View.VISIBLE
            holder.videoFramL.visibility = View.GONE
            holder.zoomImageIV.load(imageUrl)
        }
    }
    private fun setVideoPath(uri: Uri?, videoPostViewVV:VideoView) {
        // Show the "Buffering..." message while the video loads.
        videoPostViewVV.visibility = View.VISIBLE
        if (uri != null) {
            videoPostViewVV.setVideoURI(uri)
        }
        val mediaController = MediaController(context)
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
}