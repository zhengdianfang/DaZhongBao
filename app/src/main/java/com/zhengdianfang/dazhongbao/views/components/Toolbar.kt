package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R


/**
 * Created by dfgzheng on 29/07/2017.
 */
class Toolbar(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var toolbarTitleText: Any? = ""

    init {
        val typeArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.app_toolbar, 0, 0)

        toolbarTitleText = typeArray?.getString(R.styleable.app_toolbar_title)

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val view = LayoutInflater.from(context).inflate(R.layout.app_toolbar, this)

        if(toolbarTitleText is String) {
            view.findViewById<TextView>(R.id.toolbarTitleView).text = toolbarTitleText as String
        }else if (toolbarTitleText is Int){
            view.findViewById<TextView>(R.id.toolbarTitleView).setText(toolbarTitleText as Int)
        }

    }
}