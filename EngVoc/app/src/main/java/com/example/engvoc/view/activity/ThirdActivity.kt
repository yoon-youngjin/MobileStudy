package com.example.engvoc.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.engvoc.R
import com.example.engvoc.databinding.ActivityThirdBinding
import com.example.engvoc.view.fragment.ThirdFragment

class ThirdActivity : AppCompatActivity() {
    lateinit var binding: ActivityThirdBinding
    val thirdFragment = ThirdFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.voc -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.card -> {
                startActivity(Intent(this, SecondActivity::class.java))
                finish()
            }
            R.id.wrong ->{}
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {

            val fragment = supportFragmentManager.beginTransaction()
            //fragment.addToBackStack(null)
            fragment.replace(R.id.framelayout2, thirdFragment)
            fragment.commit()
        }

    }

