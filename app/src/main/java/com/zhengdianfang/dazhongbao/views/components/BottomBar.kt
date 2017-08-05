package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.zhengdianfang.dazhongbao.R

/**
 * Created by dfgzheng on 04/08/2017.
 */
class BottomBar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val tabs = arrayListOf<Button>()
    private val normalBackgroundResIds = arrayOf(R.drawable.bottom_bar_home_icon, R.drawable.bottom_bar_auction_icon, R.drawable.bottom_bar_push_icon, R.drawable.bottom_bar_personal_icon)
    private val selectedBackgroundResIds = arrayOf(R.drawable.bottom_bar_home_selected_icon, R.drawable.bottom_bar_auction_selected_icon, R.drawable.bottom_bar_push_selected_icon, R.drawable.bottom_bar_personal_selected_icon)
    private val tabItemTexts = arrayOf(R.string.bottom_bar_home_title, R.string.bottom_bar_auction_title, R.string.bottom_bar_push_title, R.string.bottom_bar_personal_title)
    var changeTabListener: ((tabIndex: Int) -> Unit)? = null

    init {
        setBackgroundResource(R.drawable.bottom_bar_background)
        val padding = resources.getDimension(R.dimen.bottom_bar_padding).toInt()
        setPadding(padding, padding, padding, padding)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setTabItemBackground(tabs[0], selectedBackgroundResIds[0])
        tabs.forEachIndexed { index, button ->
            button.setOnClickListener {
                tabs.forEachIndexed { index, button ->  setTabItemBackground(button ,normalBackgroundResIds[index]) }
                setTabItemBackground(it as Button, selectedBackgroundResIds[index])
                changeTabListener?.invoke(index)
            }
        }
    }

    private fun setTabItemBackground(tabItem: Button, backgroundResId: Int) {
        tabItem.setCompoundDrawablesRelativeWithIntrinsicBounds(0, backgroundResId, 0, 0)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (index in 0..3){
            addView(createTabItem(tabItemTexts[index], normalBackgroundResIds[index]))
        }
    }

    private fun createTabItem(titleResId: Int, iconResId: Int): Button {
        val tabItem = Button(context)
        tabItem.setBackgroundResource(android.R.color.transparent)
        tabItem.setCompoundDrawablesRelativeWithIntrinsicBounds(0, iconResId, 0, 0)
        tabItem.compoundDrawablePadding = 16
        tabItem.text = resources.getString(titleResId)
        tabItem.setTextColor(ContextCompat.getColor(context, R.color.bottom_bar_tab_text_color))
        tabItem.setTextColor(Color.BLACK)
        val layoutParams = LinearLayout.LayoutParams(0, resources.getDimension(R.dimen.bottom_bar_height).toInt())
        layoutParams.weight = 1f
        tabItem.layoutParams = layoutParams
        tabs.add(tabItem)
        return tabItem
    }
}