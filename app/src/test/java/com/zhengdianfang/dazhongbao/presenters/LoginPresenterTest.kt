package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.RxImmediateSchedulerRule
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import com.zhengdianfang.dazhongbao.views.login.ILoginView
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
        val persenter = LoginPresenter()
        persenter.attachView(mMockLoginView)
        persenter
    }

    @Mock
    private val mMockLoginView:  ILoginView? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun validate_illegal_phonenumber_When_user_login() {
        //phonenumber is empty
        mLoginPresenter.validatePhoneNumberAndPassword("", "" )
        verify(mMockLoginView, atLeastOnce())?.validateErrorUI(R.string.please_input_phonenumber)
        //phonenumber format illegal
        mLoginPresenter.validatePhoneNumberAndPassword("123", "" )
        verify(mMockLoginView,  atLeastOnce())?.validateErrorUI(R.string.please_input_legal_phonenumber)

        mLoginPresenter.validatePhoneNumberAndPassword("14587689876", "" )
        verify(mMockLoginView,   atLeastOnce())?.validateErrorUI(R.string.please_input_legal_phonenumber)
    }

    @Test
    fun should_no_phonenumber_errormsg_When_user_login() {
        mLoginPresenter.validatePhoneNumberAndPassword("18511177917", "" )
        verify(mMockLoginView, never())?.validateErrorUI(R.string.please_input_legal_phonenumber)
    }

    @Test
    fun should_validate_illegal_password_When_user_login() {
        mLoginPresenter.validatePhoneNumberAndPassword("18511177917", "" )
        verify(mMockLoginView, atLeastOnce())?.validateErrorUI(R.string.please_input_password)
        mLoginPresenter.validatePhoneNumberAndPassword("18511177917", "1" )
        verify(mMockLoginView, atLeastOnce())?.validateErrorUI(R.string.please_input_legal_password)
    }

    @Test
    fun should_no_password_erromsg_When_user_login() {
        mLoginPresenter.validatePhoneNumberAndPassword("18511177917", "123456" )
        verify(mMockLoginView,   never())?.validateErrorUI(R.string.please_input_legal_phonenumber)

    }

    @Test
    fun should_legal_user_object_When_user_login() {
        mLoginPresenter.loginByPhoneNumber("18511177916", "123456")
        verify(mMockLoginView, times(1))?.showLoadingDialog()
        verify(mMockLoginView, times(1))?.userResponseProcessor(mockUser)
        verify(mMockLoginView, times(1))?.hideLoadingDialog()

    }

}