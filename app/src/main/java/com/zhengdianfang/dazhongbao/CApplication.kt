package com.zhengdianfang.dazhongbao

import android.app.Application
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.product.SharesInfo
import java.io.File
import kotlin.properties.Delegates

/**
 * Created by dfgzheng on 30/07/2017.
 */
class CApplication : Application(){
    companion object {
        var INSTANCE: CApplication by Delegates.notNull()
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

    val shareInfosCache = mutableListOf<SharesInfo>()

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + FileUtils.APP_DIR)
        if (appDir.exists().not()) {
            appDir.mkdirs()
        }
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        initEMChat()
    }

    fun isLogin(): Boolean {
       return loginUser != null
    }

    private fun initEMChat() {
        val emOptions = EMOptions()
        emOptions.acceptInvitationAlways = false
        EMClient.getInstance().init(this, emOptions)
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)
    }
}