package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.login.UserCacheRepository
import com.zhengdianfang.dazhongbao.models.login.UserCount
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
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 08/08/2017.
 */
class UserPresenter: BasePresenter() {

    private val mUserRepository by lazy { UserRepository(MOCK) }
    private val mUserCacheRepository by lazy { UserCacheRepository(CApplication.INSTANCE.memoryCache, CApplication.INSTANCE.diskCahce) }
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
        if (passwordValidate.validate(password) && verifySmsCodeValidate.validate(verifyCode) && phoneNumberValidate.validate(phoneNumber)){
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
            addSubscription(mUserRepository.uploadBusinessLicenceCard(token, contactName, companyName, File(filePath)), Consumer<User> { user->
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
            addSubscription(mUserRepository.uploadBusinessCard(token, content , File(filePath)), Consumer<User> { user ->
                (mView as IUploadCard).uploadSuccess(user)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun uploadContactCard(token: String, cardFrontFilePath: String, cardBackEndFilePath: String){
       if(validateContactCardUploadParams(cardFrontFilePath, cardBackEndFilePath)) {
          mView?.showLoadingDialog()
           addSubscription(mUserRepository.uploadContactCard(token, File(cardFrontFilePath), File(cardBackEndFilePath)), Consumer<User> { user ->
               (mView as IUploadCard).uploadSuccess(user)
               mView?.hideLoadingDialog()
           })

       }
    }

    fun modifyPhoneNumber(token: String, phoneNumber: String, verifyCode: String){
        if(phoneNumberValidate.validate(phoneNumber) && verifySmsCodeValidate.validate(verifyCode)) {
            mView?.showLoadingDialog()
            addSubscription(mUserRepository.modifyPhoneNumber(token, phoneNumber, verifyCode), Consumer<User> { user->
                (mView as IModifyPhoneNumberView).modifySuccess(user)
                mView?.hideLoadingDialog()
            })
        }

    }

    fun fetchUserPushedProducts(token: String){
       if (phoneNumberValidate.checkLogin()) {
           val observable = Observable.concat(
                   mUserCacheRepository.loadUserProductsCache(),
                   mUserRepository.fetchUserPushedProduct(token).delay(300, TimeUnit.MILLISECONDS)
           ).doOnNext { result ->
               if (!result.isCache){
                   mUserCacheRepository.saveUserProductsCache(result.data)
               }
           }
           addSubscription(observable, Consumer { result ->
               (mView as IUserProductListView).receiveUserProductList(result.data, result.isCache)
           })
       }
    }

    fun fetchUserAttentionProducts(token: String){
        if (phoneNumberValidate.checkLogin()) {
            val observable = Observable.concat(
                    mUserCacheRepository.loadUserAttentionProductsCache(),
                    mUserRepository.fetchUserAttentionProducts(token).delay(300, TimeUnit.MILLISECONDS)
            ).doOnNext { result ->
                        if (!result.isCache){
                           mUserCacheRepository.saveUserAttentionProductsCache(result.data)
                        }
                    }
            addSubscription(observable, Consumer { result ->
                (mView as IUserAttentionListView).receiveUserAttentionList(result.data, result.isCache)
            })
        }
    }

    fun fetchUserAuctionProducts(token: String){
        if (phoneNumberValidate.checkLogin()) {
            val observable = Observable.concat(
                    mUserCacheRepository.loadUserAuctionProductsCache(),
                    mUserRepository.fetchUserAuctionProducts(token).delay(300, TimeUnit.MILLISECONDS)
            ).doOnNext { result ->
                if (!result.isCache){
                    mUserCacheRepository.saveUserAuctionProductsCache(result.data)
                }
            }
            addSubscription(observable, Consumer { result ->
                (mView as IUserAuctionListView).receiveUserAuctionList(result.data, result.isCache)
            })
        }
    }

    fun fetchDepositProducts(token: String){
        if (phoneNumberValidate.checkLogin()) {
            addSubscription(mUserRepository.fetchDepositProducts(token), Consumer { list ->
                (mView as IUserDepositListView).receiveUserDepositList(list)
            })
        }
    }

    fun fetchUserInfo(token: String){
        if (verifySmsCodeValidate.checkLogin()){
            addSubscription(mUserRepository.fetchUserInfo(token), Consumer {(user, userCount)->
                (mView as IUserInfo).receiverUserInfo(user, userCount)
            })
        }
    }

    fun uploadUserAvatar(token: String, avatarFile: File){
        mView?.showLoadingDialog()
        addSubscription(mUserRepository.uploadUserAvatar(token, avatarFile), Consumer<User> { user ->
            (mView as IUploadCard).uploadSuccess(user)
            mView?.hideLoadingDialog()
        })
    }

    fun updateUMengToken(token: String, umengId: String) {
        addSubscription(mUserRepository.updateUmengId(token, umengId), Consumer {}, Consumer {})
    }

    private fun validateBusinessLincenceCardUploadParams(contactName: String, companyName: String, filePath: String): Boolean {
        var ok = true
        if(contactName.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_input_contact_name)
            ok = false
        }else if(companyName.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_input_company_name)
            ok = false
        }else if(filePath.isNullOrEmpty()){
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
            mView?.validateErrorUI(R.string.please_select_contact_card_front_photo)
            ok = false
        }else if(filePathBack.isNullOrEmpty()){
            mView?.validateErrorUI(R.string.please_select_contact_card_back_photo)
            ok = false
        }
        return ok
    }

    interface IUserProductListView: IView {
       fun receiveUserProductList(list: MutableList<Product>, isCahce: Boolean)
    }

    interface IUserAttentionListView: IView {
        fun receiveUserAttentionList(list: MutableList<Product>, isCahce: Boolean)
    }

    interface IUserAuctionListView: IView {
        fun receiveUserAuctionList(list: MutableList<Product>, isCahce: Boolean)
    }

    interface IUserDepositListView: IView {
        fun receiveUserDepositList(list: MutableList<Product>)
    }

    interface IUserInfo :IView{
        fun receiverUserInfo(user: User, userCount: UserCount)
    }

}