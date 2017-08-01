package com.zhengdianfang.dazhongbao.views.basic

import android.support.v4.app.Fragment
import android.widget.Toast

/**
 * Created by dfgzheng on 31/07/2017.
 */
abstract class BaseFragment<out A: BaseActivity>: Fragment(), IView {

    fun getParentActivity(): A {
        return activity as A
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
}