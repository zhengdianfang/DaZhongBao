package com.zhengdianfang.dazhongbao.views.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.LoginPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.home.MainActivity
import com.zhengdianfang.dazhongbao.views.user.PhoneNumberVerifyFragment

/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : BaseFragment(),ILoginView{

    private val mLoginPresenter by lazy { LoginPresenter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLoginPresenter.attachView(this)
        view?.findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            val phoneNumber = view?.findViewById<EditText>(R.id.phoneEditText)!!.text.toString()
            val password = view?.findViewById<EditText>(R.id.passwordEditText)!!.text.toString()

            RxPermissions(getParentActivity()).request(Manifest.permission.READ_PHONE_STATE)?.subscribe { granted ->
                if (granted){
                    mLoginPresenter.loginByPhoneNumber(phoneNumber, password, DeviceUtils.getDeviceId(activity.applicationContext)
                            , DeviceUtils.getAppVersionName(this.context.applicationContext))
                }else {
                    mLoginPresenter.loginByPhoneNumber(phoneNumber, password, "", DeviceUtils.getAppVersionName(this.context.applicationContext))
                }
            }
        }

        view?.findViewById<Button>(R.id.organizationButton)?.setOnClickListener {
            startFragment(android.R.id.content, PhoneRegisterFragment(), "login")
        }

        view?.findViewById<TextView>(R.id.findPasswordTextView)!!.setOnClickListener {
            startFragment(android.R.id.content, PhoneNumberVerifyFragment(), "login")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLoginPresenter.detachView()
    }

    override fun userResponseProcessor(user: User?) {
        if (null != user){
            toast(R.string.toast_login_success)
            CApplication.INSTANCE.loginUser = user
            startActivity(Intent(getParentActivity(), MainActivity::class.java))
        }
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().finish()
        return true
    }
}
