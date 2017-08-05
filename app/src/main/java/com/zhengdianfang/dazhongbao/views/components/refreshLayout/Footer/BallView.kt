package com.zhengdianfang.dazhongbao.views.components.refreshLayout.Footer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.IBottomView
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.OnAnimEndListener
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils

/**
 * Created by dfgzheng on 05/08/2017.
 */
class BallView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr), IBottomView{

    private val DEFAULT_SIZE = 50f
    private val scaleFloats = arrayOf(1f, 1f, 1f)
    private val mUpdateListeners = mutableMapOf<ValueAnimator, ValueAnimator.AnimatorUpdateListener>()

    private val cricleSpacing by lazy { PixelUtils.dp2px(context!!, 4f)}
    private val mPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.WHITE
        p.style = Paint.Style.FILL
        p.isAntiAlias = true
        p
    }

    private val animtores by lazy {
        val list = arrayListOf<ValueAnimator>()
        val delays = arrayOf(120L, 240L, 360L)
        for (i in 0..2){
            val scaleAnimator = ValueAnimator.ofFloat(1f, 0.3f, 1f)
            scaleAnimator.duration = 750
            scaleAnimator.repeatCount = ValueAnimator.INFINITE
            scaleAnimator.startDelay = delays[i]
            mUpdateListeners.put(scaleAnimator, ValueAnimator.AnimatorUpdateListener { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            })
            list.add(scaleAnimator)
        }
        list
    }

    val normalColor = 0xffeeeeee
    val animtingColor = 0xffe75946

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?): this(context, null)

    init {
        val default_size = PixelUtils.dp2px(context!!, DEFAULT_SIZE)
        layoutParams = FrameLayout.LayoutParams(default_size.toInt(), default_size.toInt(), Gravity.CENTER)

    }

    fun setIndicatorColor(color: Int) {
       mPaint.color = color
    }

    override fun onDraw(canvas: Canvas?) {
        val radius = (Math.min(width, height) - cricleSpacing * 2) / 6
        val x = width/2 - (radius * 2 + cricleSpacing)
        val y = height / 2
        for (i in 0..2) {
            canvas?.save()
            val translateX = x + (radius * 2) * i + cricleSpacing * i
            canvas?.translate(translateX, y.toFloat())
            canvas?.scale(scaleFloats[i], scaleFloats[i])
            canvas?.drawCircle(0f, 0f ,radius, mPaint)
            canvas?.restore()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animtores.forEach {
            it.cancel()
        }
    }

    fun startAnim() {
        if(!isStarted()){
            animtores.forEach {
                val updateListener = mUpdateListeners[it]
                it.addUpdateListener(updateListener)
                it.start()
            }
        }
        setIndicatorColor(animtingColor.toInt())
    }

    fun stopAnim() {
        animtores.forEach {
            if (it.isStarted) {
                it.end()
            }
        }
        setIndicatorColor(normalColor.toInt())
    }

    private fun  isStarted(): Boolean {
        animtores.forEach { return it.isStarted }
        return false
    }

    override fun getView(): View {
        return this
    }

    override fun onPullingUp(fraction: Float, maxHeaderHeight: Float, headHeight: Float) {
        stopAnim()
    }

    override fun onPullRefreshing(fraction: Float, maxHeaderHeight: Float, headHeight: Float) {
        startAnim()
    }

    override fun onFinish(animEndListener: OnAnimEndListener) {
        stopAnim()
    }

    override fun reset() {
        stopAnim()
    }
}