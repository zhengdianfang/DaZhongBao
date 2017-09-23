package com.zhengdianfang.dazhongbao.views.login

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity


class LoginActivity : BaseActivity(){

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarTheme(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, Color.WHITE)
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
                .add(android.R.id.content, LoginFragment())
                .addToBackStack("login")
                .commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
