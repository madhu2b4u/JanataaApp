package com.malkinfo.janataaapp.adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.models.base.FileUploadItem

class MediaFromGalleryAdapter(var context: Context, var galleryModel: ArrayList<FileUploadItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onAddMoreMediaClicked: ((Int) -> Unit)? = null
    var onDeleteMediaClicked: ((Int) -> Unit)? = null
    private val mCurrentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutView: View

        return if (viewType == ITEM_VIEW) {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.media_from_gallery_item, null)
            ItemViewHolder(layoutView)
        } else if (viewType == ITEM_FOOTER) {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.add_media_footer_item, null)
            FooterViewHolder(layoutView)
        } else {
            layoutView =
                LayoutInflater.from(parent.context).inflate(R.layout.media_from_gallery_item, null)
            ItemViewHolder(layoutView)
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is ItemViewHolder){
            if (galleryModel[position].fileurl!!.endsWith(".mp4")){
                holder.vidFramL.visibility = View.VISIBLE
                holder.galleryIV.visibility = GONE
                setVideoPath(Uri.parse(galleryModel[position].fileurl),
                    holder.videoPostViewVV)
            }else{
                holder.vidFramL.visibility = GONE
                holder.galleryIV.visibility  = VISIBLE
                holder.galleryIV.load(galleryModel[position].fileurl)
            }

        }else if(holder is FooterViewHolder){

            if(position<=3){
                holder.addMoreMediaCV.visibility=VISIBLE
            }else{
                holder.addMoreMediaCV.visibility= GONE
            }
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



    override fun getItemCount(): Int {
        return galleryModel.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == galleryModel.size) ITEM_FOOTER else ITEM_VIEW
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var galleryIV:ImageView=itemView.findViewById(R.id.galleryIV)
        var deleteCV:CardView=itemView.findViewById(R.id.deleteCV)
        var videoPostViewVV: VideoView = itemView.findViewById(R.id.videoPostViewVV)
        var vidFramL: FrameLayout = itemView.findViewById(R.id.vidFramL)

        init {
            deleteCV.setOnClickListener {
                onDeleteMediaClicked?.invoke(adapterPosition)
            }
        }

    }

    inner class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView) {
        var addMoreMediaCV:CardView =footerView.findViewById(R.id.addMoreMediaCV)

        init {
            addMoreMediaCV.setOnClickListener {
                onAddMoreMediaClicked?.invoke(adapterPosition)
            }
        }
    }


    companion object {
        private val ITEM_VIEW = 1
        private val ITEM_FOOTER = 2

    }
}