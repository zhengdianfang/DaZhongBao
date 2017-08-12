package com.zhengdianfang.dazhongbao.views.user


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.BuildConfig
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.PresenterFactory
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.login.ISendSmsCode
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class ModifyPhoneNumberFragment: BaseFragment(), IModifyPhoneNumberView , ISendSmsCode{

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
        return inflater!!.inflate(R.layout.fragment_modify_phone_number, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PresenterFactory.mLoginPresenter.attachView(this)
        PresenterFactory.mUserPresenter.attachView(this)

        view?.findViewById<Button>(R.id.getSmsCodeButton)?.setOnClickListener {
            PresenterFactory.mLoginPresenter.requestSmsVerifyCode(phoneEditText.text.toString(), 3)
        }
        view?.findViewById<Button>(R.id.submitButton)?.setOnClickListener {
            val loginUser = CApplication.INSTANCE.loginUser
            if (null != loginUser){
                PresenterFactory.mUserPresenter.modifyPhoneNumber(loginUser.token ?: "", phoneEditText.text.toString(), smsCodeEditText.text.toString())
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView()
        PresenterFactory.mLoginPresenter.detachView()
        PresenterFactory.mUserPresenter.detachView()
        timerSub?.dispose()
    }

    override fun onBackPressed(): Boolean {
        toolbarBackButtonClick()
        return true
    }

    override fun toolbarBackButtonClick() {
        getParentActivity().supportFragmentManager.popBackStack("setting", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

    override fun modifySuccess(user: User) {
        toast(R.string.modify_password_success)
        CApplication.INSTANCE.loginUser = user
        toolbarBackButtonClick()
    }

}// Required empty public constructor
