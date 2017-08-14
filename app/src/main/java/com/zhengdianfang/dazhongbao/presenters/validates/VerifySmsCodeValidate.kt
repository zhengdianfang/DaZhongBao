package com.zhengdianfang.dazhongbao.presenters.validates

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 14/08/2017.
 */
class VerifySmsCodeValidate(mIView: IView?) : BaseValidate(mIView) {

    fun validate(verifyCode:String): Boolean {
        var legal = true
        if(verifyCode.isNullOrEmpty()) {
            legal = false
            mIView?.validateErrorUI(R.string.please_input_sms_code)
        }
        return legal
    }
}