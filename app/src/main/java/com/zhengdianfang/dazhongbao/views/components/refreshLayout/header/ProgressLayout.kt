package com.zhengdianfang.dazhongbao.views.components.refreshLayout.header

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.IHeaderView
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.OnAnimEndListener


/**
 * Created by dfgzheng on 05/08/2017.
 */
class ProgressLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr), IHeaderView {
    companion object {
        private val CIRCLE_DIAMETER = 40
        private val CIRCLE_DIAMETER_LARGE = 56
        private val LARGE = MaterialProgressDrawable.LARGE
        private val DEFAULT = MaterialProgressDrawable.DEFAULT
        private val CIRCLE_BG_LIGHT = 0xfffafafa
        private val DEFAULT_CRICLE_TARGET = 64
        private val MAX_PROGRESS_ANGLE = .8f
        private val MAX_ALPHA = 255
        private val STARTING_PROGRESS_ALPHA = (.3f * MAX_ALPHA).toInt()
    }

    private var mCircleWidth = 0
    private var mCircleHeight = 0

    private val mProgress by lazy {
        val progress = MaterialProgressDrawable(context!!, this)
        progress.setBackgroundColor(CIRCLE_BG_LIGHT.toInt())
        progress
    }

    private val mCircleView by lazy {
        val view = CircleImageView(context!!, CIRCLE_BG_LIGHT.toInt(), (CIRCLE_DIAMETER / 2).toFloat())
        view.setImageDrawable(mProgress)
        view.visibility = View.GONE
        view.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        view
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?): this(context, null)

    init {
        mCircleHeight = (CIRCLE_DIAMETER * resources.displayMetrics.density).toInt()
        mCircleWidth = (CIRCLE_DIAMETER * resources.displayMetrics.density).toInt()
        ViewCompat.setChildrenDrawingOrderEnabled(this, true)
    }

    fun setProgressBackgroundColroSchemeResoure(@ColorRes colorRes: Int) {
       setProgressBackgroundColroSchemeResoure(colorRes)
    }

    fun setProgressBackgroundColorSchemeColor(@ColorInt color: Int) {
        mCircleView.setBackgroundColor(color)
        mProgress.setBackgroundColor(color)
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.

     * @param colorResIds
     */
    fun setColorSchemeResources(@ColorRes vararg colorResIds: Int) {
        val res = resources
        val colorRes = IntArray(colorResIds.size)
        for (i in colorResIds.indices) {
            colorRes[i] = res.getColor(colorResIds[i])
        }
        setColorSchemeColors(colorRes)
    }

    /**
     * Set the colors used in the progress animation. The first
     * color will also be the color of the bar that grows in response to a user
     * swipe gesture.

     * @param colors
     */
    fun setColorSchemeColors(colors: IntArray) {
        mProgress.setColorSchemeColors(colors)
    }

    /**
     * One of DEFAULT, or LARGE.
     */
    fun setSize(size: Int) {
        if (size.toLong() != MaterialProgressDrawable.LARGE && size.toLong() != MaterialProgressDrawable.DEFAULT) {
            return
        }
        val metrics = resources.displayMetrics
        if (size.toLong() == MaterialProgressDrawable.LARGE) {
            mCircleWidth = (CIRCLE_DIAMETER_LARGE * metrics.density).toInt()
            mCircleHeight = mCircleWidth
        } else {
            mCircleWidth = (CIRCLE_DIAMETER * metrics.density).toInt()
            mCircleHeight = mCircleWidth
        }
        // force the bounds of the progress circle inside the circle view to
        // update by setting it to null before updating its size and then
        // re-setting it
        mCircleView.setImageDrawable(null)
        mProgress.updateSizes(size)
        mCircleView.setImageDrawable(mProgress)
    }

    override fun getView(): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPullingDown(fraction: Float, maxHeaderHeight: Float, headHeight: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPullRefreshing(fraction: Float, maxHeaderHeight: Float, headHeight: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFinish(animEndListener: OnAnimEndListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reset() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}