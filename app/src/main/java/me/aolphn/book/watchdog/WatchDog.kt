package me.aolphn.book.watchdog

import android.R
import android.os.Build
import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.Method


class WatchDogKiller{
    companion object{

        private const val TAG = "WatchDogKiller"
        @Volatile
        private var sWatchdogStopped = false
        @JvmStatic
        fun stopWatchDog() {
            // 建议在在debug包或者灰度包中不要stop，保留发现问题的能力。为了Sample效果，先注释
            //if (!BuildConfig.DEBUG) {
            //    return;
            //}

            // Android P 以后不能反射FinalizerWatchdogDaemon
            if (Build.VERSION.SDK_INT >= 28) {
                Log.w(TAG, "stopWatchDog, do not support after Android P, just return")
                return
            }
            if (sWatchdogStopped) {
                Log.w(TAG, "stopWatchDog, already stopped, just return")
                return
            }
            sWatchdogStopped = true
            Log.w(TAG, "stopWatchDog, try to stop watchdog")
            try {
                val clazz =
                    Class.forName("java.lang.Daemons\$FinalizerWatchdogDaemon")
                val field: Field = clazz.getDeclaredField("INSTANCE")
                field.isAccessible = true
                val watchdog: Any = field.get(null)
                try {
                    val thread: Field = clazz.superclass.getDeclaredField("thread")
                    thread.isAccessible = true
                    thread.set(watchdog, null)
                } catch (t: Throwable) {
                    Log.e(TAG, "stopWatchDog, set null occur error:$t")
                    t.printStackTrace()
                    try {
                        // 直接调用stop方法，在Android 6.0之前会有线程安全问题
                        val method: Method = clazz.superclass.getDeclaredMethod("stop")
                        method.isAccessible = true
                        method.invoke(watchdog)
                    } catch (e: Throwable) {
                        Log.e(TAG, "stopWatchDog, stop occur error:$t")
                        t.printStackTrace()
                    }
                }
            } catch (t: Throwable) {
                Log.w(TAG, "stopWatchDog, get object occur error:$t")
                t.printStackTrace()
            }
        }
        @JvmStatic
        fun checkWatchDogAlive(): Boolean {
            val clazz: Class<*>
            try {
                clazz = Class.forName("java.lang.Daemons\$FinalizerWatchdogDaemon")
                val field: Field = clazz.getDeclaredField("INSTANCE")
                field.setAccessible(true)
                val watchdog: Any = field.get(null)
                val isRunningMethod: Method = clazz.superclass.getDeclaredMethod("isRunning")
                isRunningMethod.setAccessible(true)
                return isRunningMethod.invoke(watchdog) as Boolean
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }


    }

}

class GhostObject{
    @Throws(Throwable::class)
    protected fun finalize() {
        Log.d(
            "ghost",
            "=============fire finalize=============" + Thread.currentThread().name
        )
        Thread.sleep(80000) //每个手机触发 Timeout 的时长不同，比如 vivo 的某些rom 是2分钟，模拟器统一都是10秒钟，所以在模拟器上效果明显
    }
}

object WatchDogUtil{
    @JvmStatic
    fun fireTimeout() {
        val `object` = GhostObject()
    }
    @JvmStatic
    fun resetWatchDogStatus() {
        val alive: Boolean = WatchDogKiller.checkWatchDogAlive()

    }
}