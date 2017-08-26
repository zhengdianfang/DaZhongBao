package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
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
    private var enableConfirm = true
    private var confirmLabel = ""
    private val titleView by lazy { findViewById<TextView>(R.id.toolbarTitleView) }
    private val toolbarBackButton by lazy { findViewById<ImageButton>(R.id.toolbarBackButton) }
    private val confirmButton by lazy { findViewById<Button>(R.id.confirmButton) }
    var backListener: (() -> Unit)? = null
    var confirmListener: (() -> Unit)? = null

    private var toolbarBackground: Int? = 0

    init {
        val typeArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.app_toolbar, 0, 0)

        toolbarTitleText = typeArray?.getString(R.styleable.app_toolbar_title)
        theme = typeArray?.getInteger(R.styleable.app_toolbar_toolbar_theme, LIGHT) ?: LIGHT
        enableBack = typeArray?.getBoolean(R.styleable.app_toolbar_enable_back, true) ?: true
        enableConfirm = typeArray?.getBoolean(R.styleable.app_toolbar_enable_confirm, false) ?: false
        confirmLabel = typeArray?.getString(R.styleable.app_toolbar_confirm_label) ?: ""
        toolbarBackground = typeArray?.getResourceId(R.styleable.app_toolbar_toolbar_background, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        toolbarBackButton.setOnClickListener {
            backListener?.invoke()
        }

        confirmButton.setOnClickListener {
           confirmListener?.invoke()
        }
        confirmButton.isEnabled = enableConfirm
        confirmButton.text = confirmLabel
        if (toolbarBackground != null && toolbarBackground != 0){
            setBackgroundResource(toolbarBackground!!)
        }
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

    fun setTitle(title: String){
        titleView.text = title
    }
}