package com.example.myrecyclerview

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.example.myrecyclerview.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var data:ArrayList<MyData> = ArrayList()
    var isTtsReady = false
    lateinit var tts:TextToSpeech
    lateinit var binding : ActivityMainBinding
    lateinit var adapter: MyAdapter
    lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initTTS()

    }

    private fun initTTS() {
        tts = TextToSpeech(this,TextToSpeech.OnInitListener {
            isTtsReady = true
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu1,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu1 -> recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            R.id.menu2 -> recyclerView.layoutManager = GridLayoutManager(this, 3)

            R.id.menu3 -> recyclerView.layoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        }

        return super.onContextItemSelected(item)
    }

//    private fun initData() {
//        try {
//        val scan2 = Scanner(openFileInput("out.txt"))
//        readFile(scan2)}
//        catch (e:Exception) {}
//        val scan = Scanner(resources.openRawResource(R.raw.words))
//        readFile(scan)
//
//    }

//    private fun readFile(scan2: Scanner) {
//        while (scan2.hasNext()) {
//            val word = scan2.nextLine()
//            val meaning = scan2.nextLine()
//            data.add(MyData(word, meaning))
//
//        }
//        scan2.close()
//    }

    private fun initRecyclerView() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = MyAdapter(data)
        adapter.itemClickListener = object : MyAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                val intent = packageManager.getLaunchIntentForPackage(data.apppackagename)
                startActivity(intent)
                //Toast.makeText(applicationContext,data.meaning,Toast.LENGTH_SHORT).show()
                //tts.speak(data.word,TextToSpeech.QUEUE_ADD,null,null)
                //holder.itemView.setBackgroundColor(Color.BLUE)
            }

        }
        binding.recyclerView.adapter = adapter

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val applist = packageManager.queryIntentActivities(intent,0)
        if(applist.size>0) {
            for(appinfo in applist) {
                val applabel = appinfo.loadLabel(packageManager)
                val appclass = appinfo.activityInfo.name
                val apppackagename = appinfo.activityInfo.packageName
                val appicon = appinfo.loadIcon(packageManager)
                adapter.items.add(MyData(applabel.toString(),appclass,apppackagename,appicon))
            }
        }

        val simpleCallback = object:ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP,ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition,target.adapterPosition)
                return true

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }


        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }
}