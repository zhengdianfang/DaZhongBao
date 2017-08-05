package com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils

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
}