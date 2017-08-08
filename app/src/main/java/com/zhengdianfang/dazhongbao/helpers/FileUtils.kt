package com.zhengdianfang.dazhongbao.helpers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
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

    fun getAbsolutePathByUri(context: Context, uri: Uri): String? {
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }
}