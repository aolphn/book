package me.aolphn.book.utils

import android.os.Process

object ProcessUtils{

    fun limit():String{
        return catCurrentProcessInfo("limits")
    }
    fun oomAdj():String{
        return catCurrentProcessInfo("oom_adj")
    }
    fun oomScore():String{
        return catCurrentProcessInfo("oom_score")
    }
    fun oomScoreAdj():String{
        return catCurrentProcessInfo("oom_score_adj")
    }
    private fun catCurrentProcessInfo(info:String) :String{
        return runCommand("cat /proc/${Process.myPid()}/$info")
    }
    private fun runCommand(cmd:String):String{
        val process = Runtime.getRuntime().exec(cmd)
        val byteArray = process.inputStream.readBytes() ?:ByteArray(0)
        return String(byteArray)
    }
}