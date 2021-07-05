package com.example.testvoc

import RecyclerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testvoc.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var data = MutableLiveData<ArrayList<User>>()
    lateinit var adapter: RecyclerAdapter
    lateinit var binding : ActivityMainBinding
    lateinit var viewmodel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.recycler.layoutManager = LinearLayoutManager(this)

        binding.funON.setOnClickListener {
            var list = ArrayList<User>()
            list.add( User("1213123","123123"))
            list.add( User("1213123","123123"))
            list.add( User("1213123","123123"))
            viewmodel.liveData.postValue(list)
        }

        val dataObserver: Observer<ArrayList<User>> =
            Observer { livedata ->
                data.value = livedata
                val scan = Scanner(resources.openRawResource(R.raw.words))
                while(scan.hasNextLine()) {
                    val word = scan.nextLine()
                    val mean = scan.nextLine()
                    data.value?.add(User(word,mean))

                }
                var newAdapter = RecyclerAdapter(data)
                binding.recycler.adapter = newAdapter
            }

        viewmodel.liveData.observe(this, dataObserver)
    }


}