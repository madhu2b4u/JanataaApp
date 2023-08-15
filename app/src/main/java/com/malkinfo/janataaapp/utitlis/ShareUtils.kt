package com.malkinfo.janataaapp.utitlis

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.utils.ImageUtils
import de.hdodenhof.circleimageview.CircleImageView
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class ShareUtils {

    companion object{
        fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri? {
            val path: String = context.externalCacheDir.toString() + "/testImg.jpg"
            var out: OutputStream? = null
            val file = File(path)
            try {
                out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return FileProvider.getUriForFile(
                context, context.packageName + ".provider", file
            )
        }

        fun shareImageToOthers(context: Context,text: String?,bitmap: Bitmap?) {
            val imageUri: Uri? = bitmap?.let { saveBitmapAndGetUri(context, it) }
            val chooserIntent = Intent(Intent.ACTION_SEND)
            chooserIntent.type = "image/*"
            chooserIntent.putExtra(Intent.EXTRA_TEXT, text)
            chooserIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(chooserIntent)
            } catch (ex: Exception) {
            }
        }

        fun sharePost(context: Context,userProfileImg:String?,profileName:String?,postDescp:String?,msgDeption:String) {
            val view = LayoutInflater.from(context).inflate(R.layout.share_postcard_item,null)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            view.layoutParams = layoutParams
            val profileImg = view.findViewById<CircleImageView>(R.id.profileImg)
            val userName = view.findViewById<TextView>(R.id.userName)
           // val postText = view.findViewById<TextView>(R.id.postText)
            Log.d("mTag","profile img = $userProfileImg")
            val bitmap = getBitmapFromURL(userProfileImg)
            profileImg.setImageBitmap(bitmap)
            userName.text = "Posted by $profileName"
            //postText.text = postDescp
            capture(context,view,msgDeption)

        }
        private fun capture(context: Context,view: View, descp:String?) {
            val bitmap = ImageUtils.generateShareImage(view)
            ShareUtils.shareImageToOthers(context,descp, bitmap)
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
    }
}