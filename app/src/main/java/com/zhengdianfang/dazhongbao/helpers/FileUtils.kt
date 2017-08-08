package com.zhengdianfang.dazhongbao.helpers

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import java.io.ByteArrayOutputStream
import java.io.File


/**
 * Created by dfgzheng on 30/07/2017.
 */
object FileUtils{
     val APP_DIR = "dazongbao"

    fun createTakePhotoImagePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + File.separator +
                FileUtils.APP_DIR + File.separator +  System.currentTimeMillis() + ".jpg"
    }

    fun readRawFile(context: Context?, rawId: Int): String? {
        val inputStream = context?.resources?.openRawResource(rawId)
        val inputString = inputStream?.bufferedReader().use { it?.readText() }
        return inputString
    }

    fun getBitmapDegree(imagePath: String): Float{
        val exifInterface = ExifInterface(imagePath)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        var degree = 0
        when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {degree = 90}
            ExifInterface.ORIENTATION_ROTATE_180 -> {degree = 180}
            ExifInterface.ORIENTATION_ROTATE_270 -> {degree = 270}
        }
        return degree.toFloat()
    }

    fun rotateBitmapByDegree(bitmap: Bitmap?, degree: Float): Bitmap? {
        if (null == bitmap) {
            return null
        }
        val matrix = Matrix()
        matrix.postRotate(degree)
        val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return newBitmap
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, destWidth: Int, destHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > destHeight || width > destWidth) {
            val halfWidth = width / 2
            val halfHeight = height / 2
            while ((halfWidth / inSampleSize) > destWidth && (halfHeight / inSampleSize) > destHeight){
               inSampleSize *= 2
            }
            var totalPixels = width * height / inSampleSize
            val totalDestPixels = destHeight * destWidth* 2
            while (totalPixels > totalDestPixels){
                inSampleSize *= 2
                totalPixels /= 2
            }
        }
        return inSampleSize
    }

    fun decodeBitmapFromFile(imagePath: String, requestWidth: Int = -1, requestHeight: Int = -1): Bitmap? {
        var bitmap: Bitmap? = null
        if (!TextUtils.isEmpty(imagePath)) {
            if (requestWidth <= 0 || requestHeight <=0) {
               bitmap = BitmapFactory.decodeFile(imagePath)
            }else{
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(imagePath, options)
                if(options.outWidth == -1 || options.outHeight == -1) {
                    val exifInterface = ExifInterface(imagePath)
                    val width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL)
                    val height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL)
                    options.outWidth = width
                    options.outHeight = height
                }
                options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight)
                options.inJustDecodeBounds = false
                bitmap = BitmapFactory.decodeFile(imagePath, options)
            }
        }
        bitmap = rotateBitmapByDegree(bitmap, getBitmapDegree(imagePath))
        return bitmap
    }

    fun bitmapToByte(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return byteArray
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri!!, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.lastPathSegment

            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    fun getDataColumn(context: Context, uri: Uri, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * *
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * *
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * *
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * *
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}