package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.login.UserRepository
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.validates.PasswordValidate
import com.zhengdianfang.dazhongbao.presenters.validates.PhoneNumberValidate
import com.zhengdianfang.dazhongbao.presenters.validates.VerifySmsCodeValidate
import com.zhengdianfang.dazhongbao.views.basic.IView
import com.zhengdianfang.dazhongbao.views.login.IFindPasswordView
import com.zhengdianfang.dazhongbao.views.login.ISetPasswordView
import com.zhengdianfang.dazhongbao.views.login.IUploadCard
import com.zhengdianfang.dazhongbao.views.login.IVerifySmsCode
import com.zhengdianfang.dazhongbao.views.user.IModifyPhoneNumberView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 08/08/2017.
 */
class UserPresenter: BasePresenter() {

    private val mUserRepository by lazy { UserRepository(Constants.MOCK) }
    private val passwordValidate by lazy { PasswordValidate(mView) }
    private val phoneNumberValidate by lazy { PhoneNumberValidate(mView) }
    private val verifySmsCodeValidate by lazy { VerifySmsCodeValidate(mView) }

    fun setPassword(password: String, token: String) {
        if (passwordValidate.validate(password)){
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.setPassword(password, token), Consumer<User> { user->
                (mView as ISetPasswordView).setPasswordSuccess(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun findPassword(password: String,  verifyCode: String,  phoneNumber: String) {
        if (passwordValidate.validate(password)){
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.findPassword(password, verifyCode, phoneNumber), Consumer<String> { msg ->
                (mView as IFindPasswordView).findPasswordSuccess(msg)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun modifyPassword(password: String, confirmPassword: String, token: String) {
        if (passwordValidate.validate(password)
                && isEqualConfirmPassword(password, confirmPassword)){
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.modifyPassword(password,  token), Consumer<String> { msg->
                (mView as IFindPasswordView).findPasswordSuccess(msg)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun isEqualConfirmPassword(password: String, confirmPassword: String): Boolean {
        var equal = true
        if (password != confirmPassword) {
            equal = false
            mView?.validateErrorUI(R.string.toast_input_confirm_password_not_equal)
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
        if(phoneNumberValidate.validate(phoneNumber) && verifySmsCodeValidate.validate(verifyCode)) {
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.verifySmsCode(phoneNumber,2, verifyCode), Consumer<Boolean> { success ->
                (mView as IVerifySmsCode).verifySmsCodeResult(success)
                mView?.hideLoadingDialog()
            })
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

    fun modifyPhoneNumber(token: String, phoneNumber: String, verifyCode: String){
        if(phoneNumberValidate.validate(phoneNumber)) {
            if (verifyCode.isNullOrEmpty()) {
                (mView as IModifyPhoneNumberView).validateErrorUI(R.string.please_input_sms_code)
            }else {
                mView?.showLoadingDialog()
                addSubscription(mUserRepository.modifyPhoneNumber(token, phoneNumber, verifyCode), Consumer<User> { user->
                    (mView as IModifyPhoneNumberView).modifySuccess(user)
                    mView?.hideLoadingDialog()
                })
            }
        }

    }

    fun fetchUserPushedProducts(token: String){
       if (phoneNumberValidate.checkLogin()) {
           addSubscription(mUserRepository.fetchUserPushedProduct(token), Consumer { list ->
               (mView as IUserProductListView).receiveUserProductList(list)
           })
       }
    }

    fun fetchUserAttentionProducts(token: String){
        if (phoneNumberValidate.checkLogin()) {
            addSubscription(mUserRepository.fetchUserAttentionProducts(token), Consumer { list ->
                (mView as IUserAttentionListView).receiveUserAttentionList(list)
            })
        }
    }

    fun fetchUserAuctionProducts(token: String){
        if (phoneNumberValidate.checkLogin()) {
            addSubscription(mUserRepository.fetchUserAuctionProducts(token), Consumer { list ->
                (mView as IUserAuctionListView).receiveUserAuctionList(list)
            })
        }
    }

    private fun validateBusinessLincenceCardUploadParams(contactName: String, companyName: String, filePath: String): Boolean {
        var ok = true
        if(contactName.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_input_contact_name)
            ok = false
        }
        if(companyName.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_input_company_name)
            ok = false
        }
        if(filePath.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        return ok
    }

    private fun validateBusinessCardUploadParams(filePath: String): Boolean {
        var ok = true
        if(filePath.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        return ok
    }

    private fun validateContactCardUploadParams(filePathFront: String, filePathBack: String): Boolean {

        var ok = true
        if(filePathFront.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        if(filePathBack.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_select_business_card_photo)
            ok = false
        }
        return ok
    }

    interface IUserProductListView: IView {
       fun receiveUserProductList(list: MutableList<Product>)
    }

    interface IUserAttentionListView: IView {
        fun receiveUserAttentionList(list: MutableList<Product>)
    }

    interface IUserAuctionListView: IView {
        fun receiveUserAuctionList(list: MutableList<Product>)
    }
}