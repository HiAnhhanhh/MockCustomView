package com.example.examplegraphview_pb2

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.concurrent.fixedRateTimer
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.round
import kotlin.math.sqrt

class CircularProgress @JvmOverloads constructor(
    context: Context, attrs : AttributeSet, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private var progress: Float = 0.0f
    private val radiusArc = 400f
    private var sweepAngleArc = 0.0F
    private var oval = RectF()

    companion object {
        private const val START_ANGLE_ARC = 150F
        private const val SWEEP_ANGLE_ARC = 240F
    }
    private var backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 80f
    }
    private var mainPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.blue)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 80f
    }

    private var textPaint = Paint().apply {
        textSize = 100f
        strokeWidth = 10f
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
    }

    private var paintCircle: Paint = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.blue)
    }

    fun setProgress(v: Float) {
        val currentProgress = this.progress
        Log.d("check_v", "setProgress: "+ v)
        val animator = ValueAnimator().apply {
            setValues(PropertyValuesHolder.ofFloat(
                "percent",
                currentProgress,
                v
            ))
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val newValue = it.getAnimatedValue("percent") as Float
                this@CircularProgress.progress = newValue
                invalidate()
            }
        }
        animator.start()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val left = width/2f - radiusArc
        val top = height/2f - radiusArc
        val right = width - left
        val bottom = height - top
        oval.set(left, top, right, bottom)
        canvas?.drawArc(
            oval,
            START_ANGLE_ARC,
            SWEEP_ANGLE_ARC,
            false,
            backgroundPaint
        )
        canvas?.drawArc(oval, START_ANGLE_ARC, progress * SWEEP_ANGLE_ARC, false, mainPaint)
        canvas?.drawCircle(width/2f,height/2f, 320f,paintCircle )
        var textInput = Math.round(progress*240*10)/10
        canvas?.drawText(textInput.toString(),width/2f,height/2f+20f,textPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (distance(
                            event.x,
                            event.y,
                            width / 2F,
                            height / 2F
                        ) >= radiusArc - 80F && distance(
                            event.x,
                            event.y,
                            width / 2F,
                            height / 2F
                        ) <= radiusArc + 80F && event.y <= height / 2 + radiusArc / 2
                    ) {
                        val xPosition = event.x
                        val yPosition = event.y
                        if (oval.contains(xPosition, yPosition)) {
                            sweepAngleArc = getSweepAngle(xPosition, yPosition, radiusArc)
                            Log.d("check_", "onTouchEvent: "+ sweepAngleArc)
                            setProgress(sweepAngleArc/240)
                        }
                        invalidate()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    performClick()
                }
            }
        }

        return true
    }


    override fun performClick(): Boolean {
        super.performClick()
        launchMissile()
        return true
    }

    private fun launchMissile() {
        Log.d("TemperatureView", "#launchMissile(): Missile launched")
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)))
    }
//
    private fun getSweepAngle(xTouch: Float, yTouch: Float, radius: Float): Float {
        val startAngle = 5 * PI / 6.0 // 150°

        val angle = if (yTouch > height / 2) {
            if (xTouch < width / 2) {
                acos((xTouch - width / 2) / radius)
            } else {
                (2 * PI + acos((xTouch - width / 2) / radius)).toFloat()
            }
        } else {
            (2 * PI - acos((xTouch - width / 2) / radius)).toFloat()
        }

        var sweepAngle = ((angle - startAngle) * 180 / PI).toFloat()
        if (sweepAngle < 0F) {
            sweepAngle = 0F
        }
        if (sweepAngle > 240F) {
            sweepAngle = 240F
        }
        return sweepAngle
    }


}