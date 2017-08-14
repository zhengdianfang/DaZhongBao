package com.zhengdianfang.dazhongbao.presenters.validates

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 14/08/2017.
 */
class PasswordValidate(mIView: IView?) : BaseValidate(mIView) {

    fun validate(password: String): Boolean {
        var legal = false
        if (password.isNullOrEmpty()){
            mIView?.validateErrorUI(R.string.please_input_password)
        }else if (password.length < 6) {
            mIView?.validateErrorUI(R.string.please_input_legal_password)
        }else{
            legal = true
        }
        return legal
    }
}