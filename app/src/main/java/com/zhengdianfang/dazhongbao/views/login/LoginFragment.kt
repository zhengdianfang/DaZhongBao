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
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.helpers.WechatUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.LoginPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.home.MainActivity
import com.zhengdianfang.dazhongbao.views.user.PhoneNumberVerifyFragment
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : BaseFragment(),ILoginView{

    private val mLoginPresenter by lazy { LoginPresenter() }
    private var weChatDisposable: Disposable? = null
    private val weChatUtils by lazy { WechatUtils(context.applicationContext) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLoginPresenter.attachView(this)
        weChatDisposable = RxBus.instance.register(arrayOf(Action.WEIXIN_OUTH_RESULT_ACTION, Action.WEIXIN_OUTH_RESULT_FAIL_ACTION), Consumer { action->
            if (action.type == Action.WEIXIN_OUTH_RESULT_ACTION){
                weChatUtils.getWeiXinAccessToken(action.data as String, {accessToken, openId ->
                    Logger.d("wechat login success : accessToken : $accessToken , openId : $openId")
                    mLoginPresenter.loginByThridParty(openId)
                }, {
                    hideLoadingDialog()
                    toast(R.string.wechat_login_fail)
                })
            }else if (action.type == Action.WEIXIN_OUTH_RESULT_FAIL_ACTION){
                hideLoadingDialog()
                toast(R.string.wechat_login_fail)
            }
        })
        initViews()
    }

    private fun initViews() {
        view?.findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            val phoneNumber = view?.findViewById<EditText>(R.id.phoneEditText)!!.text.toString()
            val password = view?.findViewById<EditText>(R.id.passwordEditText)!!.text.toString()

            RxPermissions(getParentActivity()).request(Manifest.permission.READ_PHONE_STATE)?.subscribe { granted ->
                if (granted) {
                    mLoginPresenter.loginByPhoneNumber(phoneNumber, password, DeviceUtils.getDeviceId(activity.applicationContext)
                            , DeviceUtils.getAppVersionName(this.context.applicationContext))
                } else {
                    mLoginPresenter.loginByPhoneNumber(phoneNumber, password, "", DeviceUtils.getAppVersionName(this.context.applicationContext))
                }
            }
        }

        view?.findViewById<Button>(R.id.weixinButton)?.setOnClickListener {
            showLoadingDialog()
            weChatUtils.getWeiXinToken()
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
        if (weChatDisposable?.isDisposed!!.not()) {
            weChatDisposable?.dispose()
        }
    }

    override fun userResponseProcessor(user: User?) {
        if (null != user){
            toast(R.string.toast_login_success)
            CApplication.INSTANCE.loginUser = user
            startActivity(Intent(getParentActivity(), MainActivity::class.java))
            getParentActivity().finish()
        }
    }

    override fun thirdPartyNotBinded(openId:String) {
        val fragment = ThirdAccountBindPhoneFragment()
        val bundle = Bundle()
        bundle.putString("openId", openId)
        fragment.arguments = bundle
        startFragment(android.R.id.content, fragment, "login")
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().finish()
        return true
    }
}
