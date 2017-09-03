package com.zhengdianfang.dazhongbao.views.im.components

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText

class EmoticonsEditText : EditText {

    var mOnSizeChangedListener: OnSizeChangedListener? = null
    var mOnBackKeyClickListener: OnBackKeyClickListener? = null


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    interface OnSizeChangedListener {
        fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    }

    interface OnBackKeyClickListener {
        fun onBackKeyClick()
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        if (mOnBackKeyClickListener != null) {
            mOnBackKeyClickListener!!.onBackKeyClick()
        }
        return super.dispatchKeyEventPreIme(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (oldh > 0) {
            if (mOnSizeChangedListener != null) {
                mOnSizeChangedListener!!.onSizeChanged(w, h, oldw, oldh)
            }
        }
    }
}
