package com.zhengdianfang.dazhongbao.views.basic

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.login.LoginActivity

/**
 * Created by dfgzheng on 31/07/2017.
 */
abstract class BaseFragment: Fragment(), IView {

    protected val toolBar by lazy { view?.findViewById<Toolbar>(R.id.toolbar) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolBar?.backListener = {
            this.toolbarBackButtonClick()
        }

        toolBar?.confirmListener = {
           this.toolbarConfirmButtonClick()
        }
    }

    fun getParentActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun showLoadingDialog() {
        getParentActivity().showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        getParentActivity().hideLoadingDialog()
    }

    fun toast(msg: Any, long: Boolean = false) {
        getParentActivity().toast(msg, long)
    }

    fun startFragment(id: Int, nextFragment: Fragment, backStack: String? = null) {
        getParentActivity().startFragment(id, nextFragment, backStack)
    }

    fun replaceFragment(id: Int, nextFragment: Fragment, backStack: String? = null) {
        getParentActivity().replaceFragment(id, nextFragment, backStack)
    }

    open fun onBackPressed(): Boolean {
        toolbarBackButtonClick()
        return true
    }

    override fun networkError(msg: String) {
        hideLoadingDialog()
        getParentActivity().networkError(msg)
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        getParentActivity().validateErrorUI(errorMsgResId)
    }

    override fun noLogin() {
        startActivity(Intent(getParentActivity(), LoginActivity::class.java))
    }

    open fun toolbarBackButtonClick() {
        getParentActivity().supportFragmentManager.popBackStack()
    }

    open fun toolbarConfirmButtonClick() {

    }

    fun setStatusBarTheme(theme: Int, backgroundColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getParentActivity().setStatusBarTheme(theme, backgroundColor)
        }
    }
}