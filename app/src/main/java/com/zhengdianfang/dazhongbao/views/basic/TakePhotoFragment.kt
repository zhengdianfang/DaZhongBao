package com.zhengdianfang.dazhongbao.views.basic

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.helpers.FileUtils

/**
 * Created by dfgzheng on 08/08/2017.
 j*/
abstract  class TakePhotoFragment : BaseFragment(){

     protected  var takePhotoImagePath = ""
     protected val mediaDialog by lazy {
      MaterialDialog.Builder(this.getParentActivity()).theme(Theme.LIGHT)
              .items(R.array.media_list)
              .itemsCallback({ dialog, itemView, position, text ->
               if(position == 0){
                   takePhoto()
               }else {
                   pickPhoto()
               }
              }).build()
     }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      if (resultCode == Activity.RESULT_OK){
       if (requestCode == DeviceUtils.TAKE_PHOTO){
        val bitmap = FileUtils.decodeBitmapFromFile(takePhotoImagePath, this.getPhotoWidth(), this.getPhotoHeight())
        if (null != bitmap) {
            takePhotoCallback(bitmap)
        }
       }else if (requestCode == DeviceUtils.PICK_PHOTO){
        if (data?.data != null){
            FileUtils.getPathFromUri(activity, data.data!!, {path ->
                this.takePhotoImagePath = path ?: ""
                pickPhotoCallback(path ?: "")
            })
        }
       }
      }
     }

     protected fun takePhoto() {
         takePhotoImagePath = DeviceUtils.takePhoto(this)
     }

     protected fun pickPhoto() {
         DeviceUtils.pickPhoto(this)
     }

     abstract fun getPhotoWidth(): Int
     abstract fun getPhotoHeight(): Int
     abstract  fun takePhotoCallback(bitmap: Bitmap)
     abstract fun pickPhotoCallback(imagePath: String)

}