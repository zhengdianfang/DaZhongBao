package com.zhengdianfang.dazhongbao.views.im.components

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.zhengdianfang.dazhongbao.helpers.PixelUtils


/**
 * Created by zheng on 16/6/29.
 */

object KeyboardUtils {

    val DEF_KEYBOARD_HEAGH_WITH_DP = 300f
    val EXTRA_DEF_KEYBOARDHEIGHT = "extra_def_keyboardheight"

    fun getDefKeyboardHeight(context: Context): Int {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        return preference.getInt(EXTRA_DEF_KEYBOARDHEIGHT, PixelUtils.dp2px(context, DEF_KEYBOARD_HEAGH_WITH_DP).toInt())
    }

    fun setDefKeyboardHeight(context: Context, softKeyboardHeight: Int) {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        return preference.edit().putInt(EXTRA_DEF_KEYBOARDHEIGHT, softKeyboardHeight).apply()
    }

    /**
     * 开启软键盘
     */
    fun openSoftKeyboard(et: EditText?) {
        if (et != null) {
            et.isFocusable = true
            et.isFocusableInTouchMode = true
            et.requestFocus()
            val inputManager = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(et, 0)
        }
    }

    /**
     * 关闭软键盘
     */
    fun closeSoftKeyboard(context: Activity) {

        try {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
