package com.example.mydatalist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
    var data:ArrayList<MyData> = ArrayList()
    lateinit var recyclerView: RecyclerView
    // 오버라이딩 ctrl +'o'
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1,menu) // menu1을 인플레이트해서 menu에 붙힌다
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuitem1 -> {
                recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            }
            R.id.menuitem2 -> {
                recyclerView.layoutManager = GridLayoutManager(this,3)

            }
            R.id.menuitem3 -> {
                recyclerView.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)


            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initRecylerView()
    }

    private fun initRecylerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = MyAdapter(data)
    }

    private fun initData() { // 데이터 추가시키는 작업 수행
        data.add(MyData("201914177 윤영진",30))
        data.add(MyData("item2",12))
        data.add(MyData("item3",20))
        data.add(MyData("item4",15))
        data.add(MyData("item5",8))
        data.add(MyData("item6",10))
        data.add(MyData("item7",19))
        data.add(MyData("item8",20))
        data.add(MyData("item9",15))
        data.add(MyData("item10",7))
        data.add(MyData("item11",10))
    }


}