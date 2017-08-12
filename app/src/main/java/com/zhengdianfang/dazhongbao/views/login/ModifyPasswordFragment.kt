package com.zhengdianfang.dazhongbao.views.login


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.presenters.PresenterFactory
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class ModifyPasswordFragment : BaseFragment() , IFindPasswordView{

    private val modifyPasswordEditText by lazy { view?.findViewById<EditText>(R.id.modifyPasswordEditText)!! }
    private val modifyPasswordConfirmEditText by lazy { view?.findViewById<EditText>(R.id.modifyPasswordConfirmEditText)!! }
    private val modifyPasswordSubmitButton by lazy { view?.findViewById<Button>(R.id.modifyPasswordSubmitButton)!! }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_password_modify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PresenterFactory.mUserPresenter.attachView(this)
        modifyPasswordSubmitButton.setOnClickListener {
            val token = CApplication.INSTANCE.loginUser?.token
            if (null != token) {
                PresenterFactory.mUserPresenter.modifyPassword(modifyPasswordEditText.text.toString(), modifyPasswordConfirmEditText.text.toString(), token)
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
        getParentActivity().supportFragmentManager.popBackStack("setting", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun toolbarConfirmButtonClick() {
        PresenterFactory.mUserPresenter.modifyPassword(modifyPasswordEditText.text.toString(),
                modifyPasswordConfirmEditText.text.toString(),
                CApplication.INSTANCE.loginUser?.token ?: "")
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun findPasswordSuccess(msg: String) {
        toast(msg)
        toolbarBackButtonClick()
    }
}
