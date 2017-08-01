package com.zhengdianfang.dazhongbao.views.login

import android.os.Bundle
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.PresenterActivity

class LoginActivity : PresenterActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
