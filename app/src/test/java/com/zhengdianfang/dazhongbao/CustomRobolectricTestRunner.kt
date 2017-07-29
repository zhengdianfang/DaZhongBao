package com.zhengdianfang.dazhongbao

import org.junit.runners.model.InitializationError
import org.robolectric.RobolectricTestRunner



/**
 * Created by dfgzheng on 28/07/2017.
 */
class AppRobolectricTestRunner @Throws(InitializationError::class)
constructor(testClass: Class<*>) : RobolectricTestRunner(testClass) {

    init {
        val buildVariant = BuildConfig.BUILD_TYPE + if (BuildConfig.FLAVOR.isEmpty()) "" else "/" + BuildConfig.FLAVOR
        System.setProperty("android.package", BuildConfig.APPLICATION_ID)
        System.setProperty("android.manifest", "build/intermediates/manifests/full/$buildVariant/AndroidManifest.xml")
        System.setProperty("android.resources", "build/intermediates/res/" + buildVariant)
        System.setProperty("android.assets", "build/intermediates/assets/" + buildVariant)
    }
}