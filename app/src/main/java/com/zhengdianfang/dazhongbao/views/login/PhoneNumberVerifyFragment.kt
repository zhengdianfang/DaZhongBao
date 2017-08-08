package com.zhengdianfang.dazhongbao.views.login


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.BuildConfig
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.presenters.PresenterFactory
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class PhoneNumberVerifyFragment: BaseFragment(), IVerifySmsCode{

    private val phoneEditText by lazy { view?.findViewById<EditText>(R.id.registerPhoneEditText)!! }
    private val smsCodeEditText by lazy { view?.findViewById<EditText>(R.id.smsCodeEditText)!! }
    private val smsCodeButton by lazy { view?.findViewById<Button>(R.id.getSmsCodeButton)!! }
    private val toolbar by lazy { view?.findViewById<Toolbar>(R.id.toolbar)!! }
    private val timerObservable by lazy {
        Observable.interval(1, TimeUnit.SECONDS)
                  .map { i -> 60 - i }.observeOn(AndroidSchedulers.mainThread())
    }
    private var timerSub: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_phone_registed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PresenterFactory.mLoginPresenter.attachView(this)
        PresenterFactory.mUserPresenter.attachView(this)

        toolbar.backListener  = {
           getParentActivity().supportFragmentManager.popBackStack()
        }
        view?.findViewById<Button>(R.id.getSmsCodeButton)?.setOnClickListener {
            PresenterFactory.mLoginPresenter.requestSmsVerifyCode(phoneEditText.text.toString())
        }
        view?.findViewById<Button>(R.id.submitButton)?.setOnClickListener {
            PresenterFactory.mUserPresenter.verifySmsCode(phoneEditText.text.toString(), smsCodeEditText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterFactory.mLoginPresenter.detachView()
        PresenterFactory.mUserPresenter.detachView()
        timerSub?.dispose()
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
            val bundle = Bundle()
            bundle.putInt("ac", 2)
            val fragment = SetPasswordFragment()
            fragment.arguments = bundle
            replaceFragment(android.R.id.content, fragment, "login")
        }
    }
}
