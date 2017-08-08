package com.zhengdianfang.dazhongbao

import android.app.Application
import android.os.Environment
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.login.User
import java.io.File
import kotlin.properties.Delegates

/**
 * Created by dfgzheng on 30/07/2017.
 */
class CApplication : Application(){
    companion object {
        var INSTANCE: CApplication by Delegates.notNull<CApplication>()
    }

    var loginUser: User? = null

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + FileUtils.APP_DIR)
        if (appDir.exists().not()) {
            appDir.mkdirs()
        }
    }

    fun isLogin(): Boolean {
       return loginUser != null
    }
}