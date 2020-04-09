package me.aolphn.book

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import me.aolphn.book.view.GrayFrameLayout


class ScrollingActivity : AppCompatActivity() {
    companion object{
        val TAG = "ScrollActivity"
    }
    fun hookLayout() {
        LayoutInflaterCompat.setFactory2(layoutInflater,object :LayoutInflater.Factory2{
            override fun onCreateView(
                parent: View?,
                name: String?,
                context: Context?,
                attrs: AttributeSet?
            ) = handleLayout(name,context,attrs)

            override fun onCreateView(
                name: String?,
                context: Context?,
                attrs: AttributeSet?
            ): View? {
                return null
            }
        })
    }

    private fun handleLayout(name: String?,
                     context: Context?,
                     attrs: AttributeSet?):View?{
        if ("FrameLayout" == name) {
            val count = attrs!!.attributeCount
            for (i in 0..count) {
                val attrName = attrs.getAttributeName(i)
                val attrValue = attrs.getAttributeValue(i)
                if ("id" == attrName) {
                    val id = attrValue.substring(1).toInt()
                    val resourceName = resources.getResourceName(id)
                    if ("android:id/content" == resourceName) {
                        return GrayFrameLayout(context!!,attrs)
                    }
                }
            }
        }
        return null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        layoutInflater.factory2
        setContentView(R.layout.activity_scrolling)
//        val paint = Paint()
//        val cm = ColorMatrix()
//        cm.setSaturation(0f)
//        paint.colorFilter = ColorMatrixColorFilter(cm)
//        window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//
//        return handleLayout(name,context,attrs)?:super.onCreateView(name, context, attrs)
//    }
}
