package com.zhengdianfang.dazhongbao.views.user


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.LoginPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.login.ILoginView


/**
 * A simple [Fragment] subclass.
 */
class VerfiyPasswordFragment : BaseFragment(), ILoginView {

    private val passwordEditText by lazy { view?.findViewById<EditText>(R.id.passwordEditText)!! }
    private val mLoginPresenter by lazy { LoginPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_verfiy_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLoginPresenter.attachView(this)
        val loginUser = CApplication.INSTANCE.loginUser
        view?.findViewById<Button>(R.id.nextStepButton)!!.setOnClickListener {
           if(loginUser != null){

               mLoginPresenter.loginByPhoneNumber(loginUser.phonenumber ?: "", passwordEditText.text.toString(), DeviceUtils.getDeviceId(activity.applicationContext)
                       , DeviceUtils.getAppVersionName(this.context.applicationContext))
           }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLoginPresenter.detachView()
    }

    override fun userResponseProcessor(user: User?) {
        if (null != user) {
            replaceFragment(android.R.id.content, ModifyPhoneNumberFragment(),"setting")
        }
    }

}
