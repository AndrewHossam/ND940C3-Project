package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progress: Float = 0f
    private var widthSize = 0
    private var heightSize = 0

    private var buttonText: String = context.getString(R.string.button_name)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
    }

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            progress = it.animatedFraction
            invalidate()
        }
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        duration = 2000
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Clicked) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                buttonText = context.getString(R.string.button_name)
            }
            ButtonState.Completed -> {
                buttonText = "Downloaded"
                valueAnimator.cancel()
                progress = 1f

            }
            ButtonState.Loading -> {
                valueAnimator.start()
                buttonText = context.getString(R.string.button_loading)
            }
        }
    }

    init {
        isClickable = true

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(resources.getColor(R.color.colorPrimary))
        canvas.drawRectProgress()
        canvas.drawCircleProgress()
        canvas.drawTextProgress()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun Canvas.drawRectProgress() {
        when (buttonState) {
            ButtonState.Clicked -> {

            }
            ButtonState.Completed -> {
//                drawColor(resources.getColor(R.color.colorPrimary))

            }
            ButtonState.Loading -> {
                paint.color = resources.getColor(R.color.colorPrimaryDark)

                drawRect(
                    0f, 0f, measuredWidth * progress, measuredHeight.toFloat(), paint
                )
            }
        }
    }

    private val circleRadius = 16.px.toFloat()
    private val arcDiameter = circleRadius * 2

    private var circleRect: RectF = RectF(
        0f,
        0f,
        0.8f * widthSize + arcDiameter,
        heightSize / 2f + arcDiameter
    )

    private fun Canvas.drawCircleProgress() {
        circleRect = RectF(
            0.8f * widthSize,
            heightSize / 2f - circleRadius,
            0.8f * widthSize + arcDiameter,
            heightSize / 2f + circleRadius
        )


        when (buttonState) {
            ButtonState.Clicked -> {

            }
            ButtonState.Completed -> {
                paint.color = resources.getColor(R.color.colorAccent)
                drawArc(
                    circleRect,
                    0f,
                    360f,
                    true,
                    paint
                )

            }
            ButtonState.Loading -> {
                paint.color = resources.getColor(R.color.colorAccent)
                drawArc(
                    circleRect,
                    0f,
                    progress * 360,
                    true,
                    paint
                )
            }
        }

    }

    private val textRect = Rect()

    private fun Canvas.drawTextProgress() {
        paint.color = Color.WHITE
        paint.getTextBounds(buttonText, 0, buttonText.length, textRect)
        drawText(
            buttonText,
            (measuredWidth / 2f),
            (measuredHeight / 2f) - textRect.centerY(),
            paint)
    }
}
