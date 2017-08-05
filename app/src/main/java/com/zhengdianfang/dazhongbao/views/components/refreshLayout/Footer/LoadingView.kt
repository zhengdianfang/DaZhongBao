package com.zhengdianfang.dazhongbao.views.components.refreshLayout.Footer

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.IBottomView
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.OnAnimEndListener
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils

/**
 * Created by dfgzheng on 05/08/2017.
 */
class LoadingView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ProgressBar(context, attrs, defStyleAttr), IBottomView {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?): this(context, null)

    init {
        val size = PixelUtils.dp2px(context!!, 34f).toInt()
        layoutParams = FrameLayout.LayoutParams(size, size, Gravity.CENTER)
    }

    override fun getView(): View {
        return this
    }

    override fun onPullingUp(fraction: Float, maxHeaderHeight: Float, headHeight: Float) {
    }

    override fun onPullRefreshing(fraction: Float, maxHeaderHeight: Float, headHeight: Float) {
    }

    override fun onFinish(animEndListener: OnAnimEndListener) {
    }

    override fun reset() {
    }
}