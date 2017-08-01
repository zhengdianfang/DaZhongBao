package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PhoneFormatCheckHelper
import com.zhengdianfang.dazhongbao.models.login.LoginRepository
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.login.ILoginView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 30/07/2017.
 */
class LoginPresenter: BasePresenter() {

    private val mLoginRepository by lazy { LoginRepository() }

    fun loginByPhoneNumber(phoneNumber: String, password: String){

        if (validatePhoneNumberAndPassword(phoneNumber, password)){
            //request login api
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.loginRequest(phoneNumber, password), Consumer<User> { user ->
                (mView as ILoginView).userResponseProcessor(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun validatePhoneNumberAndPassword(phoneNumber: String, password: String): Boolean {
        //validate paramters
        var legal = false
        if (mView != null && mView is ILoginView) {
            if(phoneNumber.isNullOrEmpty()) {
                (mView as ILoginView).validateErrorUI(R.string.please_input_phonenumber)
            }else if (phoneNumber.length < 11 || !PhoneFormatCheckHelper.isPhoneLegal(phoneNumber)) {
                (mView as ILoginView).validateErrorUI(R.string.please_input_legal_phonenumber)
            }else if (password.isNullOrEmpty()){
                (mView as ILoginView).validateErrorUI(R.string.please_input_password)
            }else if (password.length < 6) {
                (mView as ILoginView).validateErrorUI(R.string.please_input_legal_password)
            }else{
                legal = true
            }
        }

        return legal
    }
}