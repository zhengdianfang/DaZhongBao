package com.zhengdianfang.dazhongbao.views.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.PresenterFactory
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class PhoneRegisterFragment : BaseFragment<LoginActivity>(), IRegisterView {

    private val phoneEditText by lazy { view?.findViewById<EditText>(R.id.registerPhoneEditText)!! }
    private val smsCodeEditText by lazy { view?.findViewById<EditText>(R.id.smsCodeEditText)!! }
    private val recommendEditText by lazy { view?.findViewById<EditText>(R.id.recommendEditText)!! }
    private val smsCodeButton by lazy { view?.findViewById<Button>(R.id.getSmsCodeButton)!! }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_phone_registed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PresenterFactory.mLoginPresenter.attachView(this)
        view?.findViewById<Button>(R.id.getSmsCodeButton)?.setOnClickListener {
            PresenterFactory.mLoginPresenter.requestSmsVerifyCode(phoneEditText.text.toString())
        }
        view?.findViewById<Button>(R.id.submitButton)?.setOnClickListener {
            PresenterFactory.mLoginPresenter.requestRegister(phoneEditText.text.toString(), smsCodeEditText.text.toString(), recommendEditText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterFactory.mLoginPresenter.detachView()
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun receiverSmsCode(code: String) {
        smsCodeButton.text =  getString(R.string.button_diable_timer_alert, 60)
        toast(R.string.toast_sms_code_send_successful)
    }

    override fun receiverUser(user: User) {
        CApplication.INSTANCE.loginUser = user
        toast(R.string.toast_register_successful)
        getParentActivity().supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, ModifyPasswordFragment())
                .commitAllowingStateLoss()
    }

}
