package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.login.LoginRepository
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.validates.PasswordValidate
import com.zhengdianfang.dazhongbao.presenters.validates.PhoneNumberValidate
import com.zhengdianfang.dazhongbao.presenters.validates.VerifySmsCodeValidate
import com.zhengdianfang.dazhongbao.views.login.ILoginView
import com.zhengdianfang.dazhongbao.views.login.IRegisterView
import com.zhengdianfang.dazhongbao.views.login.ISendSmsCode
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 30/07/2017.
 */
class LoginPresenter: BasePresenter() {

    private val mLoginRepository by lazy { LoginRepository(MOCK) }
    private val passwordValidate by lazy { PasswordValidate(mView) }
    private val phoneNumberValidate by lazy { PhoneNumberValidate(mView) }
    private val verifySmsCodeValidate by lazy { VerifySmsCodeValidate(mView) }

    fun loginByPhoneNumber(phoneNumber: String, password: String, deviceId: String, versionName: String){

        if (phoneNumberValidate.validate(phoneNumber)
                && passwordValidate.validate(password)){
            //request login api
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.loginRequest(phoneNumber, password, versionName, deviceId), Consumer<User> { user ->
                (mView as ILoginView).userResponseProcessor(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun loginByThridParty(openId: String) {
        mView?.showLoadingDialog()
        addSubscription(mLoginRepository.loginByThridParty(openId), Consumer { user ->
            (mView as ILoginView).userResponseProcessor(user)
            mView?.hideLoadingDialog()
        }, Consumer {
            (mView as ILoginView).thirdPartyNotBinded(openId)
            mView?.hideLoadingDialog()
        })
    }

    fun requestSmsVerifyCode(phoneNumber: String, type: Int) {
        if (phoneNumberValidate.validate(phoneNumber)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.getSmsVerifyCode(phoneNumber, type), Consumer<String> { code ->
                (mView as ISendSmsCode).receiverSmsCode(code)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun requestRegister(phoneNumber: String, verifyCode: String, recommendPerson: String) {
        if (phoneNumberValidate.validate(phoneNumber)
                && verifySmsCodeValidate.validate(verifyCode)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.register(phoneNumber, verifyCode, recommendPerson), Consumer<User> { user->
                (mView as IRegisterView).receiverUser(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun registerByThridParty(openId: String, phoneNumber: String, verifyCode: String) {
        if (phoneNumberValidate.validate(phoneNumber)
                && verifySmsCodeValidate.validate(verifyCode)){
            mView?.showLoadingDialog()
            addSubscription(mLoginRepository.registerByThridParty(phoneNumber, verifyCode, openId), Consumer { user->
                (mView as IRegisterView).receiverUser(user)
                mView?.hideLoadingDialog()
            })
        }
    }
}