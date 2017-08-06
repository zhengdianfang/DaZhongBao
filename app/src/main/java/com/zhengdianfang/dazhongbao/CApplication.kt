package com.zhengdianfang.dazhongbao

import android.app.Application
import com.zhengdianfang.dazhongbao.models.login.User
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
    }

    fun isLogin(): Boolean {
       return loginUser != null
    }
}