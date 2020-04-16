package me.aolphn.book.utils

import android.os.Process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object ProcessUtils{


    suspend  fun catCurrentProcessInfo(info:CatInfo) :String{
        return runCommand("cat /proc/${Process.myPid()}/${info.value}")
    }
    private suspend fun runCommand(cmd:String):String{
        return withContext(Dispatchers.IO){
            LogUtils.checkThreadI("check run command")
            val process = Runtime.getRuntime().exec(cmd)
            val byteArray = process.inputStream.readBytes()
            String(byteArray)
        }
    }

}
enum class CatInfo(val value:String){
    oomAdj("oom_adj"),
    oomScore("oom_score"),
    oomScoreAdj("oom_score_adj"),
    sessionId("sessionid"),
    statm("statm"),
    io("io"),
    mountinfo("mountinfo"),
    sched("sched"),
    cgroup("cgroup"),
    status("status"),
    limits("limits"),
}
