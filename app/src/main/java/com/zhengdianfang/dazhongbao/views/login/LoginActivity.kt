package com.zhengdianfang.dazhongbao.views.login

import android.os.Bundle
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity


class LoginActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
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
