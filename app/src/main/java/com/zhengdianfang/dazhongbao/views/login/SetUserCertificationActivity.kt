package com.zhengdianfang.dazhongbao.views.login

import android.os.Bundle
import com.zhengdianfang.dazhongbao.helpers.AppUtils
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity

/**
 * Created by dfgzheng on 24/09/2017.
 */
class SetUserCertificationActivity: BaseActivity(), BasePresenter.ICheckUserIntegrityView {

    private val userPresenter = UserPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPresenter.attachView(this)
        if (userPresenter.checkUserInterity()) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
    }

    override fun notIntegrity(type: Int) {
        AppUtils.interityUserInfo(this, type)
    }
}