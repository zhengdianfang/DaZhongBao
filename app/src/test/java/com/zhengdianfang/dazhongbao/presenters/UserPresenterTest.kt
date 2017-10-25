package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.RxImmediateSchedulerRule
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import com.zhengdianfang.dazhongbao.views.login.IFindPasswordView
import com.zhengdianfang.dazhongbao.views.login.ISetPasswordView
import com.zhengdianfang.dazhongbao.views.login.IUploadCard
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dfgzheng on 25/10/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class UserPresenterTest {
    companion object {
        @ClassRule
        @JvmField val schedulers = RxImmediateSchedulerRule()
    }

    @Mock
    private val mSetPasswordView:  ISetPasswordView? = null

    @Mock
    private val mFindPasswordView: IFindPasswordView? = null

    @Mock
    private val mUploadCard: IUploadCard? = null

    private val mUserPresenter by lazy {
        val presenter = UserPresenter()
        presenter.MOCK = true
        presenter
    }
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun should_set_password_success_When_user_need_it() {
        mUserPresenter.attachView(mSetPasswordView)

        mUserPresenter.setPassword("", mockUser.token!!)
        verify(mSetPasswordView, atLeastOnce())?.validateErrorUI(R.string.please_input_password)

        mUserPresenter.setPassword("12", mockUser.token!!)
        verify(mSetPasswordView, atLeastOnce())?.validateErrorUI(R.string.please_input_legal_password)

        mUserPresenter.setPassword("111111", mockUser.token!!)
        verify(mSetPasswordView)?.showLoadingDialog()
        verify(mSetPasswordView, atLeastOnce())?.setPasswordSuccess(mockUser)
        verify(mSetPasswordView)?.hideLoadingDialog()
    }

    @Test
    fun should_find_password_when_user_need_it() {
        mUserPresenter.attachView(mFindPasswordView)

        mUserPresenter.findPassword("", "", "")
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_password)

        mUserPresenter.findPassword("12", "", "")
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_legal_password)

        mUserPresenter.findPassword("121345", "", "")
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_sms_code)

        mUserPresenter.findPassword("121345", "121234", "")
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_phonenumber)

        mUserPresenter.findPassword("121345", "121234", "12345566731")
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_legal_phonenumber)

        mUserPresenter.findPassword("121345", "121234", "18511177781")
        verify(mFindPasswordView)?.showLoadingDialog()
        verify(mFindPasswordView)?.findPasswordSuccess("success")
        verify(mFindPasswordView)?.hideLoadingDialog()
    }

    @Test
    fun should_modify_password_When_user_need_it() {
        mUserPresenter.attachView(mFindPasswordView)

        mUserPresenter.modifyPassword("", "", mockUser.token!!)
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_password)

        mUserPresenter.modifyPassword("12", "", mockUser.token!!)
        verify(mFindPasswordView)?.validateErrorUI(R.string.please_input_legal_password)

        mUserPresenter.modifyPassword("123456", "1111111", mockUser.token!!)
        verify(mFindPasswordView)?.validateErrorUI(R.string.toast_input_confirm_password_not_equal)

        mUserPresenter.modifyPassword("123456", "123456", mockUser.token!!)
        verify(mFindPasswordView)?.showLoadingDialog()
        verify(mFindPasswordView)?.findPasswordSuccess("success")
        verify(mFindPasswordView)?.hideLoadingDialog()

    }

    @Test
    fun should_upload_success_When_user_upload_business_card() {
        mUserPresenter.attachView(mUploadCard)

        mUserPresenter.uploadBusinessCard(mockUser.token!!, "", "")
        verify(mUploadCard)?.validateErrorUI(R.string.please_select_business_card_photo)

        mUserPresenter.uploadBusinessCard(mockUser.token!!, "", "/Users/test/1123.png")
        verify(mUploadCard)?.showLoadingDialog()
        verify(mUploadCard)?.uploadSuccess(mockUser)
        verify(mUploadCard)?.hideLoadingDialog()
    }

    @Test
    fun should_upload_success_When_user_upload_business_lisence_card() {
        mUserPresenter.attachView(mUploadCard)

        mUserPresenter.uploadBusinessLicenceCard(mockUser.token!!, "", "", "")
        verify(mUploadCard)?.validateErrorUI(R.string.please_input_contact_name)

        mUserPresenter.uploadBusinessLicenceCard(mockUser.token!!, "1111", "", "")
        verify(mUploadCard)?.validateErrorUI(R.string.please_input_company_name)

        mUserPresenter.uploadBusinessLicenceCard(mockUser.token!!, "1111", "222222", "")
        verify(mUploadCard)?.validateErrorUI(R.string.please_select_business_card_photo)

        mUserPresenter.uploadBusinessLicenceCard(mockUser.token!!, "1111", "22222", "/Users/test/1123.png")
        verify(mUploadCard)?.showLoadingDialog()
        verify(mUploadCard)?.uploadSuccess(mockUser)
        verify(mUploadCard)?.hideLoadingDialog()
    }

    @Test
    fun should_upload_success_When_user_upload_contact_card() {
        mUserPresenter.attachView(mUploadCard)

        mUserPresenter.uploadContactCard(mockUser.token!!, "", "")
        verify(mUploadCard)?.validateErrorUI(R.string.please_select_contact_card_front_photo)

        mUserPresenter.uploadContactCard(mockUser.token!!, "/Users/test/1123.png", "")
        verify(mUploadCard)?.validateErrorUI(R.string.please_select_contact_card_back_photo)

        mUserPresenter.uploadContactCard(mockUser.token!!,"/Users/test/1123.png", "/Users/test/1123.png")
        verify(mUploadCard)?.showLoadingDialog()
        verify(mUploadCard)?.uploadSuccess(mockUser)
        verify(mUploadCard)?.hideLoadingDialog()
    }
}