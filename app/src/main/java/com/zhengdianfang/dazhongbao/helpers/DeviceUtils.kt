package com.zhengdianfang.dazhongbao.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.telephony.TelephonyManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import java.io.File


/**
 * Created by dfgzheng on 01/08/2017.
 */
object DeviceUtils {
    val TAKE_PHOTO = 0x000001
    val PICK_PHOTO = 0x000002

    @SuppressLint("MissingPermission")
    fun getDeviceId(context: Context?): String {
        val telephonyManager = context?.applicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId ?: ""
    }

    fun getAppVersionName(context: Context?): String {
        return context?.packageManager?.getPackageInfo(context.packageName, 0)!!.versionName
    }

    fun takePhoto(activity: Activity): String {
        val imagePath = FileUtils.createTakePhotoImagePath()
        RxPermissions(activity).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { granted ->
            if (granted) {
                val photoFile = File(imagePath)
                photoFile.createNewFile()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photoURI: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //如果是7.0及以上的系统使用FileProvider的方式创建一个Uri
                    photoURI = FileProvider.getUriForFile(activity.applicationContext, activity.applicationContext.packageName, photoFile)
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                } else {
                    //7.0以下使用这种方式创建一个Uri
                    photoURI = Uri.fromFile(photoFile)
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(intent, TAKE_PHOTO)
            }
        }

        return imagePath
    }

    fun takePhoto(fragment: Fragment): String {
        val imagePath = FileUtils.createTakePhotoImagePath()
        RxPermissions(fragment.activity).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { granted ->
            if (granted) {
                val photoFile = File(imagePath)
                photoFile.createNewFile()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photoURI: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //如果是7.0及以上的系统使用FileProvider的方式创建一个Uri
                    photoURI = FileProvider.getUriForFile(fragment.context.applicationContext, fragment.context.applicationContext.packageName, photoFile)
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                } else {
                    //7.0以下使用这种方式创建一个Uri
                    photoURI = Uri.fromFile(photoFile)
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                fragment.startActivityForResult(intent, TAKE_PHOTO)
            }
        }
        return imagePath
    }

    fun pickPhoto(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(intent, PICK_PHOTO)
    }

    fun pickPhoto(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(intent, PICK_PHOTO)
    }


    @SuppressLint("MissingPermission")
    fun callPhone(activity: BaseActivity, phoneNumber: String){
        RxPermissions(activity).request(Manifest.permission.CALL_PHONE).subscribe { granted ->
            if (granted){
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                activity.startActivity(intent)
            }else{
               activity.toast("请在设置中给予应用电话权限")
            }
        }
    }
}
