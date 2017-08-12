package com.zhengdianfang.dazhongbao

import android.app.Application
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.api.API
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
        set(value) {
            val preference = PreferenceManager.getDefaultSharedPreferences(this)
            preference.edit().putString("login_user", API.objectMapper.writeValueAsString(value)).apply()
        }
        get() {
            if (field == null) {
                val preference = PreferenceManager.getDefaultSharedPreferences(this)
                val json = preference.getString("login_user", "")
                if (!TextUtils.isEmpty(json)){
                    field = API.objectMapper.readValue<User>(json, User::class.java)
                }
            }
            return field
        }


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