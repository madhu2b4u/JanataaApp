package com.malkinfo.janataaapp.adapters.homeadapter

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
import com.malkinfo.janataaapp.models.community.homemodel.HomePostModel

/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class BannerAdsAdapter(var context: Context):
    RecyclerView.Adapter<BannerAdsAdapter.BannerAdViewHolder>()
{
    private var viewImgList :ArrayList<HomePostModel> = ArrayList()


    private val mCurrentPosition = 0

    fun addViewImgList(isViewImgList :ArrayList<HomePostModel>){
        this.viewImgList = isViewImgList

        notifyDataSetChanged()
    }
    inner class BannerAdViewHolder(var v: View): RecyclerView.ViewHolder(v){
        val bannerAdzoomImageIV = v.findViewById<PhotoView>(R.id.bannerAdviewZoomImageIV)
        val bannerAdvideoPlay = v.findViewById<VideoView>(R.id.bannerAdVideoPlayerVV)
        val bannerAdvideoFramL = v.findViewById<FrameLayout>(R.id.bannerAdvideoFramL)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.banner_item_imgs,parent,false)
        return BannerAdViewHolder(v)
    }

    override fun getItemCount(): Int = viewImgList.size

    override fun onBindViewHolder(holder: BannerAdViewHolder, position: Int) {
        var imageUrl  = viewImgList[position]
        for (images in imageUrl.image_url!!){
            if (images.endsWith(".mp4")){
                holder.bannerAdzoomImageIV.visibility = View.GONE
                holder.bannerAdvideoFramL.visibility = View.VISIBLE
                setVideoPath(Uri.parse(images),holder.bannerAdvideoPlay)

            }else{
                holder.bannerAdzoomImageIV.visibility = View.VISIBLE
                holder.bannerAdvideoFramL.visibility = View.GONE
                holder.bannerAdzoomImageIV.load(images)
            }
        }


    }
    private fun setVideoPath(uri: Uri?, videoPostViewVV: VideoView) {
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