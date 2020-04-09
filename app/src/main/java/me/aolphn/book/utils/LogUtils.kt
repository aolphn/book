package me.aolphn.book.utils

import android.util.Log
import java.lang.Exception

/**
 * @author zhangkh
 *
 */
object  LogUtils {
    private const val TAG = "LogUtils";
    fun i(tag:String = TAG,msg:String){
        Log.i(tag,msg)
    }
    fun w(tag:String = TAG,msg:String){
        Log.w(tag,msg)
    }
    fun printStack(){
        Thread.currentThread().stackTrace.forEach {
            w(msg = it.toString())
        }
    }


}