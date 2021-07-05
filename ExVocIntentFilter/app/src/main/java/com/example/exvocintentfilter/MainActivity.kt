package com.example.exvocintentfilter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exvocintentfilter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var  binding:ActivityMainBinding
    lateinit var adapter:MyAdapter
    var data:ArrayList<MyData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initReCyclerView()

    }

    override fun onStop() {
        super.onStop()

    }
    override fun onDestroy() {
        super.onDestroy()

    }
    private fun initReCyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = MyAdapter(ArrayList<MyData>())

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
        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(
                holder: MyAdapter.ViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                val intent = packageManager.getLaunchIntentForPackage(data.apppackname )
                startActivity(intent)
                }

        }
        binding.recyclerView.adapter = adapter

        val simpleCallBack = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
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
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }


}