package me.aolphn.book.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import java.util.jar.Attributes

class GrayFrameLayout: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context,attributes: AttributeSet):super(context,attributes)
    private val mPaint=Paint()
    init {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0.0f)
        val colorFilterMatrix = ColorMatrixColorFilter(colorMatrix)
        mPaint.colorFilter = colorFilterMatrix

    }

    override fun onDraw(canvas: Canvas?) {
//        canvas?.saveLayer(null,mPaint)
        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.saveLayer(null,mPaint)
        super.dispatchDraw(canvas)
    }
}