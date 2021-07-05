package com.example.midproject

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 100
    var data: ArrayList<MyData> = ArrayList()
    var data2: ArrayList<MyData> = ArrayList()
    var data3: ArrayList<MyData> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initData()
    }

    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.data))
        while(scan.hasNextLine()) {
            val name:String = scan.nextLine()
            if(name.equals("ssdas")) {
                val model:String = scan.nextLine()
                val a :String = scan.nextLine()
                val cost:String = scan.nextLine()

                data.add(MyData(name,model,a,cost))
            }
            if(name.equals("afdsf")) {
                val model:String = scan.nextLine()
                val cost:String = scan.nextLine()
                val a :String = scan.nextLine()
                data2.add(MyData(name,model,a,cost))
            }
            if(name.equals("fwefd")) {
                val model:String = scan.nextLine()
                val cost:String = scan.nextLine()
                val a :String = scan.nextLine()
                data3.add(MyData(name,model,a,cost))
            }
         }
    }



    private fun initRecyclerView() {
        //Toast.makeText(applicationContext,intent.getStringExtra("data1"),Toast.LENGTH_SHORT).show()
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(data)
        recyclerView.adapter = adapter


        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {
            override fun OnItemClick(
                viewHolder: MyAdapter.ViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                val d = data.name
                val a = data.model
                val b = data.a
                val c = data.cost
                viewHolder.binding.textView4.setBackgroundColor(Color.BLUE)
                val intent = Intent()
                intent.putExtra("data",MyData(d,a,b,c))
                setResult(Activity.RESULT_OK,intent)
                finish()


            }

        }

    }
        }


