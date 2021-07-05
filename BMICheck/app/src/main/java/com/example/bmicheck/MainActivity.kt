package com.example.bmicheck

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmicheck.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    fun init() {

        val bmi = binding.editTextWeight.text.toString().toDouble() / (binding.editTextHeight.text.toString().toDouble()/100).pow(2.0)
        var resultString : String = ""
        binding.bmiButton.setOnClickListener {
            when {
                bmi >= 35 -> {
                    resultString ="고도 비만"
                    binding.imageView.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                }
                bmi >= 23 -> {
                    resultString = "비만"
                    binding.imageView.setImageResource(R.drawable.ic_baseline_sentiment_dissatisfied_24)
                }
                bmi >= 18 -> {
                    resultString = "정상"
                    binding.imageView.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
                }
                else -> {
                    resultString ="저체중"
                    binding.imageView.setImageResource(R.drawable.ic_baseline_sentiment_dissatisfied_24)
                }


            }
            Toast.makeText(this,resultString,Toast.LENGTH_LONG).show()
        }
    }
}