package com.zhengdianfang.dazhongbao.views.basic

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
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
        val duration = if(long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        if (msg is String) {
            Toast.makeText(getParentActivity(), msg, duration).show()
        }else if(msg is Int){
            Toast.makeText(getParentActivity(), msg, duration).show()
        }
    }

    fun startFragment(id: Int, nextFragment: Fragment, backStack: String? = null) {
        getParentActivity().startFragment(id, nextFragment, backStack)
    }

    fun replaceFragment(id: Int, nextFragment: Fragment, backStack: String? = null) {
        getParentActivity().replaceFragment(id, nextFragment, backStack)
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun networkError(msg: String) {
        hideLoadingDialog()
        Log.e("network error", msg)
        toast(msg)
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun noLogin() {
        startActivity(Intent(getParentActivity(), LoginActivity::class.java))
    }

    open fun toolbarBackButtonClick() {
        getParentActivity().supportFragmentManager.popBackStack()
    }

    open fun toolbarConfirmButtonClick() {

    }
}