package com.zhengdianfang.dazhongbao.views.setting

import android.os.Bundle
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar

class SettingActivity : BaseActivity() {

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<ViewGroup>(R.id.accountViewGroup).setOnClickListener {
            startFragment(android.R.id.content, AccountSettingFragment(), "setting")

        }

        toolBar.backListener = {
           finish()
        }
    }
}
