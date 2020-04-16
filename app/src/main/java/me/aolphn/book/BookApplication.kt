package me.aolphn.book

import android.app.Activity
import android.app.Application
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Process
import android.util.Log
import android.util.SparseIntArray
import android.view.Choreographer
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.aolphn.book.utils.LogUtils
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

class BookApplication: Application() {
    val map = HashMap<Int,String>(1024)
    val currentCount = 0
    var startTime = 0L
    var endTime = 0L
    var lastFrame = 0L
    init {
        LogUtils.i(msg = "check application init time:${System.currentTimeMillis()}")
//        Looper.getMainLooper().setMessageLogging {msg->
//            if (msg.startsWith(">>>>>")) {
//                map[currentCount]=msg
//                startTime = System.currentTimeMillis();
//            }else if (msg.startsWith("<<<<<")) {
//                endTime = System.currentTimeMillis();
//            }
//            val diff = endTime - startTime
//            if (diff > 20) {
//                LogUtils.i(msg ="check our printer:$msg,diff:$diff,size:${map.size}")
//            }
//        }
//        Choreographer.getInstance().postFrameCallback {
//            val diff = it-lastFrame
//            lastFrame = it
//            LogUtils.i(msg = "check last frame time:$diff")
//        }
        val originalDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (e is TimeoutException && "FinalizerWatchdogDaemon" == t.name) {
                LogUtils.w(msg = "FinalizerWatchdogDaemon timeout we ignore it")
            } else {
                originalDefaultHandler.uncaughtException(t,e)
            }
        }
        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
                
            }

            override fun onActivityDestroyed(activity: Activity?) {
                
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                
            }

            override fun onActivityStopped(activity: Activity?) {
                
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (BuildConfig.enableGray) {
                    appGray(activity)
                }
            }
        })
    }

    private fun appGray(activity: Activity?){
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        activity?.window?.decorView?.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
    }
    suspend fun doTimeConsumingTask():Int{
        return 1
    }
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val realTime = Process.getStartElapsedRealtime()
            LogUtils.i(msg = "check real start time:$realTime")
        }
    }
}