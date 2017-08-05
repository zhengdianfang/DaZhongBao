package com.zhengdianfang.dazhongbao.views.components.refreshLayout

import android.view.View

/**
 * Created by dfgzheng on 05/08/2017.
 */
interface IBottomView {
    fun getView(): View

    fun onPullingUp(fraction: Float, maxHeaderHeight: Float, headHeight: Float)

    fun onPullRefreshing(fraction: Float, maxHeaderHeight: Float, headHeight: Float)

    fun onFinish(animEndListener: OnAnimEndListener)

    fun reset()
}