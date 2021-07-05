package com.example.myrecyclerview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
    fun onClick(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}