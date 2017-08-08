package com.zhengdianfang.dazhongbao.presenters

import android.os.Parcelable
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.RxImmediateSchedulerRule
import com.zhengdianfang.dazhongbao.models.mock.mockSmsCode
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import com.zhengdianfang.dazhongbao.views.login.ILoginView
import com.zhengdianfang.dazhongbao.views.login.IRegisterView
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by dfgzheng on 30/07/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest {

    companion object {
        @ClassRule @JvmField val schedulers = RxImmediateSchedulerRule()
    }

    private val mLoginPresenter by lazy {
        val presenter = LoginPresenter()
        presenter
    }

    @Mock
    private val mMockLoginView:  ILoginView? = null

    @Mock
    private val p: Parcelable? = null

    @Mock
    private val mMockRegisterView:  IRegisterView? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun validate_illegal_phoneNumber_When_user_login() {
        mLoginPresenter.attachView(mMockLoginView)
        //phonenumber is empty
        mLoginPresenter.validatePhoneNumber("")
        verify(mMockLoginView, atLeastOnce())?.validateErrorUI(R.string.please_input_phonenumber)
        //phoneNumber format illegal
        mLoginPresenter.validatePhoneNumber("123")
        verify(mMockLoginView,  atLeastOnce())?.validateErrorUI(R.string.please_input_legal_phonenumber)

        mLoginPresenter.validatePhoneNumber("14587689876")
        verify(mMockLoginView,   atLeastOnce())?.validateErrorUI(R.string.please_input_legal_phonenumber)
    }

    @Test
    fun should_legal_phoneNumber_errormsg_When_user_login() {
        mLoginPresenter.attachView(mMockLoginView)
        mLoginPresenter.validatePhoneNumber("18511177917")
        verify(mMockLoginView, never())?.validateErrorUI(R.string.please_input_legal_phonenumber)
    }

    @Test
    fun should_validate_illegal_password_When_user_login() {
        mLoginPresenter.attachView(mMockLoginView)
        mLoginPresenter.validatePassword("" )
        verify(mMockLoginView, atLeastOnce())?.validateErrorUI(R.string.please_input_password)
        mLoginPresenter.validatePassword("1" )
        verify(mMockLoginView, atLeastOnce())?.validateErrorUI(R.string.please_input_legal_password)
    }

    @Test
    fun should_legal_password_erromsg_When_user_login() {
        mLoginPresenter.attachView(mMockLoginView)
        mLoginPresenter.validatePassword("123456")
        verify(mMockLoginView,   never())?.validateErrorUI(R.string.please_input_legal_password)

    }

    @Test
    fun should_legal_user_object_When_user_login() {
        mLoginPresenter.attachView(mMockLoginView)
        mLoginPresenter.loginByPhoneNumber("18511177916", "123456", "", "")
        verify(mMockLoginView, times(1))?.showLoadingDialog()
        verify(mMockLoginView, times(1))?.userResponseProcessor(mockUser)
        verify(mMockLoginView, times(1))?.hideLoadingDialog()

    }

    @Test
    fun should_sms_code_When_user_register() {
        mLoginPresenter.attachView(mMockRegisterView)
        mLoginPresenter.requestSmsVerifyCode("18511177916")
        verify(mMockRegisterView)?.showLoadingDialog()
        verify(mMockRegisterView)?.receiverSmsCode(mockSmsCode)
        verify(mMockRegisterView)?.hideLoadingDialog()
    }

    @Test
    fun should_error_msg_phone_illegal_When_user_register() {
        mLoginPresenter.attachView(mMockRegisterView)
        mLoginPresenter.requestSmsVerifyCode("18511177")
        verify(mMockRegisterView, atLeastOnce())?.validateErrorUI(R.string.please_input_legal_phonenumber)
    }

    @Test
    fun should_validate_phoneNumber_illegal_When_user_click_register_button() {
        mLoginPresenter.attachView(mMockRegisterView)
        mLoginPresenter.requestRegister("", mockSmsCode, "")
        verify(mMockRegisterView, atLeastOnce())?.validateErrorUI(R.string.please_input_phonenumber)
        mLoginPresenter.requestRegister("185", mockSmsCode, "")
        verify(mMockRegisterView, atLeastOnce())?.validateErrorUI(R.string.please_input_legal_phonenumber)
    }

    @Test
    fun should_validate_sms_code_empty_When_user_click_register_button() {
        mLoginPresenter.attachView(mMockRegisterView)
        mLoginPresenter.requestRegister("18511177914", "", "")
        verify(mMockRegisterView, atLeastOnce())?.validateErrorUI(R.string.please_input_sms_code)
    }

    @Test
    fun should_call_register_api_When_user_register() {
        mLoginPresenter.attachView(mMockRegisterView)
        mLoginPresenter.requestRegister("18511177916", mockSmsCode, "")
        verify(mMockRegisterView)?.showLoadingDialog()
        verify(mMockRegisterView)?.receiverUser(mockUser)
        verify(mMockRegisterView)?.hideLoadingDialog()
    }

}