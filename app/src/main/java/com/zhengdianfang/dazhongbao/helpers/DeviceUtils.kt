package com.zhengdianfang.dazhongbao.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager

/**
 * Created by dfgzheng on 01/08/2017.
 */
object DeviceUtils {

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceId(context: Context?): String {
        val telephonyManager = context?.applicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId ?: ""
    }

    fun getAppVersionName(context: Context?): String {
       return context?.packageManager?.getPackageInfo(context.packageName, 0)!!.versionName
    }
}