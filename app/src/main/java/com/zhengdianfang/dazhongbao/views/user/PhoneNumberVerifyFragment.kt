package com.zhengdianfang.dazhongbao.views.user


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.BuildConfig
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.presenters.LoginPresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.login.FindPasswordFragment
import com.zhengdianfang.dazhongbao.views.login.ISendSmsCode
import com.zhengdianfang.dazhongbao.views.login.IVerifySmsCode
import com.zhengdianfang.dazhongbao.views.login.ModifyPasswordFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class PhoneNumberVerifyFragment: BaseFragment(), IVerifySmsCode , ISendSmsCode{

    private val mUserPresenter by lazy { UserPresenter() }
    private val mLoginPresenter by lazy { LoginPresenter() }
    private val phoneEditText by lazy { view?.findViewById<EditText>(R.id.registerPhoneEditText)!! }
    private val smsCodeEditText by lazy { view?.findViewById<EditText>(R.id.smsCodeEditText)!! }
    private val smsCodeButton by lazy { view?.findViewById<Button>(R.id.getSmsCodeButton)!! }
    private val timerObservable by lazy {
        Observable.interval(1, TimeUnit.SECONDS)
                  .map { i -> 60 - i }.observeOn(AndroidSchedulers.mainThread())
    }
    private var timerSub: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_phone_number_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLoginPresenter.attachView(this)
        mUserPresenter.attachView(this)

        view?.findViewById<Button>(R.id.getSmsCodeButton)?.setOnClickListener {
            mLoginPresenter.requestSmsVerifyCode(phoneEditText.text.toString(), 2)
        }
        view?.findViewById<Button>(R.id.submitButton)?.setOnClickListener {
            mUserPresenter.verifySmsCode(phoneEditText.text.toString(), smsCodeEditText.text.toString())
        }
    }

    override fun onDestroyView() { super.onDestroyView()
        mLoginPresenter.detachView()
        mUserPresenter.detachView()
        timerSub?.dispose()
    }

    override fun onBackPressed(): Boolean {
        toolbarBackButtonClick()
        return true
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun receiverSmsCode(code: String) {
        timerSub = timerObservable.doOnNext { smsCodeButton.isEnabled = false }.subscribe { i ->
            smsCodeButton.text =  getString(R.string.button_diable_timer_alert, i)
            if (i == 0L) {
                timerSub?.dispose()
                smsCodeButton.setText(R.string.get_verify_sms_code)
                smsCodeButton.isEnabled = true
            }
        }
        if (BuildConfig.DEBUG) {
           smsCodeEditText.setText(code)
        }
        toast(R.string.toast_sms_code_send_successful)
    }

    override fun verifySmsCodeResult(success: Boolean) {
        if (success){
            if (arguments?.getString("from") == "setting") {
                replaceFragment(android.R.id.content, ModifyPasswordFragment(), "setting")
            }else{
                val bundle = Bundle()
                bundle.putString("phoneNumber", phoneEditText.text.toString())
                bundle.putString("verifyCode", smsCodeEditText.text.toString())
                val fragment = FindPasswordFragment()
                fragment.arguments = bundle
                replaceFragment(android.R.id.content, fragment, "login")
            }
        }
    }
}
