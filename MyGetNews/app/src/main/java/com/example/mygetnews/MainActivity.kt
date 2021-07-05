package com.example.mygetnews

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygetnews.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup


//swipe -> implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01'
//<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
// 코루틴 -> implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'
// jsoup >implementation 'org.jsoup:jsoup:1.13.1'

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    val url = "https://www.daum.net"
    val scope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun getNews() {
        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(url).get()
            val headlines = doc.select("ul.list_txt>li>a")
            for (news in headlines) {
                adapter.items.add(MyData(news.text(), news.absUrl("href")))
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }

    private fun init() {

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getNews()
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        // 가로줄
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        adapter = MyAdapter(ArrayList<MyData>())
        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                val intent = Intent(ACTION_VIEW, Uri.parse(adapter.items[position].Url))
                startActivity(intent)

            }
        }

        binding.recyclerView.adapter = adapter
        getNews()


    }
}