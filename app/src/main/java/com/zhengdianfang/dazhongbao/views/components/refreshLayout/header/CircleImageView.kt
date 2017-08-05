package com.zhengdianfang.dazhongbao.views.components.refreshLayout.header

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.animation.Animation
import android.widget.ImageView


/**
 * Created by dfgzheng on 05/08/2017.
 */
class CircleImageView(context: Context, color: Int, radius: Float) : ImageView(context) {

    private var mListener: Animation.AnimationListener? = null
    private var mShadowRadius: Int = 0

    init {
        val density = getContext().resources.displayMetrics.density
        val diameter = (radius * density * 2f).toInt()
        val shadowYOffset = (density * Y_OFFSET).toInt()
        val shadowXOffset = (density * X_OFFSET).toInt()

        mShadowRadius = (density * SHADOW_RADIUS).toInt()

        val circle: ShapeDrawable
        if (elevationSupported()) {
            circle = ShapeDrawable(OvalShape())
            ViewCompat.setElevation(this, SHADOW_ELEVATION * density)
        } else {
            val oval = OvalShadow(mShadowRadius, diameter)
            circle = ShapeDrawable(oval)
            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, circle.paint)
            circle.paint.setShadowLayer(mShadowRadius.toFloat(), shadowXOffset.toFloat(), shadowYOffset.toFloat(),
                    KEY_SHADOW_COLOR)
            val padding = mShadowRadius
            // set padding so the inner image sits correctly within the shadow.
            setPadding(padding, padding, padding, padding)
        }
        circle.paint.color = color
        setBackgroundDrawable(circle)
    }

    private fun elevationSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= 21
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!elevationSupported()) {
            setMeasuredDimension(measuredWidth + mShadowRadius * 2, measuredHeight + mShadowRadius * 2)
        }
    }

    fun setAnimationListener(listener: Animation.AnimationListener) {
        mListener = listener
    }

    override fun onAnimationStart() {
        super.onAnimationStart()
        if (mListener != null) {
            mListener!!.onAnimationStart(animation)
        }
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        if (mListener != null) {
            mListener!!.onAnimationEnd(animation)
        }
    }

    /**
     * Update the background color of the circle image view.

     * @param colorRes Id of a color resource.
     */
    fun setBackgroundColorRes(colorRes: Int) {
        setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    override fun setBackgroundColor(color: Int) {
        if (background is ShapeDrawable) {
            (background as ShapeDrawable).paint.color = color
        }
    }

    private inner class OvalShadow(shadowRadius: Int, private val mCircleDiameter: Int) : OvalShape() {
        private val mRadialGradient: RadialGradient
        private val mShadowPaint: Paint = Paint()

        init {
            mShadowRadius = shadowRadius
            mRadialGradient = RadialGradient((mCircleDiameter / 2).toFloat(), (mCircleDiameter / 2).toFloat(),
                    mShadowRadius.toFloat(), intArrayOf(FILL_SHADOW_COLOR, Color.TRANSPARENT), null, Shader.TileMode.CLAMP)
            mShadowPaint.shader = mRadialGradient
        }

        override fun draw(canvas: Canvas, paint: Paint) {
            val viewWidth = this@CircleImageView.width
            val viewHeight = this@CircleImageView.height
            canvas.drawCircle((viewWidth / 2).toFloat(), (viewHeight / 2).toFloat(), (mCircleDiameter / 2 + mShadowRadius).toFloat(),
                    mShadowPaint)
            canvas.drawCircle((viewWidth / 2).toFloat(), (viewHeight / 2).toFloat(), (mCircleDiameter / 2).toFloat(), paint)
        }
    }

    companion object {

        private val KEY_SHADOW_COLOR = 0x1E000000
        private val FILL_SHADOW_COLOR = 0x3D000000
        // PX
        private val X_OFFSET = 0f
        private val Y_OFFSET = 1.75f
        private val SHADOW_RADIUS = 3.5f
        private val SHADOW_ELEVATION = 4
    }
}