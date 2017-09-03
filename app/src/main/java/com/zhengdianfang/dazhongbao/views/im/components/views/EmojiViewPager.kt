package com.zhengdianfang.dazhongbao.views.im.components.views


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.view.ViewPager
import android.text.SpannableString
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.RelativeLayout
import com.zhengdianfang.dazhongbao.views.im.components.EmojiUtil
import java.util.*

/**
 * Created by zheng on 16/6/29.
 */

class EmojiViewPager : ViewPager {
    private var current = 0
    private val emojiAdapters = ArrayList<EmojiAdapter>()
    private val pageViews = ArrayList<View>()
    var mAddEmojiListener: AddEmojiListener? = null

    interface AddEmojiListener {
        fun addEmojiEvent(spannableString: SpannableString)
        fun deleteEmojiEvent()
    }

    constructor(context: Context) : super(context) {
        initViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initViews()
    }

    private fun initViews() {
        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                current = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        for (strings in EmojiUtil.instance.mEmojiPageList) {
            val gridView = GridView(context)
            val emojiAdapter = EmojiAdapter(context, strings)
            gridView.adapter = emojiAdapter
            emojiAdapters.add(emojiAdapter)
            gridView.setOnItemClickListener { parent, view, position, id ->
                val emojirStr = emojiAdapters[current].getItem(position)
                if (TextUtils.equals(emojirStr, EmojiUtil.EMOJI_DELETE_NAME)) {
                    if (mAddEmojiListener != null) {
                        mAddEmojiListener!!.deleteEmojiEvent()
                    }
                } else {
                    val spannableString = EmojiUtil.instance.addEmoji(context, emojirStr)
                    if (spannableString != null) {
                        if (mAddEmojiListener != null) {
                            mAddEmojiListener!!.addEmojiEvent(spannableString)
                        }
                    }
                }
            }
            gridView.setBackgroundColor(Color.TRANSPARENT)
            gridView.stretchMode = GridView.STRETCH_COLUMN_WIDTH
            gridView.cacheColorHint = 0
            gridView.numColumns = 7
            gridView.horizontalSpacing = 1
            gridView.verticalSpacing = 1
            gridView.setPadding(5, 0, 5, 0)
            gridView.selector = ColorDrawable(Color.TRANSPARENT)
            gridView.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            gridView.gravity = Gravity.CENTER
            pageViews.add(gridView)
            adapter = EmojiViewPagerAdapter(pageViews)
            currentItem = 0
        }
    }
}
