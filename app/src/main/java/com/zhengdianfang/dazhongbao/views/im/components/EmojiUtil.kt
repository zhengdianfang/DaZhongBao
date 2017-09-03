package com.zhengdianfang.dazhongbao.views.im.components

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ImageSpan
import com.zhengdianfang.dazhongbao.R
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

/**
 * Created by zheng on 16/6/29.
 */

class EmojiUtil {

    var mEmojiPageList = ArrayList<ArrayList<String>>()
    var mEmojis = ConcurrentHashMap<String, Int>()

    init {
        mEmojis.put("[):]", R.mipmap.ee_1)
        mEmojis.put("[:D]", R.mipmap.ee_2)
        mEmojis.put("[;)]", R.mipmap.ee_3)
        mEmojis.put("[:-o]", R.mipmap.ee_4)
        mEmojis.put("[:p]", R.mipmap.ee_5)
        mEmojis.put("[(H)]", R.mipmap.ee_6)
        mEmojis.put("[:@]", R.mipmap.ee_7)
        mEmojis.put("[:s]", R.mipmap.ee_8)
        mEmojis.put("[:$]", R.mipmap.ee_9)
        mEmojis.put("[:(]", R.mipmap.ee_10)
        mEmojis.put("[:(]", R.mipmap.ee_11)
        mEmojis.put("[:|]", R.mipmap.ee_12)
        mEmojis.put("[(a)]", R.mipmap.ee_13)
        mEmojis.put("[8o|]", R.mipmap.ee_14)
        mEmojis.put("[8-|]", R.mipmap.ee_15)
        mEmojis.put("[+o(]", R.mipmap.ee_16)
        mEmojis.put("[<o)]", R.mipmap.ee_17)
        mEmojis.put("[|-)]", R.mipmap.ee_18)
        mEmojis.put("[*-)]", R.mipmap.ee_19)
        mEmojis.put("[*-)]", R.mipmap.ee_20)
        mEmojis.put("[:-*]", R.mipmap.ee_21)
        mEmojis.put("[^o)]", R.mipmap.ee_22)
        mEmojis.put("[8-)]", R.mipmap.ee_23)
        mEmojis.put("[(|)]", R.mipmap.ee_24)
        mEmojis.put("[(u)]", R.mipmap.ee_25)
        mEmojis.put("[(S)]", R.mipmap.ee_26)
        mEmojis.put("[(*)]", R.mipmap.ee_27)
        mEmojis.put("[(#)]", R.mipmap.ee_28)
        mEmojis.put("[(R)]", R.mipmap.ee_29)
        mEmojis.put("[({)]", R.mipmap.ee_30)
        mEmojis.put("[(})]", R.mipmap.ee_31)
        mEmojis.put("[(k)]", R.mipmap.ee_32)
        mEmojis.put("[(F)]", R.mipmap.ee_33)
        mEmojis.put("[(W)]", R.mipmap.ee_34)
        mEmojis.put("[(D)]", R.mipmap.ee_35)


        var pageCount = 0
        if (mEmojis.size % PAGE_SIZE == 0) {
            pageCount = mEmojis.size / PAGE_SIZE
        } else {
            pageCount = mEmojis.size / PAGE_SIZE + 1
        }
        var i = 0
        while (i < pageCount) {
            val startIndex = i * PAGE_SIZE
            var endIndex = startIndex + PAGE_SIZE
            if (endIndex > mEmojis.size) {
                endIndex = mEmojis.size
            }

            val list = ArrayList<String>()
            var mapNum = 0
            for ((key) in mEmojis) {
                if (mapNum >= startIndex) {
                    list.add(key)
                    if (mapNum == endIndex - 1) {
                        break
                    }
                }
                mapNum++
            }

            if (list.size < PAGE_SIZE) {
                i = list.size
                while (i < PAGE_SIZE - 1) {
                    val str = String()
                    list.add(str)
                    i++
                }
            }
            if (list.size == PAGE_SIZE) {
                list.add(EMOJI_DELETE_NAME)
            }
            mEmojiPageList.add(list)
            i++
        }
    }

    private fun replaceImage(context: Context, spannableString: SpannableString, patten: Pattern, start: Int, size: Int = EMOJI_SIZE) {
        val matcher = patten.matcher(spannableString)
        while (matcher.find()) {
            val key = matcher.group()
            if (matcher.start() < start) {
                continue
            }
            val imageID = if (mEmojis[key] == null) 0 else mEmojis[key]
            if (imageID == 0) {
                continue
            }

            val drawable = ContextCompat.getDrawable(context, imageID!!)
            drawable.setBounds(0, 0, dip(context, size.toFloat()), dip(context, size.toFloat()))
            val imageSpan = ImageSpan(drawable)
            val end = matcher.start() + key.length
            spannableString.setSpan(imageSpan, matcher.start(), end,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            if (end < spannableString.length) {
                replaceImage(context, spannableString, patten, end, size)
            }
            break
        }
    }

    @JvmOverloads
    fun getSpannableString(context: Context, str: String, size: Int = EMOJI_SIZE): SpannableString {
        val spannableString = SpannableString(str)
        val express = "\\[[^\\]]+\\]"
        val patten = Pattern.compile(express, Pattern.CASE_INSENSITIVE)
        replaceImage(context, spannableString, patten, 0, size)
        return spannableString
    }


    fun addEmoji(context: Context, text: String): SpannableString? {
        if (TextUtils.isEmpty(text)) {
            return null
        }
        val imageID = mEmojis[text]
        val drawable = ContextCompat.getDrawable(context, imageID!!)
        drawable.setBounds(0, 0, dip(context, 14f), dip(context, 14f))
        val imageSpan = ImageSpan(drawable)
        val spannable = SpannableString(text)
        spannable.setSpan(imageSpan, 0, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }


    fun dip(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {

        val EMOJI_DELETE_NAME = "EMOJI_DELETE_NAME"
        var sEmojiUtil: EmojiUtil? = null
        val PAGE_SIZE = 20
        val EMOJI_SIZE = 20

        val instance: EmojiUtil
            get() {
                if (sEmojiUtil == null) {
                    sEmojiUtil = EmojiUtil()
                }
                return sEmojiUtil!!
            }
    }
}
