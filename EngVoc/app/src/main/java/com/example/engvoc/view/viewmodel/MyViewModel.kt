package com.example.engvoc.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    val liveData = MutableLiveData<Int>()

    fun getValue() :Int {
        return liveData.value!!.toInt()
    }
}