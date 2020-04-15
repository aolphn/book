package me.aolphn.book

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scrolling.*
import me.aolphn.book.databinding.ContentScrollingBinding
import me.aolphn.book.view.GrayFrameLayout
import me.aolphn.book.watchdog.WatchDogKiller
import me.aolphn.book.watchdog.WatchDogUtil.fireTimeout
import me.aolphn.book.watchdog.WatchDogUtil.resetWatchDogStatus

/**
 * @author OF
 *
 */
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
        val binding = me.aolphn.book.databinding.ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN
        AnimatorInflater.loadAnimator(this,R.animator.anim).apply {
            setTarget(fab)
            start()
            Looper.myQueue().addIdleHandler {
                //面试官：假如我开启了一个永远不停止的动画，那么IdleHandler还会执行嘛？
                //我：。。。。
                showSnakeBar("this is idle handler")
                false
            }
        }
        val content = binding.contentScroll
        content.killWatchDog.setOnClickListener {
            WatchDogKiller.stopWatchDog();
            // 触发生效
            Runtime.getRuntime().gc();
            System.runFinalization();
            resetWatchDogStatus();
        }
        content.triggerTimeout.setOnClickListener {
            // 因为 stopWatchDog需要下一次循环才会生效，这里先post一下
            Handler().postDelayed({
                Thread(Runnable {
                    fireTimeout()
                    Runtime.getRuntime().gc()
                    System.runFinalization()
                }).start()
            }, 100)

            Toast.makeText(this@ScrollingActivity, "请等待。。。。", Toast.LENGTH_SHORT).show()
        }

        content.checkProcessInfo.setOnClickListener {
            startActivity(Intent(this@ScrollingActivity,ProcessInfoActivity::class.java))
        }
    }

//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//
//        return handleLayout(name,context,attrs)?:super.onCreateView(name, context, attrs)
//    }
    private fun showToast(){
        Toast.makeText(this,"self define was invoked",Toast.LENGTH_LONG).show()
    }
    private fun  showSnakeBar(msg:String){
        Snackbar.make(window.decorView,msg,Snackbar.LENGTH_LONG).show()
    }
}
