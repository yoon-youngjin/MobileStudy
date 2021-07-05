package com.example.myengvoc.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myengvoc.data.MyData

class MyViewModel :ViewModel() {
    val liveItems  = MutableLiveData<ArrayList<MyData>>()
    val items = ArrayList<MyData>()

    fun add(data: MyData) {
        items.add(data)
        liveItems.value = items
    }

    fun getSize():Int {
        return items.size
    }

}
