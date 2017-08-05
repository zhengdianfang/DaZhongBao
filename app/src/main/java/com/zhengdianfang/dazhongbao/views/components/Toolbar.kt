package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R


/**
 * Created by dfgzheng on 29/07/2017.
 */
class Toolbar(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var toolbarTitleText: Any? = ""
    private var theme = 0
    private val LIGHT = 0
    private val DARK = 1
    private var enableBack = true
    private val titleView by lazy { findViewById<TextView>(R.id.toolbarTitleView) }
    private val toolbarBackButton by lazy { findViewById<ImageButton>(R.id.toolbarBackButton) }

    init {
        val typeArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.app_toolbar, 0, 0)

        toolbarTitleText = typeArray?.getString(R.styleable.app_toolbar_title)
        theme = typeArray?.getInteger(R.styleable.app_toolbar_toolbar_theme, LIGHT) ?: LIGHT
        enableBack = typeArray?.getBoolean(R.styleable.app_toolbar_enable_back, false) ?: false


    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val view = LayoutInflater.from(context).inflate(R.layout.app_toolbar, this)
        if (theme == LIGHT) {
            view.setBackgroundColor(Color.WHITE)
            titleView.setTextColor(ContextCompat.getColor(context, R.color.toolbar_title_light_color))
            toolbarBackButton.setImageResource(R.drawable.toolbar_light_back)
        }else if(theme == DARK){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            titleView.setTextColor(ContextCompat.getColor(context, R.color.toolbar_title_dark_color))
            toolbarBackButton.setImageResource(R.drawable.toolbar_dark_back)
        }
        toolbarBackButton.visibility = if (enableBack) View.VISIBLE else View.GONE

        if(toolbarTitleText is String) {
            view.findViewById<TextView>(R.id.toolbarTitleView).text = toolbarTitleText as String
        }else if (toolbarTitleText is Int){
            view.findViewById<TextView>(R.id.toolbarTitleView).setText(toolbarTitleText as Int)
        }

    }
}