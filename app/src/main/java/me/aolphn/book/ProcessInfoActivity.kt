package me.aolphn.book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.runBlocking
import me.aolphn.book.adapter.BaseAdapter
import me.aolphn.book.adapter.IData
import me.aolphn.book.databinding.ActivityProcessInfoBinding
import me.aolphn.book.utils.ProcessUtils

class ProcessInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProcessInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        runBlocking {
            val limits = ProcessUtils.limit()
            runOnUiThread {
                val dataList = ArrayList<IData>().apply {
                    add(object :IData{
                        override fun title(): String = "limits"

                        override fun content(): String = limits
                    })
                }
                val adapter = BaseAdapter(this@ProcessInfoActivity,dataList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this@ProcessInfoActivity)
            }
        }
    }


}
