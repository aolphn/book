package me.aolphn.book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.runBlocking
import me.aolphn.book.adapter.BaseAdapter
import me.aolphn.book.adapter.IData
import me.aolphn.book.databinding.ActivityProcessInfoBinding
import me.aolphn.book.utils.LogUtils
import me.aolphn.book.utils.ProcessUtils

class ProcessInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProcessInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        runBlocking {
            val limits = ProcessUtils.limit()
            val oomAdj = ProcessUtils.oomAdj()
            val oomScore = ProcessUtils.oomScore()
            val oomScoreAdj = ProcessUtils.oomScoreAdj()
            LogUtils.checkThreadI("runBlocking")
            runOnUiThread {
                val dataList = ArrayList<IData>().apply {
                    add(object :IData{
                        override fun title(): String = "limits"

                        override fun content(): String = limits
                    })
                    add(object :IData{
                        override fun title(): String = "oomAdj"

                        override fun content(): String = oomAdj
                    })
                    add(object :IData{
                        override fun title(): String = "oomScore"

                        override fun content(): String = oomScore
                    })
                    add(object :IData{
                        override fun title(): String = "oomScoreAdj"

                        override fun content(): String = oomScoreAdj
                    })
                }
                val adapter = BaseAdapter(this@ProcessInfoActivity,dataList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this@ProcessInfoActivity)
            }
        }
    }


}
