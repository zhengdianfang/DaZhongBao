package com.zhengdianfang.dazhongbao.helpers

import android.content.Context

/**
 * Created by dfgzheng on 05/08/2017.
 */
object PixelUtils {

    fun dp2px(context: Context, dpVaule: Float): Float {
       return context.resources.displayMetrics.density * dpVaule + 0.5f
    }

    fun px2dp(context: Context, pxValue: Float): Float {
       return pxValue / context.resources.displayMetrics.density  + 0.5f
    }

    fun sp2px(context: Context, spValue: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return spValue * scale
    }
}