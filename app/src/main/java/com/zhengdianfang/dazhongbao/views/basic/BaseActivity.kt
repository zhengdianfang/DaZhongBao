package com.zhengdianfang.dazhongbao.views.basic

import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by dfgzheng on 25/07/2017.
 */
abstract class BaseActivity : AppCompatActivity() {
    val mDialogFragment by lazy {
        val dialogFragment = DialogFragment()
        dialogFragment
    }
    fun showLoadingDialog() {
        mDialogFragment.show(supportFragmentManager, "loadingDialog")
    }

    fun hideLoadingDialog() {
        mDialogFragment.dismiss()
    }

    fun toast(msg: Any, long: Boolean = false) {
        val duration = if(long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
       if (msg is String) {
           Toast.makeText(this, msg, duration).show()
       }else if(msg is Int){
           Toast.makeText(this, msg, duration).show()
       }
    }
}