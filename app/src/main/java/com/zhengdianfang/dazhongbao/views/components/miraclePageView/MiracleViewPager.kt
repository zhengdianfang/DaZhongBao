/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.zhengdianfang.dazhongbao.views.components.miraclePageView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.zhengdianfang.dazhongbao.R

/**
 * Created by mikeafc on 15/10/26.<br></br>
 * MiracleViewPager is a super extension for ViewPager.<br></br>
 * It's actually a RelativeLayout in order to display indicator, MiracleViewPager offers some usual
 * method delegate for ViewPager, you can also invoke more method by call getViewPager() and get the actual
 * ViewPager.
 */
class MiracleViewPager : RelativeLayout, MiracleViewPagerFeature {

    enum class ScrollMode private constructor(internal var id: Int) {
        HORIZONTAL(0), VERTICAL(1);


        companion object {

            internal fun getScrollMode(id: Int): ScrollMode {
                for (scrollMode in values()) {
                    if (scrollMode.id == id)
                        return scrollMode
                }
                throw IllegalArgumentException()
            }
        }
    }

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    enum class ScrollDirection private constructor(internal var id: Int) {
        NONE(0), BACKWARD(1), FORWARD(2);


        companion object {

            internal fun getScrollDirection(id: Int): ScrollDirection {
                for (direction in values()) {
                    if (direction.id == id)
                        return direction
                }
                throw IllegalArgumentException()
            }
        }
    }

    private val size: Point
    private val maxSize: Point

    private var ratio = java.lang.Float.NaN

    //Maximum width of child when enable multiScreen.
    private var maxWidth = -1

    //Maximum height of child when enable multiScreen.
    private var maxHeight = -1

    private var viewPager: MiracleViewPagerView? = null
    private var pagerIndicator: MiracleViewPagerIndicator? = null

    private var timer: TimerHandler? = null


    constructor(context: Context) : super(context) {
        size = Point()
        maxSize = Point()
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        size = Point()
        maxSize = Point()
        initView()
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        size = Point()
        maxSize = Point()
        initView()
    }

    private fun initView() {
        viewPager = MiracleViewPagerView(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager!!.id = viewPager!!.hashCode()
        } else {
            viewPager!!.id = View.generateViewId()
        }

        addView(viewPager, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MiracleViewPager)
        setAutoScroll(ta.getInt(R.styleable.MiracleViewPager_upv_autoscroll, 0))
        setInfiniteLoop(ta.getBoolean(R.styleable.MiracleViewPager_upv_infiniteloop, false))
        setRatio(ta.getFloat(R.styleable.MiracleViewPager_upv_ratio, java.lang.Float.NaN))
        setScrollMode(ScrollMode.getScrollMode(ta.getInt(R.styleable.MiracleViewPager_upv_scrollmode, 0)))
        disableScrollDirection(ScrollDirection.getScrollDirection(ta.getInt(R.styleable.MiracleViewPager_upv_disablescroll, 0)))
        setMultiScreen(ta.getFloat(R.styleable.MiracleViewPager_upv_multiscreen, 1f))
        setAutoMeasureHeight(ta.getBoolean(R.styleable.MiracleViewPager_upv_automeasure, false))
        setItemRatio(ta.getFloat(R.styleable.MiracleViewPager_upv_itemratio, java.lang.Float.NaN).toDouble())
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (!java.lang.Float.isNaN(ratio)) {
            val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((widthSize / ratio).toInt(), View.MeasureSpec.EXACTLY)
        }
        size.set(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec))
        if (maxWidth >= 0 || maxHeight >= 0) {
            maxSize.set(maxWidth, maxHeight)
            constrainTo(size, maxSize)
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(size.x, View.MeasureSpec.EXACTLY)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(size.y, View.MeasureSpec.EXACTLY)
        }
        if (viewPager!!.constrainLength > 0) {
            if (viewPager!!.constrainLength == heightMeasureSpec) {
                viewPager!!.measure(widthMeasureSpec, heightMeasureSpec)
                setMeasuredDimension(size.x, size.y)
            } else {
                if (viewPager!!.scrollMode == ScrollMode.HORIZONTAL) {
                    super.onMeasure(widthMeasureSpec, viewPager!!.constrainLength)
                } else {
                    super.onMeasure(viewPager!!.constrainLength, heightMeasureSpec)
                }
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTimer()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    override fun onStartTemporaryDetach() {
        super.onStartTemporaryDetach()
        stopTimer()
    }

    override fun onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach()
        startTimer()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (timer != null) {
            val action = ev.action
            if (action == MotionEvent.ACTION_DOWN) {
                stopTimer()
            }
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                startTimer()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun initIndicator(): IMiracleIndicatorBuilder {
        disableIndicator()
        pagerIndicator = MiracleViewPagerIndicator(context)
        pagerIndicator!!.setViewPager(viewPager!!)
        pagerIndicator!!.setIndicatorBuildListener(object : MiracleViewPagerIndicator.MiracleViewPagerIndicatorListener {
            override fun build() {
                removeView(pagerIndicator)
                addView(pagerIndicator, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        })
        return pagerIndicator!!
    }

    override fun initIndicator(focusColor: Int, normalColor: Int, radiusInPixel: Int, gravity: Int): IMiracleIndicatorBuilder {
        return initIndicator()
                .setFocusColor(focusColor)
                .setNormalColor(normalColor)
                .setRadius(radiusInPixel)
                .setGravity(gravity)
    }

    override fun initIndicator(focusColor: Int, normalColor: Int, strokeColor: Int, strokeWidth: Int, radiusInPixel: Int, gravity: Int): IMiracleIndicatorBuilder {
        return initIndicator()
                .setFocusColor(focusColor)
                .setNormalColor(normalColor)
                .setStrokeWidth(strokeWidth)
                .setStrokeColor(strokeColor)
                .setRadius(radiusInPixel)
                .setGravity(gravity)
    }

    override fun initIndicator(focusResId: Int, normalResId: Int, gravity: Int): IMiracleIndicatorBuilder {
        return initIndicator()
                .setFocusResId(focusResId)
                .setNormalResId(normalResId)
                .setGravity(gravity)
    }

    override fun initIndicator(focusBitmap: Bitmap, normalBitmap: Bitmap, gravity: Int): IMiracleIndicatorBuilder {
        return initIndicator()
                .setFocusIcon(focusBitmap)
                .setNormalIcon(normalBitmap)
                .setGravity(gravity)
    }

    override fun disableIndicator() {
        if (pagerIndicator != null) {
            removeView(pagerIndicator)
            pagerIndicator = null
        }
    }

    val indicator: IMiracleIndicatorBuilder?
        get() = pagerIndicator

    private val mTimerHandlerListener = object : TimerHandler.TimerHandlerListener {
        override fun callBack() {
            scrollNextPage()
        }
    }

    override fun setAutoScroll(intervalInMillis: Int) {
        if (0 == intervalInMillis) {
            return
        }
        if (timer != null) {
            disableAutoScroll()
        }
        timer = TimerHandler(this, mTimerHandlerListener, intervalInMillis.toLong())
        startTimer()
    }

    override fun setAutoScroll(intervalInMillis: Int, intervalArray: SparseIntArray) {
        if (0 == intervalInMillis) {
            return
        }
        if (timer != null) {
            disableAutoScroll()
        }
        timer = TimerHandler(this, mTimerHandlerListener, intervalInMillis.toLong())
        timer!!.specialInterval = intervalArray
        startTimer()
    }

    override fun disableAutoScroll() {
        stopTimer()
        timer = null
    }

    override fun setScrollMode(scrollMode: ScrollMode) {
        viewPager!!.scrollMode = scrollMode
    }

    override fun setInfiniteLoop(enableLoop: Boolean) {
        viewPager!!.setEnableLoop(enableLoop)
    }

    override fun setMaxWidth(width: Int) {
        maxWidth = width
    }

    override fun setRatio(ratio: Float) {
        this.ratio = ratio
        viewPager!!.ratio = ratio
    }

    override fun setHGap(pixel: Int) {
        val screenWidth = context.resources.displayMetrics.widthPixels
        viewPager!!.setMultiScreen((screenWidth - pixel) / screenWidth.toFloat())
        viewPager!!.pageMargin = pixel
    }

    override fun setMaxHeight(height: Int) {
        maxHeight = height
    }

    override fun disableScrollDirection(direction: ScrollDirection) {

    }

    override fun scrollNextPage() {
        if (viewPager != null && viewPager!!.adapter != null && viewPager!!.adapter.count > 0) {
            viewPager?.adapter?.notifyDataSetChanged()
            val curr = viewPager!!.currentItemFake
            var nextPage = 0
            if (curr < viewPager!!.adapter.count - 1) {
                nextPage = curr + 1
            }
            viewPager!!.setCurrentItemFake(nextPage, true)
        }
    }

    override fun setMultiScreen(ratio: Float) {
        if (ratio <= 0 || ratio > 1) {
            throw IllegalArgumentException("")
        }
        if (ratio <= 1f) {
            viewPager!!.setMultiScreen(ratio)
        }
    }

    override fun setAutoMeasureHeight(status: Boolean) {
        viewPager!!.setAutoMeasureHeight(status)
    }

    override fun setItemRatio(ratio: Double) {
        viewPager!!.setItemRatio(ratio)
    }

    override fun setItemMargin(left: Int, top: Int, right: Int, bottom: Int) {
        viewPager!!.setItemMargin(left, top, right, bottom)
    }

    override fun setScrollMargin(left: Int, right: Int) {
        viewPager!!.setPadding(left, 0, right, 0)
    }

    fun setOffscreenPageLimit(limit: Int) {
        viewPager!!.offscreenPageLimit = limit
    }

    /**
     * delegate viewpager
     */

    var adapter: PagerAdapter?
        get() = if (viewPager!!.adapter == null) null else (viewPager!!.adapter as MiracleViewPagerAdapter).adapter
        set(adapter) {
            viewPager!!.adapter = adapter
        }

    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        if (pagerIndicator == null) {
            viewPager!!.addOnPageChangeListener(listener)
        } else {
            pagerIndicator!!.setPageChangeListener(listener)
        }
    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        viewPager!!.setCurrentItem(item, smoothScroll)
    }

    var currentItem: Int
        get() = viewPager!!.currentItem
        set(item) {
            viewPager!!.currentItem = item
        }

    val nextItem: Int
        get() = viewPager!!.nextItem

    fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: ViewPager.PageTransformer) {
        viewPager!!.setPageTransformer(reverseDrawingOrder, transformer)
    }

    fun getViewPager(): ViewPager {
        return viewPager!!
    }

    private fun constrainTo(size: Point, maxSize: Point) {
        if (maxSize.x >= 0) {
            if (size.x > maxSize.x) {
                size.x = maxSize.x
            }
        }
        if (maxSize.y >= 0) {
            if (size.y > maxSize.y) {
                size.y = maxSize.y
            }
        }
    }

    private fun startTimer() {
        if (timer == null || viewPager == null || !timer!!.isStopped) {
            return
        }
        timer!!.listener = mTimerHandlerListener
        timer!!.removeCallbacksAndMessages(null)
        timer!!.tick(0)
        timer!!.isStopped = false
    }

    private fun stopTimer() {
        if (timer == null || viewPager == null || timer!!.isStopped) {
            return
        }
        timer!!.removeCallbacksAndMessages(null)
        timer!!.listener = null
        timer!!.isStopped = true
    }

    override fun setInfiniteRatio(infiniteRatio: Int) {
        if (viewPager!!.adapter != null && viewPager!!.adapter is MiracleViewPagerAdapter) {
            (viewPager!!.adapter as MiracleViewPagerAdapter).setInfiniteRatio(infiniteRatio)
        }
    }

}
