package com.example.extoggle

import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val togglebtn = findViewById<ToggleButton>(R.id.toggleButton)

        togglebtn.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) Toast.makeText(this,"Toggle On", Toast.LENGTH_LONG).show()

            else Toast.makeText(this,"Toggle Off", Toast.LENGTH_LONG).show()
        }
    }
}