package com.zhengdianfang.dazhongbao.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class FindPasswordFragment : BaseFragment() , IFindPasswordView {

    private val passwordEditText by lazy { view?.findViewById<EditText>(R.id.passwordEditText)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }
    private val mUserPresenter by lazy { UserPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_set_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mUserPresenter.attachView(this)
        submitButton.setOnClickListener {
            val phoneNumber = arguments?.getString("phoneNumber") ?: ""
            val verifyCode = arguments?.getString("verifyCode") ?: ""
            mUserPresenter.findPassword(passwordEditText.text.toString(), verifyCode, phoneNumber)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUserPresenter.detachView()
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

    override fun findPasswordSuccess(msg: String) {
        toast(msg)
        replaceFragment(android.R.id.content, LoginFragment())
    }
}// Required empty public constructor