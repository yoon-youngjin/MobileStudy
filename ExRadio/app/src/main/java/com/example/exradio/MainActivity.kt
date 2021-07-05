package com.example.exradio

import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val img =findViewById<ImageView>(R.id.imageView2)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioButton1 -> img.setImageResource(R.drawable.ic_baseline_brightness_2_24)

                R.id.radioButton2 -> img.setImageResource(R.drawable.ic_baseline_brightness_4_24)

                R.id.radioButton3 -> img.setImageResource(R.drawable.ic_baseline_brightness_6_24)
            }

        }

    }
}