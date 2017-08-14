package com.zhengdianfang.dazhongbao.presenters.validates

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PhoneFormatCheckHelper
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 14/08/2017.
 */
class PhoneNumberValidate(mIView: IView?) : BaseValidate(mIView) {

    fun validate(phoneNumber: String): Boolean {
        var legal = false
        if (mIView != null && mIView is IView) {
            if(phoneNumber.isNullOrEmpty()) {
                mIView.validateErrorUI(R.string.please_input_phonenumber)
            }else if (phoneNumber.length < 11 || !PhoneFormatCheckHelper.isPhoneLegal(phoneNumber)) {
                mIView.validateErrorUI(R.string.please_input_legal_phonenumber)
            }else{
                legal = true
            }
        }
        return legal
    }
}