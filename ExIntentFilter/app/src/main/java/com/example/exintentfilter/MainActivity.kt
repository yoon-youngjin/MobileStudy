package com.example.exintentfilter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exintentfilter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    var data:ArrayList<MyData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initRecyclerView()
        setContentView(binding.root)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        adapter = MyAdapter(data)
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val applist = packageManager.queryIntentActivities(intent,0)
        if(applist.size>0) {
            for(appinfo in applist) {
                val applabel = appinfo.loadLabel(packageManager)
                val appclass = appinfo.activityInfo.name
                val appicon = appinfo.loadIcon(packageManager)
                val apppackname = appinfo.activityInfo.packageName
                data.add(MyData(applabel.toString(),appclass,apppackname,appicon))
            }
        }

        adapter.itemClickListener = object : MyAdapter.OnItemClickListener {
            override fun OnItemClick(holder: MyAdapter.ViewHolder, view: View, data: MyData, position: Int) {
                val intent = packageManager.getLaunchIntentForPackage(data.apppackagename)
                startActivity(intent)
            }

        }
        binding.recyclerView.adapter = adapter
    }


}