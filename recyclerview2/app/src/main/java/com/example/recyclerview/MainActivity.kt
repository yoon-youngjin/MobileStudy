package com.example.recyclerview

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var data:ArrayList<MyData> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter
    lateinit var tts:TextToSpeech
    var isTTSReady = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemMenu1 -> {
                recyclerView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
            }
            R.id.itemMenu2 -> {
                recyclerView.layoutManager = GridLayoutManager(applicationContext,3)
            }

            R.id.itemMenu3 -> {
                recyclerView.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initRecyclerView()
        initTTs()
    }

    private fun initTTs() {

        tts = TextToSpeech(this,TextToSpeech.OnInitListener {
            isTTSReady = true
            tts.language = Locale.US
        })
    }

    override fun onStop() {
        super.onStop()
        tts.stop()

    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        adapter = MyAdapter(data)

        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {
            override fun OnItemClick(
                viewHolder: MyAdapter.ViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
//                tts.speak(data.word,TextToSpeech.QUEUE_ADD,null,null)
                if(viewHolder.textView2.visibility ==View.GONE)
                    viewHolder.textView2.visibility = View.VISIBLE
                else
                    viewHolder.textView2.visibility = View.GONE
            }

        }

    }

    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.wordss))
        while(scan.hasNextLine()) {
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            data.add(MyData(word,meaning))
        }
        scan.close()

    }
}