package com.zhengdianfang.dazhongbao.views.login

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.PresenterFactory
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment

/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : BaseFragment<LoginActivity>(), ILoginView{


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PresenterFactory.mLoginPresenter.attachView(this)
        view?.findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            val phoneNumber = view?.findViewById<EditText>(R.id.phoneEditText)!!.text.toString()
            val password = view?.findViewById<EditText>(R.id.passwordEditText)!!.text.toString()

            RxPermissions(getParentActivity()).request(Manifest.permission.READ_PHONE_STATE)?.subscribe { granted ->
                if (granted){
                    PresenterFactory.mLoginPresenter.loginByPhoneNumber(phoneNumber, password, DeviceUtils.getDeviceId(activity.applicationContext))
                }else {
                    PresenterFactory.mLoginPresenter.loginByPhoneNumber(phoneNumber, password, "")
                }
            }
        }

        view?.findViewById<Button>(R.id.organizationButton)?.setOnClickListener {
           getParentActivity().supportFragmentManager.beginTransaction()
                   .add(android.R.id.content, PhoneRegisterFragment())
                   .commitNowAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterFactory.mLoginPresenter.detachView()
    }

    override fun userResponseProcessor(user: User?) {
        if (null != user){
            toast(R.string.toast_login_success)
        }
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }
}
