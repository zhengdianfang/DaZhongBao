package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PhoneFormatCheckHelper
import com.zhengdianfang.dazhongbao.models.login.LoginRepository
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.basic.IView
import com.zhengdianfang.dazhongbao.views.login.ILoginView
import com.zhengdianfang.dazhongbao.views.login.IRegisterView
import com.zhengdianfang.dazhongbao.views.login.ISendSmsCode
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 30/07/2017.
 */
class LoginPresenter: BasePresenter() {

    private val mLoginRepository by lazy { LoginRepository() }

    fun loginByPhoneNumber(phoneNumber: String, password: String, deviceId: String, versionName: String){

        if (validatePhoneNumber(phoneNumber) && validatePassword(password)){
            //request login api
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.loginRequest(phoneNumber, password, versionName, deviceId), Consumer<User> { user ->
                (mView as ILoginView).userResponseProcessor(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun validatePassword(password: String): Boolean {
        var legal = false
        if (password.isNullOrEmpty()){
            (mView as IView).validateErrorUI(R.string.please_input_password)
        }else if (password.length < 6) {
            (mView as IView).validateErrorUI(R.string.please_input_legal_password)
        }else{
            legal = true
        }
        return legal
    }

    fun validatePhoneNumber(phoneNumber: String): Boolean {
        var legal = false
        if (mView != null && mView is IView) {
            if(phoneNumber.isNullOrEmpty()) {
                (mView as IView).validateErrorUI(R.string.please_input_phonenumber)
            }else if (phoneNumber.length < 11 || !PhoneFormatCheckHelper.isPhoneLegal(phoneNumber)) {
                (mView as IView).validateErrorUI(R.string.please_input_legal_phonenumber)
            }else{
                legal = true
            }
        }
        return legal
    }

    private fun validateSmsVerifyCode(verifyCode: String): Boolean {
        var legal = true
        if(verifyCode.isNullOrEmpty()) {
            legal = false
            (mView as IRegisterView).validateErrorUI(R.string.please_input_sms_code)
        }
        return legal
    }

    fun requestSmsVerifyCode(phoneNumber: String, type: Int) {
        if (validatePhoneNumber(phoneNumber)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.getSmsVerifyCode(phoneNumber, type), Consumer<String> { code ->
                (mView as ISendSmsCode).receiverSmsCode(code)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun requestRegister(phoneNumber: String, verifyCode: String, recommendPerson: String) {
        if (validatePhoneNumber(phoneNumber) && validateSmsVerifyCode(verifyCode)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.register(phoneNumber, verifyCode, recommendPerson), Consumer<User> { user->
                (mView as IRegisterView).receiverUser(user)
                mView?.hideLoadingDialog()
            })
        }
    }

}