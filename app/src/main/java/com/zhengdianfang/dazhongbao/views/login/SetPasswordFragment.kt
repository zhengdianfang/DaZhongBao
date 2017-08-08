package com.zhengdianfang.dazhongbao.views.login


import android.content.Intent
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
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.home.MainActivity
import kotlinx.android.synthetic.main.fragment_set_password.*


/**
 * A simple [Fragment] subclass.
 */
class SetPasswordFragment : BaseFragment<LoginActivity>() , IRegisterView{

    private val modifyPasswordEditText by lazy { view?.findViewById<EditText>(R.id.passwordEditText)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }
    private val toolbar by lazy { view?.findViewById<Toolbar>(R.id.toolbar)!! }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_set_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        PresenterFactory.mLoginPresenter.attachView(this)
        toolbar.backListener = {
            getParentActivity().supportFragmentManager.popBackStack()
        }
        submitButton.setOnClickListener {
            PresenterFactory.mLoginPresenter.setPassword(modifyPasswordEditText.text.toString(),
                    passwordEditText.text.toString(),
                    CApplication.INSTANCE.loginUser?.token ?: "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterFactory.mLoginPresenter.detachView()
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun receiverSmsCode(code: String) {}

    override fun receiverUser(user: User) {
        toast(R.string.toast_set_password_successful)
        CApplication.INSTANCE.loginUser = user
        startActivity(Intent(getParentActivity(), MainActivity::class.java))
    }
}// Required empty public constructor
