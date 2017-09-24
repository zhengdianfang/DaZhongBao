package com.zhengdianfang.dazhongbao.presenters.validates

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 14/08/2017.
 */
class UserInfoInterityValidate(mIView: IView?) : BaseValidate(mIView) {

    companion object {
        val NO_EXITS_BUSINESS_CARD = 0
        val NO_EXITS_BUSINESS_LINLENCE_CARD = 1
        val NO_EXITS_CONTACT_CARD = 3
    }

    fun validate(): Boolean {
        var res = true
        if (checkLogin()){
            val loginUser = CApplication.INSTANCE.loginUser!!
            when(loginUser.type) {
                User.ORGANIZATION_TYPE -> {
                    if (loginUser.isExitsBusinessLicenceCard().not()) {
                        (mIView as BasePresenter.ICheckUserIntegrityView).notIntegrity(NO_EXITS_BUSINESS_LINLENCE_CARD)
                        res = false
                    }
                    if (loginUser.isExitsBusinessCard().not()){
                        (mIView as BasePresenter.ICheckUserIntegrityView).notIntegrity(NO_EXITS_BUSINESS_CARD)
                       res = false
                    }
                }
                User.PERSONAL_TYPE -> {
                    if (loginUser.isExitsContactCard().not()){
                        (mIView as BasePresenter.ICheckUserIntegrityView).notIntegrity(NO_EXITS_CONTACT_CARD)
                        res = false
                    }
                }
            }
        }
        return res
    }
}