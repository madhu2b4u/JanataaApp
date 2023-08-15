package com.malkinfo.janataaapp.utitlis

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore

fun generatePath(uri: Uri, context: Context):String? {
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
private fun generateFromKitkat(uri: Uri, context: Context):String? {
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








