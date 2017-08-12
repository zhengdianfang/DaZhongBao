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
import com.zhengdianfang.dazhongbao.views.home.MainActivity


/**
 * A simple [Fragment] subclass.
 */
class SetPasswordFragment : BaseFragment() , ISetPasswordView, IFindPasswordView{

    private val passwordEditText by lazy { view?.findViewById<EditText>(R.id.passwordEditText)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_set_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        PresenterFactory.mUserPresenter.attachView(this)
        submitButton.setOnClickListener {
            val phoneNumber = arguments?.getString("phoneNumber") ?: ""
            val verifyCode = arguments?.getString("verifyCode") ?: ""
            if (!phoneNumber.isNullOrEmpty() && !verifyCode.isNullOrEmpty()) {
                PresenterFactory.mUserPresenter.findPassword(passwordEditText.text.toString(), verifyCode, phoneNumber)
            }else{
                PresenterFactory.mUserPresenter.setPassword(passwordEditText.text.toString(), CApplication.INSTANCE.loginUser?.token ?: "")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterFactory.mUserPresenter.detachView()
    }

    override fun onBackPressed(): Boolean {
        toolbarBackButtonClick()
        return true
    }

    override fun toolbarBackButtonClick() {
        getParentActivity().finish()
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun setPasswordSuccess(user: User) {
        toast(R.string.toast_set_password_successful)
        CApplication.INSTANCE.loginUser = user
        replaceFragment(android.R.id.content, SetOrganizationInfoFragment(), "login")
    }
    override fun findPasswordSuccess(msg: String) {
        toast(msg)
        startActivity(Intent(getParentActivity(), MainActivity::class.java))
    }
}// Required empty public constructor
