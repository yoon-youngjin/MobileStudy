package com.example.myfragmentapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myfragmentapp.databinding.ActivityMainBinding

//implementation "androidx.fragment:fragment-ktx:1.3.2"
//implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
//implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val myViewModel:MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}