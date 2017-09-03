package com.zhengdianfang.dazhongbao.views.im.components


import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import java.util.*

/**
 * Created by zheng on 16/6/29.
 */

class FuncLayout : LinearLayout {

    private val funcViewsMap = SparseArray<View>()
    var mCurrentFuncKey = 0
    private val listenerList = ArrayList<OnFuncKeyBoardListener>()
    private val mOnFuncChangeListener: OnFuncChangeListener? = null
    var mHeight = 0

    interface OnFuncKeyBoardListener {
        fun onFuncPop(height: Int)
        fun onFuncClose()
    }

    interface OnFuncChangeListener {
        fun onFuncChange(key: Int)
    }

    constructor(context: Context) : super(context) {
        orientation = LinearLayout.HORIZONTAL
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        orientation = LinearLayout.HORIZONTAL
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = LinearLayout.HORIZONTAL
    }

    internal fun addFuncView(key: Int, view: View) {
        funcViewsMap.put(key, view)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(view, params)
        view.visibility = View.GONE
    }

    internal fun hideAllFuncView() {
        for (i in 0..funcViewsMap.size() - 1) {
            val keyTemp = funcViewsMap.keyAt(i)
            funcViewsMap.get(keyTemp).setVisibility(View.GONE)
        }
        mCurrentFuncKey = 0
        setMVisibility(false)
    }

    internal fun showFuncView(key: Int) {
        if (funcViewsMap.get(key) == null) {
            return
        }
        for (i in 0..funcViewsMap.size() - 1) {
            val keyTemp = funcViewsMap.keyAt(i)
            if (keyTemp == key) {
                funcViewsMap.get(keyTemp).setVisibility(View.VISIBLE)
            } else {
                funcViewsMap.get(keyTemp).setVisibility(View.GONE)
            }
        }
        mCurrentFuncKey = key
        setMVisibility(true)
        mOnFuncChangeListener?.onFuncChange(mCurrentFuncKey)
    }

    val isOnlyShowSoftKeyboard: Boolean
        get() = mCurrentFuncKey == 0


    internal fun addFunKeyListener(listener: OnFuncKeyBoardListener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener)
        }
    }

    internal fun removeFunKeyListener(listener: OnFuncKeyBoardListener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener)
        }
    }


    internal fun toggleFuncView(key: Int, isSoftKeyboardPop: Boolean, editText: EditText) {
        if (mCurrentFuncKey == key) {
            if (isSoftKeyboardPop) {
                KeyboardUtils.closeSoftKeyboard(context as Activity)
            } else {
                KeyboardUtils.openSoftKeyboard(editText)
            }
        } else {
            if (isSoftKeyboardPop) {
                KeyboardUtils.closeSoftKeyboard(context as Activity)
            }
            showFuncView(key)
        }
    }


    internal fun setMVisibility(b: Boolean) {
        val params = layoutParams as LinearLayout.LayoutParams
        if (b) {
            visibility = View.VISIBLE
            params.height = mHeight
            for (onFuncKeyBoardListener in listenerList) {
                onFuncKeyBoardListener.onFuncPop(mHeight)
            }
        } else {
            visibility = View.GONE
            params.height = 0
            for (onFuncKeyBoardListener in listenerList) {
                onFuncKeyBoardListener.onFuncClose()
            }
        }
        layoutParams = params
    }


}
