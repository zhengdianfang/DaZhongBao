package com.zhengdianfang.dazhongbao

import android.app.Application
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import com.miracle.redux.Store
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.product.SharesInfo
import com.zhengdianfang.dazhongbao.reducers.AppState
import com.zhengdianfang.dazhongbao.reducers.MyProductReducer
import com.zhengdianfang.dazhongbao.reducers.RootReducer
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
    var store = Store.create(RootReducer(arrayOf(MyProductReducer())), AppState())

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
    }

    fun isLogin(): Boolean {
       return loginUser != null
    }
}