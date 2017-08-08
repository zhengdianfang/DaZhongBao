package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PhoneFormatCheckHelper
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.login.UserRepository
import com.zhengdianfang.dazhongbao.views.login.IBaseView
import com.zhengdianfang.dazhongbao.views.login.IRegisterView
import com.zhengdianfang.dazhongbao.views.login.IUploadCard
import com.zhengdianfang.dazhongbao.views.login.IVerifySmsCode
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 08/08/2017.
 */
class UserPresenter: BasePresenter() {

    private val mUserRepository by lazy { UserRepository() }

    fun setPassword(password: String, token: String) {
        if (validatePassword(password)){
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.modifyPassword(password, token), Consumer<User> { user->
                (mView as IRegisterView).receiverUser(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun modifyPassword(password: String, confirmPassword: String, token: String) {
        if (validatePassword(password) && isEqualConfirmPassword(password, confirmPassword)){
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.modifyPassword(password, token), Consumer<User> { user->
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


    fun uploadBusinessLicenceCard(token: String, contactName: String, companyName: String, filePath: String) {
        if (validateBusinessLincenceCardUploadParams(contactName, companyName, filePath)) {
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.uploadBusinessLicenceCard(token, contactName, companyName, filePath), Consumer<User> { user->
                (mView as IUploadCard).uploadSuccess(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun verifySmsCode(phoneNumber: String, verifyCode: String) {
        if(validatePhoneNumber(phoneNumber)) {
            if (verifyCode.isNullOrEmpty()) {
                (mView as IVerifySmsCode).validateErrorUI(R.string.please_input_sms_code)
            }else {
                mView?.showLoadingDialog()
                addSubscription(mUserRepository.verifySmsCode(phoneNumber,2, verifyCode), Consumer<Boolean> { success ->
                    (mView as IVerifySmsCode).verifySmsCodeResult(success)
                    mView?.hideLoadingDialog()
                })
            }
        }
    }

    fun uploadBusinessCard(token: String, content: String, filePath: String) {
        if (validateBusinessCardUploadParams(filePath)){
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.uploadBusinessCard(token, content , filePath), Consumer<User> { user ->
                (mView as IUploadCard).uploadSuccess(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun uploadContactCard(token: String, cardFrontFilePath: String, cardBackEndFilePath: String){
       if(validateContactCardUploadParams(cardFrontFilePath, cardBackEndFilePath)) {
          mView?.showLoadingDialog()
           addSubscription(mUserRepository.uploadContactCard(token, cardFrontFilePath, cardBackEndFilePath), Consumer<User> { user ->
               (mView as IUploadCard).uploadSuccess(user)
               mView?.hideLoadingDialog()
           })

       }
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
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

    private fun validateBusinessLincenceCardUploadParams(contactName: String, companyName: String, filePath: String): Boolean {
        var ok = true
        if(contactName.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_input_contact_name)
            ok = false
        }
        if(companyName.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_input_company_name)
            ok = false
        }
        if(filePath.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        return ok
    }

    private fun validateBusinessCardUploadParams(filePath: String): Boolean {
        var ok = true
        if(filePath.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        return ok
    }

    private fun validateContactCardUploadParams(filePathFront: String, filePathBack: String): Boolean {

        var ok = true
        if(filePathFront.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        if(filePathBack.isNullOrEmpty()){
            (mView as IBaseView).validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        return ok
    }

    private fun validatePassword(password: String): Boolean {
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
}