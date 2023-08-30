package com.example.examplegraphview_pb2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class GraphView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val dataSet = mutableListOf<DataPoint>()
    private val backgroundRect = RectF()
    private var chartWidth = 0f
    private var path = Path()
    private var padding = 0f
    private var chartHeight =0f
    private val borderPath = Path()
    private var touchPosition = PointF()
    private var touching = false
    private var index = 0
    private var trianglePath = Path()

//    private val paint = Paint().apply {
//        color = Color.RED
//    }


    private val paintRect = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    private val backgroundTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    private val textMarkerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        typeface = Typeface.create("", Typeface.NORMAL)
        textSize = 30f
    }

    private val linePaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 1f
    }

    private val lineTouchPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
    }

    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = resources.getColor(R.color.red)
    }

    private val borderPathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 8f
        color = Color.RED
    }


    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val dataPointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

//    private val dataPointFillPaint = Paint().apply {
//        color = Color.BLUE
//    }

//    private val dataPointLinePaint = Paint().apply {
//        color = Color.BLUE
//        strokeWidth = 10f
//        isAntiAlias = true
//    }

    private val axisLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        padding = width/12f
        chartHeight = height - padding
        chartWidth = width - 2* padding
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawLine(padding, padding, padding, height.toFloat(), axisLinePaint)
        canvas.drawLine(padding, height.toFloat(), width.toFloat()- padding, height.toFloat(), axisLinePaint)

        drawText(canvas)
        drawAxis(canvas)
        drawPath(canvas)
        drawWhenTouching(canvas)

    }

    private fun drawWhenTouching(canvas: Canvas) {
        if(touching){
            canvas.drawLine(touchPosition.x,
                touchPosition.y,
                touchPosition.x,
                chartHeight+padding,
                lineTouchPaint)

            canvas.drawCircle(touchPosition.x,
                touchPosition.y,
                15f,
                dataPointPaint)

            Log.d("check_point", "drawWhenTouching: "+ touchPosition)
            val xMarker = when (index){
                0 ->  touchPosition.x - padding/2
                in 1 until dataSet.size-3 -> touchPosition.x - padding/2
                else -> touchPosition.x - padding * 3.5f
            }
            val yMarker = touchPosition.y + padding/6

            val textMaker = "Ngày ${dataSet[index].xVal} : ${50-dataSet[index].yVal} °C"

            backgroundRect.set(
                xMarker,
                yMarker - padding * 1.5f,
                xMarker + chartWidth/10 * 4,
                yMarker - padding/2
            )
            trianglePath.reset()
            trianglePath.moveTo(touchPosition.x,touchPosition.y - 15f)
            trianglePath.lineTo(touchPosition.x- padding/8,yMarker-padding/2)
            trianglePath.lineTo(touchPosition.x+ padding/8, yMarker - padding/2)
            canvas.drawPath(trianglePath, backgroundTextPaint)

//            canvas.drawRect(backgroundRect, paintRect)
            canvas.drawRoundRect(backgroundRect,padding/6, padding/6, paintRect)
            canvas.drawText(textMaker,xMarker + padding/4 ,yMarker - padding + padding/10, textMarkerPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                val xPosition = event.x
                val yPosition = event.y
                if ( xPosition in padding..padding + chartWidth && yPosition in padding..padding + chartWidth){
                    touching = true
                    val percent = chartWidth/ dataSet.size
                    index = ((xPosition-padding)/percent).toInt()
                    touchPosition = realPoint(index, dataSet)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                touching = false
                performClick()
            }

            MotionEvent.ACTION_CANCEL -> {
                touching = false
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
        Log.d("ChartView", "#launchMissile(): Missile launched")
    }

    private fun realPoint (index: Int, data : List<DataPoint> ) : PointF {
        val realX = padding + chartWidth*index/(data.size-1)
        val realY = padding+ chartHeight*(data[index].yVal)/50
        return PointF(realX,realY)
    }

    private fun conPoint1(point1: PointF, point2: PointF): PointF {
        val x = (point1.x + point2.x) / 2
        val y =  point1.y
        return PointF(x, y)
    }

    private fun conPoint2(point1: PointF, point2: PointF): PointF {
        val x = (point1.x + point2.x) / 2
        val y =  point2.y
        return PointF(x, y)
    }


    private fun drawPath(canvas: Canvas) {
        path.reset()
        val startRealPoint = realPoint(0,dataSet)
        path.moveTo(startRealPoint.x,startRealPoint.y)
        for (i in 1 until dataSet.size){
            val realPoint = realPoint(i-1,dataSet)
            val nextRealPoint = realPoint(i, dataSet)
            val coinPoint1 = conPoint1(realPoint, nextRealPoint)
            val coinPoint2 = conPoint2(realPoint, nextRealPoint)

            path.cubicTo(
                coinPoint1.x,
                coinPoint1.y,
                coinPoint2.x,
                coinPoint2.y,
                nextRealPoint.x ,
                nextRealPoint.y
            )
        }
//        val endPoint = realPoint(dataSet.size-1,dataSet)
        borderPath.set(path)
        path.lineTo(padding + chartWidth, padding+ chartHeight)
        path.lineTo(padding, padding+ chartHeight)
        path.lineTo(startRealPoint.x,startRealPoint.y)

        canvas.drawPath(path,pathPaint)
        canvas.drawPath(borderPath,borderPathPaint)
    }

    private fun drawAxis(canvas: Canvas) {

        for (i in 0..dataSet.size){
            canvas.drawLine(padding,
                chartHeight/5 * i + padding,
                width.toFloat() - padding,
                chartHeight/5 *i + padding ,
                linePaint)
        }
    }
    private fun drawText(canvas: Canvas) {
        for(i in 0 ..dataSet.size){
            canvas.drawText((i*10).toString(), 30f, chartHeight/5 * (5-i) + padding,textPaint)
        }
    }

    fun setData(newDataSet: List<DataPoint>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        invalidate()
    }
}

data class DataPoint(
    val xVal: String,
    val yVal: Int
)


//    private fun Int.toRealX() = toFloat() /( dataSet.size-1) * (width-padding*2)
//    private fun Int.toRealY() = toFloat() / 50 * (height-padding)

//        dataSet.forEachIndexed { index, currentDataPoint ->
//            val realX = currentDataPoint.xVal.toRealX() + padding
//            val realY = chartHeight - currentDataPoint.yVal.toRealY() + padding
//            Log.d("check_currentDataPoint", "onDraw: "+ currentDataPoint)
//            if (index < dataSet.size -1) {
//                val nextDataPoint = dataSet[index+1]
//                val startX = currentDataPoint.xVal.toRealX() + padding
//                val startY = chartHeight - currentDataPoint.yVal.toRealY() + padding
//                val endX = nextDataPoint.xVal.toRealX() + padding
//                val endY = chartHeight - nextDataPoint.yVal.toRealY() + padding
//                canvas.drawLine(startX, startY, endX, endY, dataPointLinePaint)
//            }
//            canvas.drawCircle(realX, realY, 10f, dataPointFillPaint)
//            canvas.drawCircle(realX, realY, 10f, dataPointPaint)
//        }