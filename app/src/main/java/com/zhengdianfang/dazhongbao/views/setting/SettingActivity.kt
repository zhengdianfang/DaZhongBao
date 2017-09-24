package com.zhengdianfang.dazhongbao.views.setting

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.basic.WebActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar

class SettingActivity : BaseActivity() {

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val logoutConfirmDialog by lazy {
        MaterialDialog.Builder(this)
                .content(R.string.confrim_logout_content)
                .positiveText(R.string.confirm_label)
                .negativeText(R.string.cancel_label)
                .onPositive { dialog, _ ->
                    CApplication.INSTANCE.logout()
                    dialog.cancel()
                    SettingActivity@this.finish()
                }.onNegative { dialog, _ -> dialog.cancel() }
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<ViewGroup>(R.id.accountViewGroup).setOnClickListener {
            startFragment(android.R.id.content, AccountSettingFragment(), "setting")
        }

        toolBar.backListener = {
           finish()
        }

        findViewById<TextView>(R.id.logoutButton).setOnClickListener {
            logoutConfirmDialog.show()
        }

        findViewById<View>(R.id.userAgreementGroup).setOnClickListener {
           WebActivity.startActivity(this, getString(R.string.setting_user_agreement), Constants.USER_LINCENCE)
        }
        findViewById<View>(R.id.aboutUsGroup).setOnClickListener {
            WebActivity.startActivity(this, getString(R.string.setting_about_us), Constants.ABOUT_ME)
        }
    }
}
