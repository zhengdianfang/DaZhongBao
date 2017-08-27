package com.zhengdianfang.dazhongbao.helpers

/**
 * Created by dfgzheng on 18/08/2017.
 */

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View

/**
 * Created by zheng on 16/6/17.
 */

object SpannableStringUtils {


    fun addClickSpan(spannableString: SpannableString, matchStr: String, color: Int, onClick: (view: View)->Unit?) {
        val start = spannableString.toString().indexOf(matchStr)
        val end = start + matchStr.length
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick?.invoke(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    }

    fun addClickSpan(spannableString: SpannableStringBuilder, matchStr: String, color: Int, onClick: (view: View)->Unit?) {
        val start = spannableString.toString().indexOf(matchStr)
        val end = start + matchStr.length
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick?.invoke(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    }

    fun addImageSpan(spannableString: SpannableStringBuilder, matchStr: String, drawable: Drawable, width: Int, height: Int, onClick: (view: View)->Unit?) {
        val start = spannableString.toString().indexOf(matchStr)
        val end = start + matchStr.length
        drawable.setBounds(0, 0, width, height)
        spannableString.setSpan(ImageSpan(drawable), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick?.invoke(widget)
            }
        }
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    }


    fun addColorSpan(content: String, matchStr: String, color: Int): SpannableString {
        val spannableString = SpannableString(content)
        val start = spannableString.toString().indexOf(matchStr)
        val end = start +  matchStr.length
        val foregroundColorSpan = ForegroundColorSpan(color)
        spannableString.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun addColorSpan(content: SpannableString, matchStr: String, color: Int): SpannableString {
        val spannableString = SpannableString(content)
        val start = spannableString.toString().indexOf(matchStr)
        val end = start +  matchStr.length
        val foregroundColorSpan = ForegroundColorSpan(color)
        spannableString.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun addColorSpan(content: String, matchStr: String, color: Int, size: Int): SpannableString {
        val spannableString = SpannableString(content)
        val start = spannableString.toString().indexOf(matchStr)
        val end = start +  matchStr.length
        val foregroundColorSpan = ForegroundColorSpan(color)
        val absoluteSizeSpan = AbsoluteSizeSpan(size)
        spannableString.setSpan(absoluteSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun addSizeSpan(content: String, matchStr: String, size: Int): SpannableString {
        val spannableString = SpannableString(content)
        val start = spannableString.toString().indexOf(matchStr)
        val end = start +  matchStr.length
        val absoluteSizeSpan = AbsoluteSizeSpan(size)
        spannableString.setSpan(absoluteSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}
