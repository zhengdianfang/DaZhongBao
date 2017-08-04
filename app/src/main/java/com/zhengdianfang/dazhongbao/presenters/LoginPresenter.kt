package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.helpers.PhoneFormatCheckHelper
import com.zhengdianfang.dazhongbao.models.login.LoginRepository
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.login.IBaseView
import com.zhengdianfang.dazhongbao.views.login.ILoginView
import com.zhengdianfang.dazhongbao.views.login.IRegisterView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 30/07/2017.
 */
class LoginPresenter: BasePresenter() {

    private val mLoginRepository by lazy { LoginRepository() }

    fun loginByPhoneNumber(phoneNumber: String, password: String, deviceId: String){

        if (validatePhoneNumber(phoneNumber) && validatePassword(password)){
            //request login api
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.loginRequest(phoneNumber, password,
                    DeviceUtils.getAppVersionName(mView?.getContext()),
                    deviceId), Consumer<User> { user ->
                (mView as ILoginView).userResponseProcessor(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun validatePassword(password: String): Boolean {
        var legal = false
        if (password.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_input_password)
        }else if (password.length < 6) {
            (mView as IBaseView).validateErrorUI(R.string.please_input_legal_password)
        }else{
            legal = true
        }
        return legal
    }

    fun validatePhoneNumber(phoneNumber: String): Boolean {
        var legal = false
        if (mView != null && mView is IBaseView) {
            if(phoneNumber.isNullOrEmpty()) {
                (mView as IBaseView).validateErrorUI(R.string.please_input_phonenumber)
            }else if (phoneNumber.length < 11 || !PhoneFormatCheckHelper.isPhoneLegal(phoneNumber)) {
                (mView as IBaseView).validateErrorUI(R.string.please_input_legal_phonenumber)
            }else{
                legal = true
            }
        }
        return legal
    }

    fun validateSmsVerifyCode(verifyCode: String): Boolean {
        var legal = true
        if(verifyCode.isNullOrEmpty()) {
            legal = false
            (mView as IRegisterView).validateErrorUI(R.string.please_input_sms_code)
        }
        return legal
    }

    fun requestSmsVerifyCode(phoneNumber: String) {
        if (validatePhoneNumber(phoneNumber)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.getSmsVerifyCode(phoneNumber, 1), Consumer<String> { code ->
                (mView as IRegisterView).receiverSmsCode(code)
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

    fun setPassword(password: String, confirmPassword: String, token: String) {
        if (validatePassword(password) && isEqualConfirmPassword(password, confirmPassword)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.modifyPassword(password, token), Consumer<User> { user->
                (mView as IRegisterView).receiverUser(user)
                mView?.hideLoadingDialog()
            })
        }
    }
    fun isEqualConfirmPassword(password: String, confirmPassword: String): Boolean {
        var equal = true
        if (password != confirmPassword) {
            equal = false
            (mView as IRegisterView).validateErrorUI(R.string.toast_input_confirm_password_not_equal)
        }
        return equal
    }
}