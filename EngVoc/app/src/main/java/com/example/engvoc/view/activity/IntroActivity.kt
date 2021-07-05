package com.example.engvoc.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    val data = MyData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }
    fun init() {
            binding.vocBtn.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            binding.cardBtn.setOnClickListener {
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
            }
            binding.wrongBtn.setOnClickListener {
                val intent = Intent(this, ThirdActivity::class.java)
                startActivity(intent)
            }
        }
    }
