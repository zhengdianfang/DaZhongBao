package com.zhengdianfang.dazhongbao.views.basic

import android.content.Intent
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.login.LoginActivity

/**
 * Created by dfgzheng on 25/07/2017.
 */
abstract class BaseActivity : AppCompatActivity() {
    private val mDialogFragment by lazy {
        val dialogFragment = DialogFragment()
        dialogFragment
    }
    open fun showLoadingDialog() {
        mDialogFragment.show(supportFragmentManager, "loadingDialog")
    }

    open fun hideLoadingDialog() {
        if (mDialogFragment.isAdded){
            mDialogFragment.dismiss()
        }
    }

    fun toast(msg: Any, long: Boolean = false) {
        val duration = if(long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
       if (msg is String) {
           Toast.makeText(this, msg, duration).show()
       }else if(msg is Int){
           Toast.makeText(this, msg, duration).show()
       }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 0){
           val fragment = supportFragmentManager.fragments.last()
            if (fragment is BaseFragment && !fragment.isRemoving){
               if(!fragment.onBackPressed()) {
                  finish()
               }
            }
        }else {
            finish()
        }
    }

    open fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    open fun networkError(errorMsg: String) {
        toast(errorMsg)
    }

    open fun noLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun startFragment(id: Int, nextFragment: Fragment, backStack: String? = null) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .add(id, nextFragment)
                .addToBackStack(backStack)
                .commitAllowingStateLoss()
    }

    fun replaceFragment(id: Int, nextFragment: Fragment, backStack: String? = null) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(id, nextFragment)
                .addToBackStack(backStack)
                .commitAllowingStateLoss()
    }
}