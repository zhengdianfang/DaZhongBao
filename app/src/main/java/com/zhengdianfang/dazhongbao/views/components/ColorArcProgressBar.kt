package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PixelUtils

/**
 * Created by bruce on 11/6/14.
 */
class ColorArcProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var paint: Paint? = null
    protected lateinit var suffixTextPaint: Paint
    protected lateinit var bottomTextPaint: Paint

    private val rectF = RectF()

    private var strokeWidth: Float = 0.toFloat()
    private var suffixTextSize: Float = 0.toFloat()
    private var bottomTextSize: Float = 0.toFloat()
    private var bottomText: String? = null
    private var suffixTextColor: Int = 0
    private var bottomTextColor: Int = 0

    var progress = 0
        set(progress) {
            field = progress
            if (this.progress > max) {
                field %= max
            }
            invalidate()
        }
    var max: Int = 0
        set(max) {
            if (max > 0) {
                field = max
                invalidate()
            }
        }
    private var finishedStrokeColor: Int = 0
    private var unfinishedStrokeColor: Int = 0
    private var arcAngle: Float = 0.toFloat()
    private var suffixText = ""
    private var suffixTextPadding: Float = 0.toFloat()

    private var arcBottomHeight: Float = 0.toFloat()

    private val default_finished_color = Color.WHITE
    private val default_unfinished_color = Color.rgb(72, 106, 176)
    private val default_text_color = Color.rgb(66, 145, 241)
    private val default_suffix_text_size: Float
    private val default_suffix_padding: Float
    private val default_bottom_text_size: Float
    private val default_stroke_width: Float
    private val default_suffix_text: String
    private val default_max = 100
    private val default_arc_angle = 360 * 0.7f
    private var default_text_size: Float = 0.toFloat()
    private val min_size: Int

    init {

        default_text_size = PixelUtils.sp2px(context, 18f)
        min_size = PixelUtils.dp2px(context, 100f).toInt()
        default_text_size = PixelUtils.sp2px(context, 40f)
        default_suffix_text_size = PixelUtils.sp2px(context, 15f)
        default_suffix_padding = PixelUtils.dp2px(context, 4f)
        default_suffix_text = ""
        default_bottom_text_size = PixelUtils.sp2px(context, 10f)
        default_stroke_width = PixelUtils.dp2px(context, 8f)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0)
        initByAttributes(attributes)
        attributes.recycle()

        initPainters()
    }


    protected fun initByAttributes(attributes: TypedArray) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color)
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color)
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle)
        max = attributes.getInt(R.styleable.ArcProgress_arc_max, default_max)
        progress = attributes.getInt(R.styleable.ArcProgress_arc_progress, 0)
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width)
        suffixTextColor = attributes.getColor(R.styleable.ArcProgress_arc_suffix_text_color, default_text_color)
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size)
        suffixText = if (TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text))) default_suffix_text else attributes.getString(R.styleable.ArcProgress_arc_suffix_text)
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding)
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size)
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text)
        bottomTextColor = attributes.getInt(R.styleable.ArcProgress_arc_bottom_text_color, default_text_color)
    }

    protected fun initPainters() {
        suffixTextPaint = TextPaint()
        suffixTextPaint.color = suffixTextColor
        suffixTextPaint.textSize = suffixTextSize
        suffixTextPaint.isAntiAlias = true

        bottomTextPaint = TextPaint()
        bottomTextPaint.color = bottomTextColor
        bottomTextPaint.textSize = bottomTextSize
        bottomTextPaint.isAntiAlias = true

        paint = Paint()
        paint!!.color = default_unfinished_color
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = strokeWidth
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun invalidate() {
        initPainters()
        super.invalidate()
    }

    fun getStrokeWidth(): Float {
        return strokeWidth
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        this.invalidate()
    }

    fun getSuffixTextSize(): Float {
        return suffixTextSize
    }

    fun setSuffixTextSize(suffixTextSize: Float) {
        this.suffixTextSize = suffixTextSize
        this.invalidate()
    }

    fun getBottomText(): String? {
        return bottomText
    }

    fun setBottomText(bottomText: String) {
        this.bottomText = bottomText
        this.invalidate()
    }

    fun getBottomTextSize(): Float {
        return bottomTextSize
    }

    fun setBottomTextSize(bottomTextSize: Float) {
        this.bottomTextSize = bottomTextSize
        this.invalidate()
    }

    fun getFinishedStrokeColor(): Int {
        return finishedStrokeColor
    }

    fun setFinishedStrokeColor(finishedStrokeColor: Int) {
        this.finishedStrokeColor = finishedStrokeColor
        this.invalidate()
    }

    fun getUnfinishedStrokeColor(): Int {
        return unfinishedStrokeColor
    }

    fun setUnfinishedStrokeColor(unfinishedStrokeColor: Int) {
        this.unfinishedStrokeColor = unfinishedStrokeColor
        this.invalidate()
    }

    fun getArcAngle(): Float {
        return arcAngle
    }

    fun setArcAngle(arcAngle: Float) {
        this.arcAngle = arcAngle
        this.invalidate()
    }

    fun getSuffixText(): String {
        return suffixText
    }

    fun setSuffixText(suffixText: String) {
        this.suffixText = suffixText
        this.invalidate()
    }

    fun getSuffixTextPadding(): Float {
        return suffixTextPadding
    }

    fun setSuffixTextPadding(suffixTextPadding: Float) {
        this.suffixTextPadding = suffixTextPadding
        this.invalidate()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return min_size
    }

    override fun getSuggestedMinimumWidth(): Int {
        return min_size
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, View.MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f)
        val radius = width / 2f
        val angle = (360 - arcAngle) / 2f
        arcBottomHeight = radius * (1 - Math.cos(angle / 180 * Math.PI)).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startAngle = 270 - arcAngle / 2f
        val finishedSweepAngle = this.progress / max.toFloat() * arcAngle
        var finishedStartAngle = startAngle
        if (this.progress == 0) finishedStartAngle = 0.01f
        paint!!.color = unfinishedStrokeColor
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint!!)
        paint!!.color = finishedStrokeColor
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint!!)

        val text = progress.toString()
        val textHeight = suffixTextPaint.descent() + suffixTextPaint.ascent()
        val textBaseline = (height - textHeight) / 2.0f
        canvas.drawText(suffixText, (width - suffixTextPaint.measureText(text)) / 4.0f, textBaseline , suffixTextPaint)

        if (arcBottomHeight == 0f) {
            val radius = width / 2f
            val angle = (360 - arcAngle) / 2f
            arcBottomHeight = radius * (1 - Math.cos(angle / 180 * Math.PI)).toFloat()
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            val bottomTextBaseline = height.toFloat() - arcBottomHeight - (bottomTextPaint.descent() + bottomTextPaint.ascent()) / 2
            canvas.drawText(getBottomText(), (width - bottomTextPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, bottomTextPaint)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding())
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize())
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText())
        bundle.putInt(INSTANCE_PROGRESS, progress)
        bundle.putInt(INSTANCE_MAX, max)
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor())
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor())
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle())
        bundle.putString(INSTANCE_SUFFIX, getSuffixText())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH)
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE)
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING)
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE)
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT)
            max = bundle.getInt(INSTANCE_MAX)
            progress = bundle.getInt(INSTANCE_PROGRESS)
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR)
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
            suffixText = bundle.getString(INSTANCE_SUFFIX)
            initPainters()
            super.onRestoreInstanceState(bundle.getParcelable<Parcelable>(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    companion object {

        private val INSTANCE_STATE = "saved_instance"
        private val INSTANCE_STROKE_WIDTH = "stroke_width"
        private val INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size"
        private val INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding"
        private val INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size"
        private val INSTANCE_BOTTOM_TEXT = "bottom_text"
        private val INSTANCE_TEXT_SIZE = "text_size"
        private val INSTANCE_TEXT_COLOR = "text_color"
        private val INSTANCE_PROGRESS = "progress"
        private val INSTANCE_MAX = "max"
        private val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
        private val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
        private val INSTANCE_ARC_ANGLE = "arc_angle"
        private val INSTANCE_SUFFIX = "suffix"
    }
}